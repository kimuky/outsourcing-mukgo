package com.example.outsourcing.store.controller;

import com.example.outsourcing.entity.Store;
import com.example.outsourcing.entity.User;
import com.example.outsourcing.store.dto.StoreRequestDto;
import com.example.outsourcing.store.dto.StoreResponseDto;
import com.example.outsourcing.store.repository.StoreRepository;
import com.example.outsourcing.store.service.StoreService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

        Store findStore = storeRepository.findByOrElseThrow(storeId);

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
}
