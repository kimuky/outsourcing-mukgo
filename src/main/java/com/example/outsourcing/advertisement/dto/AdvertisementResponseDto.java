package com.example.outsourcing.advertisement.dto;

import com.example.outsourcing.entity.Advertisement;
import com.example.outsourcing.status.AdvertisementStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AdvertisementResponseDto {

    private final Long advertisementId;

    private final Long userId;

    private final Long storeId;

    private final Integer price;

    private final LocalDate contractDate;

    private final LocalDateTime requestDate;

    private final AdvertisementStatus status;

    public AdvertisementResponseDto(Advertisement savedAdvertisement) {
        this.advertisementId = savedAdvertisement.getId();
        this.userId = savedAdvertisement.getUser().getId();
        this.storeId = savedAdvertisement.getStore().getId();
        this.price = savedAdvertisement.getPrice();
        this.contractDate = savedAdvertisement.getContractDate();
        this.requestDate = savedAdvertisement.getCreatedAt();
        this.status = savedAdvertisement.getAdvertisementStatus();
    }
}
