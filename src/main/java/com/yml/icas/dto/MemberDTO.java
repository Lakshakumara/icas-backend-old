package com.yml.icas.dto;

import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class MemberDTO {
    private Integer id;
    private String empNo;
    private String name;
    private String address;
    private String email;
    private String contactNo;
    private String civilStatus;
    private String nic;
    private String sex;
    private Date dob;
    private String designation;
    private String department;
    private String password;
    private Date mDate;
    private String status;
    private Integer registrationOpen;
    private Set<RoleDTO> roles = new HashSet<>();
    private boolean deleted;

    private MemberRegistrationDTO currentRegistration;
    private Set<MemberRegistrationDTO> memberRegistrations = new HashSet<>();
    private Set<DependantDTO> dependants = new HashSet<>();
    private Set<BeneficiaryDTO> beneficiaries = new HashSet<>();
}
