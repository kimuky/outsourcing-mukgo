package com.example.outsourcing.user.repository;

import com.example.outsourcing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 사용자 검색 및 예외
    default User findUserByEmailOrElseThrow (String email) {
        return findUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    Optional<User> findUserByEmail(String email);
}
