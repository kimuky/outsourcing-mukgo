package com.example.outsourcing.menu.controller;


import com.example.outsourcing.entity.User;
import com.example.outsourcing.menu.dto.MenuRequestDto;
import com.example.outsourcing.menu.dto.MenuResponseDto;
import com.example.outsourcing.menu.service.MenuService;
import com.example.outsourcing.store.dto.StoreRequestDto;
import com.example.outsourcing.store.dto.StoreResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stores/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<MenuResponseDto> createMenu(
            @RequestBody MenuRequestDto requestDto,
            @RequestParam Long storeId,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        //login 되어있는 user data
        User loginUser = (User) session.getAttribute("user");

        MenuResponseDto responseDto = menuService.createMenu(storeId, loginUser.getId(),requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }
}
