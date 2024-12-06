package com.example.outsourcing.advertisement.dto;

import com.example.outsourcing.entity.Advertisement;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RejectResponseDto extends AdvertisementBaseDto {
    private final Integer contractMonth;
    private final String rejectComment;
    private final LocalDateTime rejectDate;

    public RejectResponseDto(Advertisement advertisement) {
        super(advertisement);
        this.contractMonth = advertisement.getContractMonth();
        this.rejectComment = advertisement.getRejectComment();
        this.rejectDate = advertisement.getUpdatedAt();
    }
}
