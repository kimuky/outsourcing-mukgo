package com.example.outsourcing.advertisement.dto;

import com.example.outsourcing.status.AdvertisementStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdvertisementResponseDto {

    private final Long advertisementId;

    private final Long userId;

    private final Long storeId;

    private final Integer price;

    private final String rejectComment;

    private final Integer contractMonth;

    private final LocalDateTime requestDate;

    private final LocalDateTime contractDate;

    private final LocalDateTime updateDate;

    private final AdvertisementStatus status;
}
