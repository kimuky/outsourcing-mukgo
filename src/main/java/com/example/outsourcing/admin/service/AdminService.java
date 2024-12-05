package com.example.outsourcing.admin.service;

import com.example.outsourcing.admin.dto.AdminAllOrderStaticsDto;
import com.example.outsourcing.admin.dto.StartEndDateTimeDto;
import com.example.outsourcing.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public List<AdminAllOrderStaticsDto> getStatics(User loginUser, StartEndDateTimeDto dateDto, Long storeId) {
        // 당일 내 모든 주문 건수와 총액
        AdminAllOrderStaticsDto dailyStatics = userRepository.getDailyStatics();

        // 정해진 날짜 내  모든 주문 건수와 총액
        AdminAllOrderStaticsDto monthlyStatics = userRepository.getMonthlyStatics(dateDto.getStartDate(), dateDto.getEndDate());

        // 가게별 당일 주문 건수와 총액
        userRepository.getStaticsByStore(dateDto.getStartDate(), dateDto.getEndDate());

        return null;
    }

}
