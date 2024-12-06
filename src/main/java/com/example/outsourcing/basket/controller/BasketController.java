package com.example.outsourcing.basket.controller;

import com.example.outsourcing.basket.dto.BasketItemDto;
import com.example.outsourcing.basket.dto.BasketResponseDto;
import com.example.outsourcing.basket.service.BasketService;
import com.example.outsourcing.entity.User;
import com.example.outsourcing.error.errorcode.ErrorCode;
import com.example.outsourcing.error.exception.CustomException;
import com.example.outsourcing.order.dto.OrderRequestDto;
import com.example.outsourcing.order.dto.OrderResponseDto;
import com.example.outsourcing.order.service.OrderService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class BasketController {

    private final BasketService basketService;

    /**
     * 장바구니 메뉴 추가
     * @param requestDto
     * @param basketCookie
     * @param userId
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/{userId}/baskets")
    public ResponseEntity<List<BasketItemDto>> addBasket(
            @RequestBody OrderRequestDto requestDto,
            @CookieValue(value = "basket", required = false)Cookie basketCookie,
            @PathVariable Long userId,
            HttpServletRequest request,
            HttpServletResponse response
            ) {

        HttpSession session = request.getSession(false);
        //login 되어있는 user data
        User loginUser = (User) session.getAttribute("user");

        // 로그인된 사용자와 userId가 일치하는지 확인
        if (loginUser == null || !loginUser.getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        List<BasketItemDto> basketItem = basketService.addBasket(requestDto, basketCookie, response);

//        BasketResponseDto basketResponseDto = BasketResponseDto.fromBasket(basketItem);

        return ResponseEntity.status(HttpStatus.OK).body(basketItem);
    }

    /**
     * 장바구니의 주문 처리
     * @param basketCookie
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/{userId}/baskets/purchase") //buy 수정
    public ResponseEntity<OrderResponseDto> basketOrder(
            @CookieValue(value = "basket", required = false) Cookie basketCookie,
            @PathVariable Long userId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        HttpSession session = request.getSession(false);
        //login 되어있는 user data
        User loginUser = (User) session.getAttribute("user");

        // 로그인된 사용자와 userId가 일치하는지 확인
        if (loginUser == null || !loginUser.getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION); // 접근 권한 없음
        }

        OrderResponseDto orderResponseDto = basketService.basketOrder(loginUser, basketCookie, response);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDto);
    }
}
