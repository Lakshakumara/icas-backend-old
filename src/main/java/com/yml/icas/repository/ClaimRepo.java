package com.yml.icas.repository;

import com.yml.icas.model.Claim;
import com.yml.icas.model.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ClaimRepo extends JpaRepository<Claim, Integer> {

    List<Claim> findAllByMember(Member member);

    Claim findClaimById(Integer integer);

    List<Claim> findAllByVoucherId(long voucherId);

    @Query(value = "Select c from Claim c where c.member =:member order by c.claimDate ")
    List<Claim> getClaimData(Member member);

    @Query(value = "Select c from Claim c where  " +
            " c.member =:member and lower(c.claimStatus) like lower(:claimStatus) order by c.claimDate ")
    List<Claim> getClaimData(Member member, String claimStatus);

    @Query(value = "Select c from Claim c where c.member =:member and YEAR(c.claimDate)=:year order by c.claimDate ")
    List<Claim> getClaimData(Member member, Integer year);

    @Query(value = "Select c from Claim c where c.member =:member and YEAR(c.claimDate)=:year " +
            "and lower(c.claimStatus) like lower(:claimStatus) order by c.claimDate ")
    List<Claim> getClaimData(Member member, Integer year,  String claimStatus);

    @Query(value = "Select c from Claim c where lower(c.category) like (lower(:claimType)) and " +
            " YEAR(c.claimDate)=:year and c.member =:member " +
            "and lower(c.claimStatus) like lower(:claimStatus) order by c.claimDate ")
    List<Claim> getClaimData(Member member, String claimType, Integer year,  String claimStatus);

    @Query(value = "select c from Claim c where c.member =:member and YEAR(c.claimDate)=:year")
    List<Claim> getDashboardData(Member member, Integer year);

    /**
     * @param claimType
     * @param claimStatus
     * @return
     */
    /*@Query(value = "Select c from Claim c where lower(c.category) = lower(:claimType) " +
            "and lower(c.claimStatus) = lower(:claimStatus)")*/
    @Query(value = "Select * from Claim c where lower(c.category) like (lower(:claimType))  " +
            "and lower(c.claimStatus) like lower(:claimStatus)", nativeQuery = true)
    List<Claim> getClaims(String claimType, String claimStatus);

    List<Claim> findAllByClaimStatusLike(String claimStatus);

    @Modifying
    @Transactional
    @Query("update Claim c set c.claimStatus=:claimStatus, c.rejectRemarks=:rejectremarks," +
            "c.rejectedDate=:rejecteddate where c.id=:id")
    int claimReject(@Param(value = "id") Integer id,
                         @Param(value = "claimStatus") String claimStatus,
                         @Param(value = "rejectremarks") String rejectremarks,
                         @Param(value = "rejecteddate") Date rejecteddate);

    @Modifying
    @Transactional
    @Query("update Claim c set c.claimStatus=:claimStatus, c.acceptedBy=:acceptBy, c.acceptedDate=:acceptedDate " +
            " where c.id=:id")
    int updateHeadAccept(@Param(value = "id") long id,
                         @Param(value = "claimStatus") String claimStatus,
                         @Param(value = "acceptBy") long acceptBy,
                         @Param(value = "acceptedDate") Date acceptedDate);

    @Modifying
    @Transactional
    @Query("update Claim c set c.claimStatus=:claimStatus," +
            " c.mecSendDate=:mecSendDate " +
            " where c.id=:id")
    int forwardMEC(@Param(value = "id") long id,
                   @Param(value = "claimStatus") String claimStatus,
                   @Param(value = "mecSendDate") Date mecSendDate);

    @Modifying
    @Transactional
    @Query("update Claim c set c.claimStatus=:claimStatus, c.financeSendDate=:financeSendDate, " +
            "c.paidAmount=:paidAmount, c.voucherId=:voucherId " +
            " where c.id=:id")
    int forwardFinance(@Param(value = "id") long id,
                       @Param(value = "claimStatus") String claimStatus,
                       @Param(value = "financeSendDate") Date financeSendDate,
                       @Param(value = "paidAmount") Double paidAmount,
                       @Param(value = "voucherId") Long voucherId);
    @Modifying
    @Transactional
    @Query("update Claim c set c.claimStatus=:claimStatus, " +
            "c.completedDate=:completeddate where c.id=:id")
    int forwardPaid(@Param(value = "id") long id,
                       @Param(value = "claimStatus") String claimStatus,
                       @Param(value = "completeddate") Date completeddate);
    @Modifying
    @Transactional
    @Query("update Claim c set c.claimStatus=:claimStatus," +
            " c.deductionAmount=:deductionAmount," +
            " c.mecRemarks=:mecremarks," +
            " c.mecReturnDate=:mecreturndate," +
            " c.rejectedDate=:rejecteddate," +
            " c.rejectRemarks=:rejectremarks" +
            " where c.id=:id")
    int opdComplete(@Param(value = "id") long id,
                    @Param(value = "claimStatus") String claimStatus,
                    @Param(value = "deductionAmount") Double deductionAmount,
                    @Param(value = "mecremarks") String mecremarks,
                    @Param(value = "mecreturndate") Date mecreturndate,
                    @Param(value = "rejecteddate") Date rejecteddate,
                    @Param(value = "rejectremarks") String rejectremarks);
    @Modifying
    @Transactional
    @Query("update Claim c set c.claimStatus=:claimStatus" +
            " where c.id=:id")
    int mecApproval(@Param(value = "id") long id,
                    @Param(value = "claimStatus") String claimStatus);

@Query("select distinct c.voucherId from Claim c where c.voucherId != null")
    long[] getVoucherId();

    @Modifying
    @Transactional
    @Query("update Claim c set c.deductionAmount=:deductionAmount, c.paidAmount=:paidAmount" +
            " where c.id=:id")
    int finalize(@Param(value = "id") long id,
                 @Param(value = "deductionAmount") double deductionAmount,
                 @Param(value = "paidAmount") double paidAmount);
}
