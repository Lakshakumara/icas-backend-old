package com.yml.icas.repository;

import com.yml.icas.model.Member;
import com.yml.icas.model.MemberRegistration;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface MemberRegistrationRepo extends JpaRepository<MemberRegistration, Integer> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE member_registration set acceptedby=:acceptedBy, accepteddate=:accepteddate " +
            "where member_id=:memberId and acceptedby is null", nativeQuery = true)
    Integer acceptRegistration(@Param("acceptedBy") Integer acceptedBy,
                               @Param("accepteddate") LocalDate accepteddate, @Param("memberId") Integer memberId);


    MemberRegistration findByYearAndMember(@Param("year") Integer year, @Param("member") Member member);

    Set<MemberRegistration> findByMember(@Param("member") Member member);

    List<MemberRegistration> findByAcceptedDate(@Param("acceptDate") Date acceptDate);

    List<MemberRegistration> findByAcceptedDateNull();


    Set<MemberRegistration> findByAcceptedDateNullAndYear(@Param("year") Integer year);
/*
    String queryReportListAndCategoryName = "SELECT * FROM answer INNER JOIN question on answer.question_id = question.id WHERE answer.question_id = :id ORDER BY answer.creation_date DESC";

    @Query(value = queryReportListAndCategoryName, nativeQuery = true)*/
    // Member findMemberRegistrationByYearAndMember_EmpNo(Integer year, String empNo);
}
