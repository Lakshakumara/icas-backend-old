package com.yml.icas.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ClaimOPDDTO {
    private Integer id;
    private Integer memberId;
    private MemberDTO member;
    /**
     * OPD or SHE(Surgical &Hospital Expenses)
     */
    private String category;
    /**
     * Outdoor, Spectacles, covid test etc..
     */
    private String requestFor;

    private Date startDate;
    private Date endDate;
    private Date claimDate;
    private Date applyDate;
    private Date acceptedDate;
    private Integer acceptedBy;

    private Double requestAmount;
    private Double deductionAmount;
    private Double paidAmount;
    private String claimStatus;
}
