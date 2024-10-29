package com.yml.icas.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Entity(name = "claim_data")
public class ClaimData implements Serializable {
    private static final long serialVersionId = 5L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    private SchemeData schemeData;

    @ManyToOne
    private Claim claim;

    @Column(name = "claimdatastatus")
    private String ClaimDataStatus;

    @Column(name = "requestamount")
    private Double requestAmount;

    @Temporal(TemporalType.DATE)
    @Column(name = "rejecteddate")
    private Date rejectedDate;

    @Column(name = "rejectremarks")
    private String rejectRemarks;

    @Column(name = "deductionamount")
    private Double deductionAmount;

    @Column(name = "deductionremarks")
    private String deductionRemarks;

    @Column(name = "paidamount")
    private Double paidAmount;

    @Column(name = "remarks")
    private String remarks;


}