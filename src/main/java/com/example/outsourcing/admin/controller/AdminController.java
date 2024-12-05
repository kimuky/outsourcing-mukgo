package com.example.outsourcing.admin.controller;

import com.example.outsourcing.admin.dto.StartEndDateTimeDto;
import com.example.outsourcing.admin.service.AdminService;
import com.example.outsourcing.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    /**
     *
     * @param startDate 조회 시, 시작일
     * @param endDate 조회 시, 마지막일
     * @param storeId 자신의 가게 id
     * @param servletRequest 세션 정보
     * @return
     */
    @GetMapping("/statics")
    public ResponseEntity<?> getStaticsByStore(@RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}") LocalDate startDate,
                                               @RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}") LocalDate endDate,
                                               @RequestParam(required = false) Long storeId,
                                               HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession(false);
        User loginUser = (User) session.getAttribute("user");

        StartEndDateTimeDto dateDto = handlingDate(startDate, endDate);

//        adminService.getStatics(loginUser, dateDto, storeId);
        return null;
    }

    // 날짜를 판별, 예외 핸들링하기 위함
    private StartEndDateTimeDto handlingDate(LocalDate startDate, LocalDate endDate) {
        // 예외처리를 하지않고 예외를 전환
        if (startDate.isAfter(endDate) || startDate.isEqual(endDate)) {
            // 조회 호출 시, 날짜를 기준으로 그 달의 첫날
            startDate = LocalDate.now().withDayOfMonth(1);

            // 조회 호출 시, 날짜를 기준으로 그 달의 마지막날
            endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        }
        // LocalDate to LocalDateTime
        LocalDateTime startOfDay = startDate.atStartOfDay(); // ex) 00:00:00
        LocalDateTime endOfDay = endDate.atTime(LocalTime.MAX);// ex) 23:59:59.99999

        return new StartEndDateTimeDto(startOfDay, endOfDay);
    }
}
