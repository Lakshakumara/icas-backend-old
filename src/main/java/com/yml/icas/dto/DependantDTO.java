package com.yml.icas.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DependantDTO {
    private Integer id;
    private String name;
    private String nic;
    private Date dob;
    private String relationship;
    private Integer registerYear;
    private boolean deleted;
}
