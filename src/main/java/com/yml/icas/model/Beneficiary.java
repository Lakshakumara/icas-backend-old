package com.yml.icas.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "beneficiary")
public class Beneficiary implements Serializable {
    private static final long serialVersionId = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String nic;

    @OneToMany(mappedBy = "beneficiary", cascade = CascadeType.ALL)
    private Set<BeneficiaryData> beneficiaryData = new HashSet<>();
}
