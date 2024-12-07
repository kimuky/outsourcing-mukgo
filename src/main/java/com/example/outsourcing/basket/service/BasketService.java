package com.example.outsourcing.basket.service;

import com.example.outsourcing.basket.dto.BasketItemDto;
import com.example.outsourcing.entity.*;
import com.example.outsourcing.error.errorcode.ErrorCode;
import com.example.outsourcing.error.exception.CustomException;
import com.example.outsourcing.menu.repository.MenuRepository;
import com.example.outsourcing.order.dto.OrderDto;
import com.example.outsourcing.order.dto.OrderRequestDto;
import com.example.outsourcing.order.dto.OrderResponseDto;
import com.example.outsourcing.order.repository.OrderMenuRepository;
import com.example.outsourcing.order.repository.OrderRepository;
import com.example.outsourcing.status.OrderStep;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BasketService {

    private OrderRepository orderRepository;
    private OrderMenuRepository orderMenuRepository;
    private MenuRepository menuRepository;
    private final ObjectMapper objectMapper;


    private static final long COOKIE_MAX_AGE = 24 * 60 * 60; // 1일


    /**
     * 장바구니 메뉴 추가
     * @param requestDto
     * @param basketCookie
     * @param response
     * @return
     */
    public List<BasketItemDto> addBasket(OrderRequestDto requestDto, Cookie basketCookie, HttpServletResponse response) {

        List<BasketItemDto> basket = parseBasketFromCookie(basketCookie);

        //메뉴 가져오기
        Menu menu = menuRepository.findByMenuOrElseThrow(requestDto.getMenuId());

        //한 가게의 메뉴만 허용
        if(!basket.isEmpty() && !basket.get(0).getStoreId().equals(menu.getStore().getId())) {
            // 다른 가게 메뉴가 담긴 경우 초기화
            basket.clear();
        }

        // 같은 메뉴가 있는지 확인
        boolean itemExists = false;
        for (BasketItemDto item : basket) {
            if (item.getMenuId().equals(menu.getId())) {
                // 같은 메뉴가 있을 경우 수량 증가
                item.setCount(item.getCount() + requestDto.getCount());
                itemExists = true;
                break;
            }
        }

        // 같은 메뉴가 없을 경우 새 항목 추가
        if (!itemExists) {
            BasketItemDto basketItem = new BasketItemDto(
                    menu.getId(),
                    menu.getStore().getId(),
                    requestDto.getCount()
            );
            basket.add(basketItem);
        }

        //쿠키에 저장
        saveBasketToCookie(basket, response);

        return basket;
    }


    /**
     * 장바구니 주문 처리
     * @param loginUser
     * @param basketCookie
     * @param response
     * @return
     */
    @Transactional
    public OrderResponseDto basketOrder(User loginUser, Cookie basketCookie, HttpServletResponse response) {

        List<BasketItemDto> basket = parseBasketFromCookie(basketCookie);
        List<Store> stores = new ArrayList<>();
        LocalTime now = LocalTime.now();

        if (basket.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_BASKET);
        }

        // 주문 생성
        Orders order = new Orders(loginUser,OrderStep.ORDER_COMPLETED);
        Orders savedOrder = orderRepository.save(order);

        for (BasketItemDto basketItem : basket) {
            Menu menu = menuRepository.findByMenuOrElseThrow(basketItem.getMenuId());

            Store store = menu.getStore();
            stores.add(store);

            // 오픈시간이 아닐때 예외
            if (now.isBefore(store.getOpenTime()) || now.isAfter(store.getCloseTime())) {
                throw new CustomException(ErrorCode.NOT_OPEN_TIME);
            }

            orderMenuRepository.insertOrderMenu(savedOrder.getId(), menu.getId(), basketItem.getCount());

        }
        List<OrderDto> ordersList = orderMenuRepository.getOrderMenus(savedOrder.getId());
        Integer totalPrice = ((BigDecimal) orderMenuRepository.findTotalPrice(savedOrder.getId())).intValue();


        // 총 가격 업데이트
        orderRepository.updateTotalPrice(totalPrice, savedOrder.getId());
        orderRepository.updateStore(stores.get(0), savedOrder.getId());
        // 장바구니 비우기
        clearBasketCookie(response);

        return OrderResponseDto.builder()
                .id(savedOrder.getId())
                .storeId(basket.get(0).getStoreId())
                .order(ordersList)
                .totalPrice(totalPrice)
                .createdAt(savedOrder.getCreatedAt())
                .build();

    }

    /**
     * 사용자 현재 사용 중인 장바구니 조회
     * @param basketCookie
     * @return
     */
    public List<BasketItemDto> findByBasket(Cookie basketCookie) {

        List<BasketItemDto> basket = parseBasketFromCookie(basketCookie);

        return basket;
    }






    /**
     * 쿠키에서 장바구니 데이터를 읽어와 객체로 변환(역직렬화)
     * @param basketCookie
     * @return
     */
    private List<BasketItemDto> parseBasketFromCookie(Cookie basketCookie) {
        //쿠키가 없으면 빈 장바구니로 반환
        if (basketCookie == null) {
            return new ArrayList<>();
        }
        try {
            // URL 디코딩
            String decodedBasket = URLDecoder.decode(basketCookie.getValue(), "UTF-8");

            //JSON 문자열인 쿠키 값을 List<BasketItemDto>로 역직렬화, //제네릭 타입을 처리하기 위한 도구.
            return objectMapper.readValue(decodedBasket, new TypeReference<List<BasketItemDto>>() {});

        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }

    /**
     * 장바구니 데이터를 JSON으로 직렬화하여 쿠키에 저장
     * @param basket
     * @param response
     */
    private void saveBasketToCookie(List<BasketItemDto> basket, HttpServletResponse response) {
        try {
            //List<BasketItemDto> 객체를 JSON 문자열로 직렬화
            String basketJson = objectMapper.writeValueAsString(basket);

            // URL 인코딩
            String encodedBasket = URLEncoder.encode(basketJson, "UTF-8");

            //직렬화된 JSON 데이터를 쿠키에 저장
            Cookie cookie = new Cookie("basket", encodedBasket);
            cookie.setMaxAge((int) COOKIE_MAX_AGE); // 쿠키 만료 시간 설정 (24시간)
            cookie.setHttpOnly(true); // JavaScript 접근 불가 설정
            cookie.setPath("/"); // 모든 경로에서 쿠키 접근 가능
            response.addCookie(cookie); // HTTP 응답에 쿠키 추가
        }
        catch (JsonProcessingException | UnsupportedEncodingException e ) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }


    /**
     * 장바구니 쿠키 삭제
     * @param response
     */
    private void clearBasketCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("basket", null); // 쿠키 값을 null로 설정
        cookie.setMaxAge(0); // 만료 시간을 0으로 설정 (즉시 삭제)
        cookie.setPath("/"); // 모든 경로에서 쿠키 삭제
        response.addCookie(cookie); // HTTP 응답에 쿠키 추가
    }

}
