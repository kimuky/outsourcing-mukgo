package com.example.outsourcing.advertisement.dto;

import com.example.outsourcing.entity.Advertisement;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApproveResponseDto extends  AdvertisementBaseDto{
    private final LocalDateTime contractDate;
    private final LocalDateTime updateDate;

    public ApproveResponseDto(Advertisement advertisement) {
        super(advertisement);
        this.contractDate = advertisement.getContractDate();
        this.updateDate = advertisement.getUpdatedAt();
    }
}
