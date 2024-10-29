package com.yml.icas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {
    private String category;
    private Long requestSum;
    private Long paidSum;
}
