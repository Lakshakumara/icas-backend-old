package com.yml.icas.control;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

//@CrossOrigin(origins = {"http://192.168.43.181:4200", "http://localhost:4200"}))
@CrossOrigin(origins = "*")
@RequestMapping(path = "/guest")
public interface GuestRest {

    @GetMapping(path = "/{year}/{empNo}")
    ResponseEntity<Map<String, Object>> memberValidation(@PathVariable(name = "year") Integer year,
                                                         @PathVariable(name = "empNo") String empNo);
}
