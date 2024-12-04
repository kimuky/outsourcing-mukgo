package com.example.outsourcing.store.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class StoreRequestDto {

    @NotBlank(message = "가게 이름은 필수 입력값입니다.")
    private String name;

    @NotBlank(message = "최소주문 금액은 필수 입력값입니다.")
    private Integer minimumAmount;

    @NotBlank(message = "오픈 시간은 필수 입력값입니다.")
    private LocalTime openTime;

    @NotBlank(message = "마감 시간은 필수 입력값입니다.")
    private LocalTime closeTime;
}
