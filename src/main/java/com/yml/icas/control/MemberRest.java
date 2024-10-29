package com.yml.icas.control;

import com.yml.icas.dto.MemberDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@CrossOrigin(origins = "*")
@RequestMapping(path = "/member")
public interface MemberRest {

    @RequestMapping(method = RequestMethod.PUT, value = "/update/{criteria}")
    ResponseEntity<Integer> updateMember(
            @PathVariable(name = "criteria") String criteria, @RequestBody Map<String, Object> dataSet);

    @PostMapping(path = "/signupOld")
        // not used
    ResponseEntity<byte[]> signup(@RequestBody MemberDTO memberDTO);

    @PostMapping(path = "/signup")
    ResponseEntity<Object> signupNew(@RequestBody MemberDTO memberDTO);

    @GetMapping(path = "/{empNo}")
    ResponseEntity<MemberDTO> getMember(@PathVariable(name = "empNo") String empNo);

    @RequestMapping(method = RequestMethod.GET, value = "/get")
    ResponseEntity<Set<MemberDTO>> searchMember(@RequestParam Map<String, Object> searchParams);

    @RequestMapping(method = RequestMethod.GET, value = "/relationship/{rs}")
    ResponseEntity<String[]> getRelationShip(@PathVariable(name = "rs") String rs);
}
