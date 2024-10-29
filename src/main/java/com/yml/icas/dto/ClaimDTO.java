package com.yml.icas.dto;

import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class ClaimDTO {
    private Integer id;
    private Integer memberId;
    private String empNo;
    private String name;
    private MemberDTO member;
    private DependantDTO dependant;
    private Set<ClaimDataDTO> claimData = new HashSet<>();
    private String category;
    private String requestFor;

    private Date injuryDate;
    private String injuryPlace;
    private String injuryHow;
    private String injuryNature;

    private Date illnessDate;
    private String illnessNature;
    private Date illnessFirstConsultDate;
    private String illnessFirstDr;

    private Date hospitalStartDate;
    private Date hospitalEndDate;

    private String infoTreatment;
    private String infoHospital;
    private String infoConsultant;

    private Date claimDate;
    private Date startDate;
    private Double requestAmount;

    private Date acceptedDate;
    private Integer acceptedBy;

    private Date mecSendDate;
    private Double deductionAmount;
    private Double paidAmount;
    private Long voucherId;

    private Date financeSendDate;
    private Date completedDate;

    private String mecRemarks;
    private Date mecReturnDate;
    private Date rejectedDate;
    private String rejectRemarks;

    private String claimStatus;
    private String remarks;

    private boolean appeal;
    private Integer appealRefId;
    private String appealRemarks;

}
