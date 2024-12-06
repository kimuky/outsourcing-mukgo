package com.example.outsourcing.menu.dto;

import com.example.outsourcing.entity.Menu;
import com.example.outsourcing.status.MenuStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class MenuResponseDto {

    private Long id;
    private String menuName;
    private Integer price;
    private MenuStatus status;

    public static MenuResponseDto fromMenu(Menu menu) {
        return new MenuResponseDto(
                menu.getId(),
                menu.getMenuName(),
                menu.getPrice(),
                menu.getStatus()
        );
    }
}
