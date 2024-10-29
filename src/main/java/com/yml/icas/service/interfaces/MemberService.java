package com.yml.icas.service.interfaces;

import com.yml.icas.dto.MemberDTO;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Set;

public interface MemberService {
    ResponseEntity<Integer> updateMember(String criteria, Map<String, Object> dataSet);

    ResponseEntity<byte[]> signUp(MemberDTO memberDTO);

    Object signUpNew(MemberDTO memberDTO);

    ResponseEntity<MemberDTO> getMember(String empNo);

    ResponseEntity<String[]> getRelationShip(String rs);

    ResponseEntity<Set<MemberDTO>> searchMember(Map<String, Object> searchParams);
}
