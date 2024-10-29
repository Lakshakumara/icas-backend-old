package com.yml.icas.control;

import com.yml.icas.dto.ClaimDTO;
import com.yml.icas.dto.ClaimOPDDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

//@CrossOrigin(origins = {"http://10.72.80.133:4200", "http://localhost:4200"})
@CrossOrigin(origins = "*")
@RequestMapping(path = "/claim")
public interface ClaimRest {

    @RequestMapping(method = RequestMethod.GET, value = "/voucherIds")
    ResponseEntity<long[]> getVoucherList();
    @RequestMapping(method = RequestMethod.GET, value = "/dashboard/{year}/{empNo}")
    ResponseEntity<Set<ClaimDTO>> getDashboardData(@PathVariable(name = "year") Integer year,
                                                   @PathVariable(name = "empNo") String empNo);

    @PostMapping(path = "/add")
    ResponseEntity<Integer> addClaim(@RequestBody ClaimDTO claimDTO);

    @PostMapping(path = "/opd")
    ResponseEntity<Integer> opdClaimSave(@RequestBody ClaimOPDDTO claimOPDDTO);


    @RequestMapping(method = RequestMethod.GET, value = "/get/{claimId}")
    ResponseEntity<ClaimDTO> getClaim(@PathVariable(name = "claimId") Integer claimId);

    @RequestMapping(method = RequestMethod.GET, value = "/getAll")
    ResponseEntity<Set<ClaimDTO>> getAllClaim(@RequestParam Map<String, String> params);

    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    ResponseEntity<Integer> updateClaims(@RequestBody Map<String, Object>[] dataSet);

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
    ResponseEntity<Boolean> deleteClaimsData(@PathVariable(name = "id") Integer id);
}
