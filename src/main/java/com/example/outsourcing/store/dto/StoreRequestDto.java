package com.example.outsourcing.store.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class StoreRequestDto {

    @NotNull(message = "가게 이름은 필수 입력값입니다.")
    private String name;

    @NotNull(message = "최소주문 금액은 필수 입력값입니다.")
    private Integer minimumAmount;

    @NotNull(message = "오픈 시간은 필수 입력값입니다.")
    private LocalTime openTime;

    @NotNull(message = "마감 시간은 필수 입력값입니다.")
    private LocalTime closeTime;
}
