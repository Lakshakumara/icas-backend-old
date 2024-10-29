package com.yml.icas.repository;

import com.yml.icas.model.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiaryRepo extends JpaRepository<Beneficiary, Integer> {
}
