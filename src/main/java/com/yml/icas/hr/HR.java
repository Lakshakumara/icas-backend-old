package com.yml.icas.hr;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Data
@Entity(name = "HR")
@Table(name = "hr")
public class HR {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "empNo", unique = true)
    private String empNo;
    private String name;
    private String address;
    private String email;
    private String contactNo;
    private String civilStatus;
    private String nic;
    private String sex;

    @Temporal(TemporalType.DATE)
    private Date dob;

    private String designation;
    private String department;
    private boolean deleted = false;

    @Temporal(TemporalType.DATE)
    private java.util.Date deletedDate;
}
