package com.yml.icas.control;

import com.yml.icas.dto.DependantDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin(origins = "*")
@RequestMapping(path = "/dependant")
public interface DependantRest {
    @GetMapping(path = "/{depName}")
    ResponseEntity<DependantDTO> getDependant(@PathVariable(name = "depName") String depName);

    /*@GetMapping(path = "/{year}/{empNo}")
    ResponseEntity<Set<DependantDTO>> getMemberDependant(@PathVariable(name = "year") int year,
                                                         @PathVariable(name = "empNo") String empNo);*/
}