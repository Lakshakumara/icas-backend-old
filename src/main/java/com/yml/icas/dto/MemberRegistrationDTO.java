package com.yml.icas.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MemberRegistrationDTO {
    private Integer id;
    private Integer memberId;
    private Integer year;
    private String schemeType;
    private Date acceptedDate;
    private Integer acceptedBy;
}
