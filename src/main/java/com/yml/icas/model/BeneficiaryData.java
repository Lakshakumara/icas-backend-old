package com.yml.icas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;

/*@NamedQuery(name = "BeneficiaryData.getBeneficiary", query = " from BeneficiaryData b")*/

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "beneficiary_data")

public class BeneficiaryData implements Serializable {
    private static final long serialVersionId = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Beneficiary beneficiary;

    @Column(name = "relasionship")
    private String relationship;

    @Column(name = "percent")
    private double percent;

    @Column(name = "registerdate")
    @Temporal(TemporalType.DATE)
    private Date registerDate;

    private boolean deleted = false;
}
