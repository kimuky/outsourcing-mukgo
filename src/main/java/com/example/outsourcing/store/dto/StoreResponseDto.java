package com.example.outsourcing.store.dto;

import com.example.outsourcing.entity.Store;
import com.example.outsourcing.status.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class StoreResponseDto {

    private Long id;
    private String name;
    private Integer minimumAmount;
    private LocalTime openTime;
    private LocalTime closeTime;
    private StoreStatus status;


    public static StoreResponseDto fromStore(Store store) {
        return new StoreResponseDto(
                store.getId(),
                store.getName(),
                store.getMinimumAmount(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getStatus()
        );
    }
}
