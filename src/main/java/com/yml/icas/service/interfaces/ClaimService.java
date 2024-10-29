package com.yml.icas.service.interfaces;

import com.yml.icas.dto.ClaimDTO;
import com.yml.icas.dto.ClaimOPDDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public interface ClaimService {
    Integer addClaim(ClaimDTO claimDTO);

    ResponseEntity<Integer> saveOpd(ClaimOPDDTO claimOPDDTO);

    ResponseEntity<Integer> updateClaim(Map<String, Object>[] dataSets);

    ClaimDTO getClaim(Integer claimId);

    Set<ClaimDTO> getAllClaim(Map<String, String> params);


    Set<ClaimDTO> getDashboardData(Integer year, String empNo);

    long[] getVoucherIds();
}
