package com.example.outsourcing.admin.service;

import com.example.outsourcing.admin.dto.StartEndDateDto;
//import com.example.outsourcing.admin.repository.AdminRepository;
import com.example.outsourcing.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

//    public void getStatics(User loginUser, StartEndDateDto dateDto, Long storeId) {
//        userRepository.getDailyOrderCount();
//    }

}
