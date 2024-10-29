package com.yml.icas.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class SchemeTitleDTO {
    private String id;

    private List<String> idText = new ArrayList<>();

    private String description;

    public void addIdText(String idText) {
        this.idText.add(idText);
    }

    @Override
    public String toString() {
        return "\nSchemeTitleDTO{" +
                "id='" + id + '\'' +
                ", idText=" + idText +
                '}';
    }
}
