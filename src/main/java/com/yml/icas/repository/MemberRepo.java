package com.yml.icas.repository;

import com.yml.icas.model.Member;
import com.yml.icas.model.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface MemberRepo extends JpaRepository<Member, Integer> {

    List<Member> findAllByEmpNoContainsIgnoreCase(String empNo);

    List<Member> findAllByNameContainsIgnoreCase(String name);

    @Query("Select m.id from Member m where lower(m.empNo) = lower(:empNo)")
    Integer getMemberId(@Param("empNo") String empNo);

    @Query("Select m from Member m where lower(m.empNo) = lower(:empNo)")
    Member getMember(@Param("empNo") String empNo);


    Member findByEmpNoIgnoreCase(@Param("empNo") String empNo);


    @Query("Select m from Member m, MemberRegistration mr " +
            " where m.id = mr.member.id and mr.year= :regYear")
    Set<Member> findByRegYear(@Param("regYear") Integer regYear);

    @Query("Select m from Member m where m.status = :status")
    Set<Member> findAllByStatusIgnoreCase(@Param("status") String status);

    @Query(value = "SELECT * FROM Users ORDER BY id",
            countQuery = "SELECT count(*) FROM Users",
            nativeQuery = true)
    Page<Member> findAllMembersWithPagination(Pageable pageable);

    @Query(value = "UPDATE Member set registrationOpen =:regYear where deleted = false" +
            " and registrationOpen != :regYear ",
            nativeQuery = true)
    Integer updateRegistrationAll(Integer regYear);

    @Query(value = "UPDATE Member set registrationOpen =:regYear where deleted = false" +
            " and empNo = :empNo ",
            nativeQuery = true)
    Integer updateRegistrationMember(String empNo, Integer regYear);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Member set roles =:roles where deleted = false" +
            " and id = :memberId ")
    Integer updateRoles(@Param(value = "memberId") Integer memberId, @Param(value = "roles") Set<Role> roles);

}
