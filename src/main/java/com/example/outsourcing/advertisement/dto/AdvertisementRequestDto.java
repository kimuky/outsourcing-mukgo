package com.example.outsourcing.advertisement.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class AdvertisementRequestDto {

    private final Integer price;

    private final LocalDate contractDate;
}
