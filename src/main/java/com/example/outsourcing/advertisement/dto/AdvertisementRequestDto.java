package com.example.outsourcing.advertisement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class AdvertisementRequestDto {

    @Min(value = 200000, message = "20만원 이상으로 입력해주세요")
    private final Integer price;

    @NotNull(message = "언제까지 광고를 할 것인지는 yyyy-mm-dd로 입력해주세요.")
    private final LocalDate contractDate;
}
