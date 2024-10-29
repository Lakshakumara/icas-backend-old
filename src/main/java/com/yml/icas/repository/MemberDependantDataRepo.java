package com.yml.icas.repository;

import com.yml.icas.model.MemberDependantData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberDependantDataRepo extends JpaRepository<MemberDependantData, Integer> {


    @Query("SELECT m.relationship FROM MemberDependantData m WHERE m.relationship LIKE %:rs%")
    String[] findDistinctByRelationshipLike(@Param("rs") String rs);

    MemberDependantData findMemberDependantDataByAndDependant_Name(@Param("dependantName") String dependantName);

    List<MemberDependantData> findMemberDependantDataByMember_IdAndRegisterYear(
            @Param("memberId") Integer memberId, @Param("year") Integer year);
}
