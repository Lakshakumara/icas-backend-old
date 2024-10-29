package com.yml.icas.service.interfaces;

import com.yml.icas.dto.DependantDTO;
import org.springframework.http.ResponseEntity;


public interface DependantService {
    ResponseEntity<DependantDTO> getDependant(String depName);

    //Set<DependantDTO> getMemberDependant(int year, String empNo);
}
