package com.example.outsourcing.admin.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class DailyStaticsResponseDto {

    private final Long orderCount;

    private final Integer totalPrice;

    private final LocalDate onDate;

}
