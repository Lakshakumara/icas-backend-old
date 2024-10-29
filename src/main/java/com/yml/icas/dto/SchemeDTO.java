package com.yml.icas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SchemeDTO {
    private Integer id;

    private String idText;

    private String title;

    private String descriptionen;

    private String descriptionsi;

    private String descriptionta;

    private double maxAmount;

    private String unit;

    private double rate;

    private String category;

    @Override
    public String toString() {
        return "SchemeDTO{" +
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
