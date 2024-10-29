package com.yml.icas.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Setter
@Getter
public class BeneficiaryDTO {
    private Integer id;
    private String name;
    private String nic;
    private String relationship;
    private double percent;
    private Date registerDate;
    private boolean deleted = false;
}
