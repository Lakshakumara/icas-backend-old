package com.yml.icas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NamedQuery(name = "MemberDependantData.getAllMembersData", query = " from MemberDependantData m ")

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dependant_data")
public class MemberDependantData implements Serializable {
    private static final long serialVersionId = 5L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    private Dependant dependant;

    @Column(name = "relasionship")
    private String relationship;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")

    @Column(name = "registeryear")
    private Integer registerYear;

    private boolean deleted = false;

}
