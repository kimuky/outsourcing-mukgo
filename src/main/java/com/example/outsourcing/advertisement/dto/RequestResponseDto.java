package com.example.outsourcing.advertisement.dto;

import com.example.outsourcing.entity.Advertisement;
import lombok.Getter;

@Getter
public class RequestResponseDto extends AdvertisementBaseDto {
    private final Integer contractMonth;

    public RequestResponseDto(Advertisement advertisement) {
        super(advertisement);
        this.contractMonth = advertisement.getContractMonth();
    }
}
