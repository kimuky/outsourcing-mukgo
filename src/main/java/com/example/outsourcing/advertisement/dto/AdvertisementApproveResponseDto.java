package com.example.outsourcing.advertisement.dto;

import com.example.outsourcing.entity.Advertisement;
import com.example.outsourcing.status.AdvertisementStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdvertisementApproveResponseDto {
    private final Long advertisementId;

    private final Long userId;

    private final Long storeId;

    private final Integer price;

    private final LocalDateTime contractDate;

    private final LocalDateTime requestDate;

    private final LocalDateTime updateDate;

    private final AdvertisementStatus status;

    public AdvertisementApproveResponseDto (Advertisement advertisement) {
        this.advertisementId = advertisement.getId();
        this.userId = advertisement.getUser().getId();
        this.storeId = advertisement.getStore().getId();
        this.price = advertisement.getPrice();
        this.contractDate = advertisement.getContractDate();
        this.requestDate = advertisement.getCreatedAt();
        this.updateDate = advertisement.getUpdatedAt();
        this.status = advertisement.getAdvertisementStatus();
    }
}
