package com.yml.icas.hr;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HRDao extends JpaRepository<HR, Integer> {

    HR findByEmpNo(String empNo);

}
