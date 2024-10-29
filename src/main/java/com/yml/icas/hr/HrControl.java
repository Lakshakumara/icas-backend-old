package com.yml.icas.hr;

import com.yml.icas.repository.SchemeDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RequestMapping(path = "/hr")
@RestController
public class HrControl {

    @Autowired
    HrService hrService;

    @Autowired
    SchemeDataRepo schemeDataRepo;


    @GetMapping("/{empNo}")
    public ResponseEntity<HR> getEmployee(@PathVariable("empNo") String empNo) {
        try {
            return hrService.getEmployee(empNo);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}