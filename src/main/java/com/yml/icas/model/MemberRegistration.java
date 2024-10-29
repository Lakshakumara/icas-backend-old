package com.yml.icas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member_registration")
public class MemberRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Member member;

    private Integer year;

    @Column(name = "schemetype")
    private String schemeType;

    @Temporal(TemporalType.DATE)
    @Column(name = "accepteddate")
    private Date acceptedDate;

    @Column(name = "acceptedby")
    private Integer acceptedBy;
}
