package com.example.outsourcing.menu.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MenuRequestDto {

    @NotBlank(message = "메뉴의 이름은 필수 입니다.")
    private String menuName;

    @NotBlank(message = "메뉴의 가격은 필수 입니다.")
    private Integer price;


}
