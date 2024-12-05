package com.example.outsourcing.admin.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class BetweenStaticsResponseDto {

    private final Long storeId;

    private final Long orderCount;

    private final Integer totalPrice;

    private final LocalDateTime startDate;

    private final LocalDateTime endDate;
}
