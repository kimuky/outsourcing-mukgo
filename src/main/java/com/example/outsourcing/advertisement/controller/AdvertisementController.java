package com.example.outsourcing.advertisement.controller;

import com.example.outsourcing.advertisement.dto.AdvertisementRequestDto;
import com.example.outsourcing.advertisement.dto.AdvertisementResponseDto;
import com.example.outsourcing.advertisement.service.AdvertisementService;
import com.example.outsourcing.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @PostMapping("/{storeId}/advertisements")
    public ResponseEntity<AdvertisementResponseDto> RequestAdvertisement(@PathVariable Long storeId,
                                                                         @RequestBody AdvertisementRequestDto requestDto,
                                                                         HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession(false);
        User loginUser = (User) session.getAttribute("user");

        AdvertisementResponseDto responseDto
                = advertisementService.RequestAdvertisement(storeId, loginUser.getId(), requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

}
