package com.example.outsourcing.admin.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class StartEndDateDto {

    private final LocalDate startDate;

    private final LocalDate endDate;

    public StartEndDateDto(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
