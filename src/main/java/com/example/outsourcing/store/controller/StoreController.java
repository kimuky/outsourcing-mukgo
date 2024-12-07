package com.example.outsourcing.store.controller;

import com.example.outsourcing.entity.Store;
import com.example.outsourcing.entity.User;
import com.example.outsourcing.order.dto.OrderResponseDto;
import com.example.outsourcing.store.dto.StoreMenuResponseDto;
import com.example.outsourcing.store.dto.StoreRequestDto;
import com.example.outsourcing.store.dto.StoreResponseDto;
import com.example.outsourcing.store.repository.StoreRepository;
import com.example.outsourcing.store.service.StoreService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final StoreRepository storeRepository;

    /**
     * 가게 생성
     * @param requestDto
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<StoreResponseDto> createStore(@RequestBody StoreRequestDto requestDto, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        //login 되어있는 user data
        User loginUser = (User) session.getAttribute("user");

        StoreResponseDto responseDto = storeService.createStore(requestDto, loginUser);
        return ResponseEntity.ok().body(responseDto);

    }


    /**
     * 가게 수정
     * @param requestDto
     * @param storeId
     * @param request
     * @return
     */
    @PatchMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> updateStore(
            @RequestBody StoreRequestDto requestDto,
            @PathVariable Long storeId,
            HttpServletRequest request) {

        Store findStore = storeRepository.findByStoreOrElseThrow(storeId);

        HttpSession session = request.getSession(false);
        //login 되어있는 user data
        User loginUser = (User) session.getAttribute("user");

        StoreResponseDto updateStore = storeService.updateStore(
                storeId,
                loginUser.getId(),
                requestDto.getName(),
                requestDto.getMinimumAmount(),
                requestDto.getOpenTime(),
                requestDto.getCloseTime());

        return ResponseEntity.ok().body(updateStore);
    }

    /**
     * 가게 단건 조회
     * @param storeId
     * @return
     */
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreMenuResponseDto> findStore(@PathVariable Long storeId, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        //login 되어있는 user data
        User loginUser = (User) session.getAttribute("user");

        StoreMenuResponseDto responseDto = storeService.findStore(storeId, loginUser);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 가게 이름 검색 조회 + 광고 우선순위 검색
     * @param name 가게 이름
     * @return
     */
    @GetMapping
    public ResponseEntity<List<StoreResponseDto>> SearchStoreByName(
            @RequestParam(value = "name", required = false) String name,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        //login 되어있는 user data
        User loginUser = (User) session.getAttribute("user");

        List<StoreResponseDto> responseDtoList = storeService.SearchStoreByName(name, loginUser);
        return ResponseEntity.ok().body(responseDtoList);
    }

    /**
     * 가게 폐업 변경
     * @param storeId
     * @param request
     * @return
     */
    @PatchMapping("/{storeId}/close")
    public ResponseEntity<Void> closeStore(@PathVariable Long storeId, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        //login 되어있는 user data
        User loginUser = (User) session.getAttribute("user");

        storeService.closeStore(storeId, loginUser.getId());

        return ResponseEntity.ok().build();
    }


    /**
     * 본인 가게만 조회
     * @param request
     * @return
     */
    @GetMapping("/my")
    public ResponseEntity<List<StoreResponseDto>> findMyStore(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        //login 되어있는 user data
        User loginUser = (User) session.getAttribute("user");

        List<StoreResponseDto> responseDtoList = storeService.findMyStore(loginUser);
        return ResponseEntity.ok(responseDtoList);
    }

    /**
     * 가게 주문 조회
     * @param storeId
     * @param request
     * @return
     */
    @GetMapping("/{storeId}/orders")
    public ResponseEntity<List<OrderResponseDto>> checkOrderStore(
            @PathVariable Long storeId,
            HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        //login 되어있는 user data
        User loginUser = (User) session.getAttribute("user");

        List<OrderResponseDto> orders = storeService.checkOrderStore(storeId, loginUser);

        return ResponseEntity.ok().body(orders);
    }

    /**
     * 주문 상태 변경
     * @param storeId
     * @param orderId
     * @param request
     * @return
     */
    @PatchMapping("/{storeId}/orders/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable Long storeId,
            @PathVariable Long orderId,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        //login 되어있는 user data
        User loginUser = (User) session.getAttribute("user");

        OrderResponseDto orderStatus = storeService.updateOrderStatus(storeId, orderId, loginUser);

        return ResponseEntity.ok().body(orderStatus);
    }

}
