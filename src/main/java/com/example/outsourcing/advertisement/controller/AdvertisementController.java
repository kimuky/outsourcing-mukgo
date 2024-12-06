package com.example.outsourcing.advertisement.controller;

import com.example.outsourcing.advertisement.dto.AdvertisementRequestDto;
import com.example.outsourcing.advertisement.dto.AdvertisementResponseDto;
import com.example.outsourcing.advertisement.service.AdvertisementService;
import com.example.outsourcing.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    /**
     * 사장님만 광고 요청 가능하며 원하는 달, 광고 금액을 같이 요청한다.
     * 달은 1 이상, 금액은 20만원 이상부터 가능하다.
     * @param storeId 가게 ID
     * @param requestDto 금액, 원하는 달
     * @param servletRequest 세션 확인용
     * @return 광고 정보 (ID, userId, storeId, price, 광고 계약 날짜, 광고 요청날짜, 광고가 승인난 날짜, 광고 상태)
     */
    @PostMapping("/{storeId}/advertisements")
    public ResponseEntity<AdvertisementResponseDto> RequestAdvertisement(@PathVariable Long storeId,
                                                                         @Valid @RequestBody AdvertisementRequestDto requestDto,
                                                                         HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession(false);
        User loginUser = (User) session.getAttribute("user");

        AdvertisementResponseDto responseDto
                = advertisementService.RequestAdvertisement(storeId, loginUser.getId(), requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

}
