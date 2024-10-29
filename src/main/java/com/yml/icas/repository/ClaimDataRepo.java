package com.yml.icas.repository;

import com.yml.icas.model.ClaimData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

public interface ClaimDataRepo extends JpaRepository<ClaimData, Integer> {

}
