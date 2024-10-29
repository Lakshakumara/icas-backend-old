package com.yml.icas.service;

import com.yml.icas.dto.MemberDTO;
import com.yml.icas.dto.ObjectMapper;
import com.yml.icas.model.Member;
import com.yml.icas.repository.MemberRegistrationRepo;
import com.yml.icas.repository.MemberRepo;
import com.yml.icas.service.interfaces.GuestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class GuestServiceImpl implements GuestService {

    @Autowired
    MemberRepo memberRepo;

    @Autowired
    MemberRegistrationRepo memberRegistrationRepo;

    @Override
    public ResponseEntity<Map<String, Object>> memberValidation(Integer year, String empNo) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus statusGest;
        try {
            Member member = memberRepo.findByEmpNoIgnoreCase(empNo);
            MemberDTO memberDTO = ObjectMapper.mapToMemberDTO(member);

            if (Objects.isNull(memberDTO)) {
                response.put("isMember", false);
            } else {
                response.put("isMember", true);
                response.put("member", memberDTO);
 /*
                Set<MemberRegistration> mr = memberRegistrationRepo.findByMember(new Member(member.getId()));
                log.info("guest MemberRegistration {}", mr);
                Set<Map<String, Object>> aa = mr.stream().map(data -> {
                    data.setMember(null);
                    Map<String, Object> i = new HashMap<>();
                    i.put("id", data.getId());
                    i.put("year", data.getYear());
                    return i;
                }).collect(Collectors.toSet());
                response.put("registration", aa);*/
            }
            statusGest = HttpStatus.OK;
        } catch (Exception ex) {
            System.out.println("Error: "+ex.toString() );
            ex.printStackTrace();
            statusGest = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(response, statusGest);
    }
}
