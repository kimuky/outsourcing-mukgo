package com.example.outsourcing.store.dto;

import com.example.outsourcing.entity.Store;
import com.example.outsourcing.menu.dto.MenuResponseDto;
import com.example.outsourcing.status.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class StoreMenuResponseDto {

    private Long id;
    private String name;
    private Integer minimumAmount;
    private LocalTime openTime;
    private LocalTime closeTime;
    private StoreStatus status;
    private List<MenuResponseDto> menus;


    public static StoreMenuResponseDto fromStoreMenu(Store store, List<MenuResponseDto> menus) {
        return new StoreMenuResponseDto(
                store.getId(),
                store.getName(),
                store.getMinimumAmount(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getStatus(),
                menus
        );
    }
}
