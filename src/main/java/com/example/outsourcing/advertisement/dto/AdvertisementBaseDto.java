package com.example.outsourcing.advertisement.dto;

import com.example.outsourcing.entity.Advertisement;
import com.example.outsourcing.status.AdvertisementStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public abstract class AdvertisementBaseDto {

    private final Long advertisementId;
    private final Long userId;
    private final Long storeId;
    private final Integer price;
    private final LocalDateTime requestDate;
    private final AdvertisementStatus status;

    public AdvertisementBaseDto (Advertisement advertisement) {
        this.advertisementId = advertisement.getId();
        this.userId = advertisement.getUser().getId();
        this.storeId = advertisement.getStore().getId();
        this.price = advertisement.getPrice();
        this.requestDate = advertisement.getCreatedAt();
        this.status = advertisement.getAdvertisementStatus();
    }
}
