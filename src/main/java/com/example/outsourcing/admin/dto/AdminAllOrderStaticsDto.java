package com.example.outsourcing.admin.dto;

import lombok.Getter;

@Getter
public class AdminAllOrderStaticsDto {

    private final Long count;

    private final Long totalPrice;

    public AdminAllOrderStaticsDto(Long count, Long totalPrice) {
        this.count = count;
        this.totalPrice = totalPrice;
    }
}
