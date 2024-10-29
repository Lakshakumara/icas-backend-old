package com.yml.icas.control.admin;

import com.yml.icas.dto.SchemeDTO;
import com.yml.icas.dto.SchemeTitleDTO;
import com.yml.icas.service.SchemeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/admin/scheme")
public class SchemeRest {
    @Autowired
    SchemeServiceImpl schemeService;

    @PostMapping(path = "/add")
    ResponseEntity<SchemeDTO> addScheme(@RequestBody SchemeDTO schemeDTO) {
        log.info("inside addScheme");
        try {
            return schemeService.addScheme(schemeDTO);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/addAll")
    ResponseEntity<SchemeDTO> addScheme(@RequestBody Set<SchemeDTO> schemeDTOs) {
        log.info("inside addScheme");
        try {
            return schemeService.addAllScheme(schemeDTOs);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(path = "/{id}")
    ResponseEntity<SchemeDTO> updateScheme(@PathVariable("id") Integer id, @RequestBody SchemeDTO schemeDTO) {
        log.info("inside updateScheme");
        try {
            return schemeService.updateScheme(id, schemeDTO);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "")
    public ResponseEntity<List<SchemeDTO>> getScheme() {
        try {
            return schemeService.getScheme();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/titles/{category}")
    public ResponseEntity<Set<SchemeTitleDTO>> getSchemeTitles(@PathVariable("category") String category) {
        try {
            Set<SchemeTitleDTO> scheme = schemeService.getSchemeTitles(category);

            return new ResponseEntity<>(scheme, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}