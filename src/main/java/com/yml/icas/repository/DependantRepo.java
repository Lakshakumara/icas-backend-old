package com.yml.icas.repository;

import com.yml.icas.model.Dependant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface DependantRepo extends JpaRepository<Dependant, Integer> {
    Dependant findByName(@Param("name") String name);
    //@Query("select d. from MemberDependantData md, Dependant d where md.registerYear=:year and md.member.empNo=:empNo")
    // List<Dependant> findAllByEmpNo(@Param("year") int year,
    //                               @Param("empNo") String empNo);
}
