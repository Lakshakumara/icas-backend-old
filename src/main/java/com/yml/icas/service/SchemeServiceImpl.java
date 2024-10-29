package com.yml.icas.service;

import com.yml.icas.dto.ObjectMapper;
import com.yml.icas.dto.SchemeDTO;
import com.yml.icas.dto.SchemeTitleDTO;
import com.yml.icas.model.SchemeData;
import com.yml.icas.repository.SchemeDataRepo;
import com.yml.icas.service.interfaces.SchemeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SchemeServiceImpl implements SchemeService {
    @Autowired
    SchemeDataRepo schemeDataRepo;

    @Override
    public ResponseEntity<List<SchemeDTO>> getScheme() {
        try {
            List<SchemeDTO> schemeDTOS = schemeDataRepo
                    .findAll(Sort.by(Sort.Direction.ASC, "idText"))
                    .stream()
                    .map(ObjectMapper::mapToSchemeDTO)
                    .toList();
            return new ResponseEntity<>(schemeDTOS, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<SchemeDTO> addScheme(SchemeDTO schemeDTO) {
        try {
            return new ResponseEntity<>(ObjectMapper.mapToSchemeDTO(
                    schemeDataRepo.save(
                            ObjectMapper.mapToSchemeData(schemeDTO))), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<SchemeDTO> addAllScheme(Set<SchemeDTO> schemeDTO) {
        try {
            List<SchemeData> schemeData = schemeDataRepo.saveAll(schemeDTO.stream().map(ObjectMapper::mapToSchemeData).collect(Collectors.toSet()));
            return new ResponseEntity<>(ObjectMapper.mapToSchemeDTO(schemeData.get(0)), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<SchemeDTO> updateScheme(Integer id, SchemeDTO schemeDTO) {
        try {
            schemeDTO.setId(id);
            return new ResponseEntity<>(ObjectMapper.mapToSchemeDTO(
                    schemeDataRepo.save(
                            ObjectMapper.mapToSchemeData(schemeDTO))), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<SchemeDTO> deleteScheme(Integer id) {
        return null;
    }

    @Override
    public Set<SchemeTitleDTO> getSchemeTitles(String category) {
        Set<SchemeTitleDTO> schemeTitleDTOS = null;
        try {
            List<SchemeData> idText = schemeDataRepo
                    .getByCategory(category);
            schemeTitleDTOS = ObjectMapper.mapToSchemeTitleDTO(idText);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return schemeTitleDTOS;
    }
}
