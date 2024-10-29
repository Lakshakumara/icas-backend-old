package com.yml.icas.control;

import com.yml.icas.dto.MemberDTO;
import com.yml.icas.service.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
public class MemberRestImpl implements MemberRest {
    @Autowired
    MemberServiceImpl memberService;

    @Override
    public ResponseEntity<Integer> updateMember(String criteria, Map<String, Object> dataSet) {
        log.info(criteria +"dataset {}",dataSet);
        return memberService.updateMember(criteria, dataSet);
    }

    @Deprecated
    @Override
    public ResponseEntity<byte[]> signup(MemberDTO memberDTO) {
        try {
            return memberService.signUp(memberDTO);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Object> signupNew(MemberDTO memberDTO) {
        try {
            memberService.signUpNew(memberDTO);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{empNo}")
                    .buildAndExpand(memberDTO.getEmpNo()).toUri();
            return ResponseEntity.created(location).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<MemberDTO> getMember(String empNo) {
        try {
            return memberService.getMember(empNo);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Set<MemberDTO>> searchMember(Map<String, Object> searchParams) {
        try {
            return memberService.searchMember(searchParams);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String[]> getRelationShip(String rs) {
        return memberService.getRelationShip(rs);
    }
}
