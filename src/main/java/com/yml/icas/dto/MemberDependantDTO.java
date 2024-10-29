package com.yml.icas.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class MemberDependantDTO {
    private Integer id;
    private DependantDTO dependants;
    private String relationship;
    private Date registerDate;

    private boolean deleted = false;

}
