package com.example.outsourcing.user.repository;

import com.example.outsourcing.entity.User;
import com.example.outsourcing.error.errorcode.ErrorCode;
import com.example.outsourcing.error.exception.CustomException;
import com.example.outsourcing.status.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    default User findUserByIdOrElseThrow (Long id) {
        return findUserById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    // 아이디가 있는지 검사
    default User findUserByEmailOrElseThrow (String email) {
        return findUserByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    // 아이디 중복 검사
    default void findUserByEmailForDuplicateCheck(String email) {
        if (findUserByEmail(email).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_USER_ID);
        }
    }

    Optional<User> findUserByEmail(String email);

    List<User> findUserByEmailAndStatus( String email, UserStatus userStatus);

    Optional<User> findUserById(Long id);
}


