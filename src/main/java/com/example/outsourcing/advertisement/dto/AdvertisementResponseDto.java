package com.example.outsourcing.advertisement.dto;

import com.example.outsourcing.entity.Advertisement;
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

    private Integer contractMonth;

    private final LocalDateTime requestDate;

    private final LocalDateTime contractDate;

    private final LocalDateTime updateDate;

    private final AdvertisementStatus status;

    public AdvertisementResponseDto(Advertisement savedAdvertisement) {
        this.advertisementId = savedAdvertisement.getId();
        this.userId = savedAdvertisement.getUser().getId();
        this.storeId = savedAdvertisement.getStore().getId();
        this.price = savedAdvertisement.getPrice();
        this.contractMonth = savedAdvertisement.getContractMonth();
        this.requestDate = savedAdvertisement.getCreatedAt();
        this.contractDate = savedAdvertisement.getContractDate();
        this.updateDate = savedAdvertisement.getUpdatedAt();
        this.status = savedAdvertisement.getAdvertisementStatus();
    }
}
