package com.yml.icas.control;

import com.yml.icas.dto.DependantDTO;
import com.yml.icas.service.interfaces.DependantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class DependantRestImpl implements DependantRest {
    @Autowired
    DependantService dependantService;

    @Override
    public ResponseEntity<DependantDTO> getDependant(String depName) {
        try {
            return dependantService.getDependant(depName);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
