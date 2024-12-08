package com.example.outsourcing.menu.controller;


import com.example.outsourcing.entity.User;
import com.example.outsourcing.menu.dto.MenuRequestDto;
import com.example.outsourcing.menu.dto.MenuResponseDto;
import com.example.outsourcing.menu.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    /**
     * 메뉴 생성
     * @param requestDto
     * @param storeId
     * @param request
     * @return
     */
    @PostMapping("/menus")
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

    /**
     * 메뉴 수정
     * @param requestDto
     * @param storeId
     * @param menuId
     * @param request
     * @return
     */
    @PatchMapping("/{storeId}/menus/{menuId}")
    public ResponseEntity<MenuResponseDto> updateStore(
            @RequestBody MenuRequestDto requestDto,
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        //login 되어있는 user data
        User loginUser = (User) session.getAttribute("user");

        MenuResponseDto updateMenu = menuService.updateMenu(
                storeId,
                menuId,
                loginUser.getId(),
                requestDto.getMenuName(),
                requestDto.getPrice());

        return ResponseEntity.ok().body(updateMenu);
    }


    /**
     * 메뉴 삭제
     * @param storeId
     * @param menuId
     * @param request
     * @return
     */
    @PatchMapping("/{storeId}/menus/{menuId}/delete")
    public ResponseEntity<Void> deleteMenu(
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        //login 되어있는 user data
        User loginUser = (User) session.getAttribute("user");

        menuService.deleteMenu(storeId, menuId, loginUser.getId());

        return ResponseEntity.ok().build();
    }



}
