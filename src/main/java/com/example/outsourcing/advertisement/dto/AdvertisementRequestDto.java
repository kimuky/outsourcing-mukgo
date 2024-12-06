package com.example.outsourcing.advertisement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdvertisementRequestDto {

    @Min(value = 200000, message = "20만원 이상으로 입력해주세요")
    private final Integer price;

    @Positive
    private final Integer month;
}
