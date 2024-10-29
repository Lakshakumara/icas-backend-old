package com.yml.icas.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "dependant")
public class Dependant implements Serializable {
    private static final long serialVersionId = 3L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    public Dependant(Integer id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "dependant", cascade = CascadeType.ALL)
    private Set<MemberDependantData> dependantData = new HashSet<>();

    private String name;

    private String nic;

    @Temporal(TemporalType.DATE)
    private Date dob;


}
