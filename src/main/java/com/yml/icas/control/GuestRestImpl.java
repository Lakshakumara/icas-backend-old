package com.yml.icas.control;

import com.yml.icas.service.GuestServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class GuestRestImpl implements GuestRest {
    @Autowired
    GuestServiceImpl guestService;

    @Override
    public ResponseEntity<Map<String, Object>> memberValidation(Integer year, String empNo) {
        System.out.println("GuestRestImpl received year: "+year+" EmpNo: "+empNo );
        return guestService.memberValidation(year, empNo);
    }
}
