package com.yml.icas.hr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class HrService {

    @Autowired
    HRDao hrDao;

    public ResponseEntity<HR> getEmployee(String empNo) {
        return new ResponseEntity<>(hrDao.findByEmpNo(empNo), HttpStatus.OK);
    }
}
