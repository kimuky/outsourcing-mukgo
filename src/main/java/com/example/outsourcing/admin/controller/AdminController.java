package com.example.outsourcing.admin.controller;

import com.example.outsourcing.admin.dto.BetweenStaticsResponseDto;
import com.example.outsourcing.admin.dto.DailyStaticsResponseDto;
import com.example.outsourcing.admin.dto.MonthlyStaticsResponseDto;
import com.example.outsourcing.admin.dto.StartEndDateTimeDto;
import com.example.outsourcing.admin.service.AdminService;
import com.example.outsourcing.advertisement.dto.AdvertisementResponseDto;
import com.example.outsourcing.error.errorcode.ErrorCode;
import com.example.outsourcing.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    // "#{T(java.time.LocalDate).now()}" -> Spql: 현재 시간을 LocalDate
    // "#{T(java.time.LocalDate).now().getYear}" -> Spql: 현재 시간 기준으로 년도를 받아옴
    // "#{T(java.time.LocalDate).now().getMonthValue()}" -> Spal: 현재 시간 기준으로 월을 받아옴

    /**
     * 해당 날의 주문 건수, 주문 총액을 확인 가능
     * 가게 id 를 입력하지 않으면 해당 날의 모든 가게 통계 데이터를 확인 가능
     * date 를 입력하지 않으면 컨트롤러를 실행시킨 날짜로 default value
     *
     * @param storeId 가게 id
     * @param date    원하는 날짜 ex) 2024-12-05
     * @return 주문 건수, 주문 총액, 파라미터로 입력한 날짜
     */
    @GetMapping("/statics/daily")
    public ResponseEntity<?> getDailyStatics(@RequestParam(required = false) Long storeId,
                                             @RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}") LocalDate date) {

        DailyStaticsResponseDto dailyStatics = adminService.getDailyStatics(storeId, date);

        return ResponseEntity.status(HttpStatus.OK).body(dailyStatics);
    }

    /**
     * 모든 가게를 달별로 주문건수, 주문 총액을 확인 가능
     *
     * @param storeId 가게 id
     * @param year    원하는 연도
     * @param month   원하는 달
     * @return 가게 id, 주문 건수, 주문 총액, 해당 연도 달의 첫날, 해당 연도 달의 첫날 마지막날
     */
    @GetMapping("/statics/monthly")
    public ResponseEntity<List<MonthlyStaticsResponseDto>> getMonthlyStatics(
            @RequestParam(required = false) Long storeId,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getYear}") Integer year,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getMonthValue()}") Integer month) {

        // 연도와 달을 검사
        if ((year < LocalDate.MIN.getYear() || year > LocalDate.MAX.getYear()) || (month > 12 || month < 1)) {
            throw new CustomException(ErrorCode.BAD_REQUEST_YEAR_MONTH);
        }

        List<MonthlyStaticsResponseDto> monthlyStatics = adminService.getMonthlyStatics(storeId, year, month);

        return ResponseEntity.status(HttpStatus.OK).body(monthlyStatics);
    }

    /**
     * 원하는 날짜 사이 통계를 조회
     *
     * @param storeId 가게 id
     * @param startDate 원하는 시작 날짜
     * @param endDate   원하는 마지막 날짜
     * @return 가게 id, 주문 건수, 주문 총액, 원하는 시작 날짜, 원하는 마지막 날짜
     */
    @GetMapping("/statics/between")
    public ResponseEntity<List<BetweenStaticsResponseDto>> getStaticsBetween(
            @RequestParam(required = false) Long storeId,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}") LocalDate startDate,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}") LocalDate endDate) {

        StartEndDateTimeDto dateDto = handlingDate(startDate, endDate);

        List<BetweenStaticsResponseDto> staticsAll = adminService.getStaticsBetween(storeId, dateDto);
        return ResponseEntity.status(HttpStatus.OK).body(staticsAll);
    }

    @GetMapping("/advertisements")
    public ResponseEntity<List<AdvertisementResponseDto>> getAdvertisementList (@RequestParam(required = false) String status) {
        List<AdvertisementResponseDto> advertisementList = adminService.getAdvertisementList(status);

        return ResponseEntity.status(HttpStatus.OK).body(advertisementList);
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
        LocalDateTime startOfDay = startDate.atTime(LocalTime.MIN); // ex) 00:00:00
        LocalDateTime endOfDay = endDate.atTime(LocalTime.MAX); // ex) 23:59:59.99999

        return new StartEndDateTimeDto(startOfDay, endOfDay);
    }
}
