package com.yml.icas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ClaimDataDTO {
    private Integer id;
    private SchemeDTO scheme;
    private String ClaimDataStatus;
    private Double requestAmount;

    private Date rejectedDate;
    private String rejectRemarks;

    private Double deductionAmount;
    private String deductionRemarks;
    private Double paidAmount;
    private String remarks;
}
