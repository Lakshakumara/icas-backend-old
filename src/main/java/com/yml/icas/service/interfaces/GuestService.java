package com.yml.icas.service.interfaces;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface GuestService {

    ResponseEntity<Map<String, Object>> memberValidation(Integer year, String empNo);
}
