package com.example.outsourcing.admin.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StartEndDateTimeDto {

    private final LocalDateTime startDate;

    private final LocalDateTime endDate;

    public StartEndDateTimeDto(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
