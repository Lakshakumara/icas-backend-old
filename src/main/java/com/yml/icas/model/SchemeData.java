package com.yml.icas.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "schemeplan", uniqueConstraints = {
        @UniqueConstraint(name = "schemePlan_IdText_unique", columnNames = "idtext")
})
public class SchemeData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    public SchemeData(Integer id) {
        this.id = id;
    }

    @Column(name = "idtext")
    private String idText;

    private String title;
    @Column(name = "descriptionen", length = 1000)

    private String descriptionen;
    @Column(name = "descriptionsi", length = 1000)

    private String descriptionsi;
    @Column(name = "descriptionta", length = 1000)
    private String descriptionta;

    @Column(name = "maxamount")
    private double maxAmount;
    @Column(name = "unit", length = 50)
    private String unit;

    private double rate;

    private String category;

    @Override
    public String toString() {
        return "SchemeData{" +
                "id=" + id +
                ", idText='" + idText + '\'' +
                ", title='" + title + '\'' +
                ", descriptionen='" + descriptionen + '\'' +
                ", descriptionsi='" + descriptionsi + '\'' +
                ", descriptionta='" + descriptionta + '\'' +
                ", maxAmount=" + maxAmount +
                ", unit='" + unit + '\'' +
                ", rate=" + rate +
                ", category='" + category + '\'' +
                '}';
    }
}
