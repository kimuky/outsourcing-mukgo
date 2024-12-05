package com.example.outsourcing.admin.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class MonthlyStaticsResponseDto {

    private final Long storeId;

    private final Long orderCount;

    private final Integer totalPrice;

    private final LocalDate startDate;

    private final LocalDate endDate;

}
