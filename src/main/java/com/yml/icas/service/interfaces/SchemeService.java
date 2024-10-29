package com.yml.icas.service.interfaces;

import com.yml.icas.dto.SchemeDTO;
import com.yml.icas.dto.SchemeTitleDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface SchemeService {
    ResponseEntity<List<SchemeDTO>> getScheme();

    ResponseEntity<SchemeDTO> updateScheme(Integer id, SchemeDTO schemeDTO);

    ResponseEntity<SchemeDTO> addScheme(SchemeDTO schemaDTO);

    ResponseEntity<SchemeDTO> addAllScheme(Set<SchemeDTO> schemeDTO);

    ResponseEntity<SchemeDTO> deleteScheme(Integer id);

    Set<SchemeTitleDTO> getSchemeTitles(String category);
}
