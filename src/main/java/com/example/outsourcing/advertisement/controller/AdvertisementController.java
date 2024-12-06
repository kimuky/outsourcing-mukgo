package com.example.outsourcing.advertisement.controller;

import com.example.outsourcing.advertisement.dto.AdvertisementRequestDto;
import com.example.outsourcing.advertisement.dto.AdvertisementResponseDto;
import com.example.outsourcing.advertisement.service.AdvertisementService;
import com.example.outsourcing.entity.User;
import com.example.outsourcing.error.errorcode.ErrorCode;
import com.example.outsourcing.error.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @PostMapping("/{storeId}/advertisements")
    public ResponseEntity<AdvertisementResponseDto> RequestAdvertisement(@PathVariable Long storeId,
                                                                         @Valid @RequestBody AdvertisementRequestDto requestDto,
                                                                         HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession(false);
        User loginUser = (User) session.getAttribute("user");

        LocalDateTime requestLocalDateTime
                = requestDto.getContractDate().atStartOfDay();

        if (requestLocalDateTime.isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.BAD_REQUEST_YEAR_MONTH_DAY);
        }

        AdvertisementResponseDto responseDto
                = advertisementService.RequestAdvertisement(storeId, loginUser.getId(), requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

}
