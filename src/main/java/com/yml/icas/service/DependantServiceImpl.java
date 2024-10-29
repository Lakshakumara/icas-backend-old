package com.yml.icas.service;

import com.yml.icas.dto.DependantDTO;
import com.yml.icas.dto.ObjectMapper;
import com.yml.icas.model.Dependant;
import com.yml.icas.repository.DependantRepo;
import com.yml.icas.service.interfaces.DependantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DependantServiceImpl implements DependantService {

    @Autowired
    DependantRepo dependantRepo;

    @Override
    public ResponseEntity<DependantDTO> getDependant(String depName) {
        log.info("inside get MEMBER of memberService class ");
        try {
            Dependant dependant = dependantRepo.findByName(depName);
            return new ResponseEntity<>(ObjectMapper.mapToDependant(dependant), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*@Override
    public Set<DependantDTO> getMemberDependant(int year, String empNo) {
        log.info("getMemberDependant {}", empNo);
        try {
            List<Dependant> dependant = dependantRepo.findAllByEmpNo(year, empNo);
            return ObjectMapper.mapToDependant(dependant);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }*/
}
