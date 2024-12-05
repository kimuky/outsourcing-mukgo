package com.example.outsourcing.user.repository;

import com.example.outsourcing.admin.dto.AdminAllOrderStaticsDto;
import com.example.outsourcing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 사용자 검색 및 예외
    default User findUserByEmailOrElseThrow (String email) {
        return findUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    Optional<User> findUserByEmail(String email);

    //TODO: 오더로 옮겨야함

    // 하루 모든 주문건수와 총액
    @Query(value = "SELECT new com.example.outsourcing.admin.dto" +
            ".AdminAllOrderStaticsDto(COUNT(1), SUM(o.totalPrice))" +
            " FROM orders o " +
            "WHERE DATE_FORMAT(o.createdAt, 'yyyy-MM-dd') = DATE_FORMAT(NOW(), 'yyyy-MM-dd')")
    AdminAllOrderStaticsDto getDailyStatics();

    // 정해진 날짜 내의 모든 주문건수와 총액
    @Query(value = "SELECT new com.example.outsourcing.admin.dto" +
            ".AdminAllOrderStaticsDto(COUNT(1), SUM(o.totalPrice)) " +
            "FROM orders o WHERE o.createdAt BETWEEN :start_date AND :end_date ")
    AdminAllOrderStaticsDto getMonthlyStatics(@Param(value = "start_date") LocalDateTime startDate,
                                                @Param(value = "end_date") LocalDateTime endDate);

    @Query(value = "SELECT * FROM ", nativeQuery = true)
    void getStaticsByStore(LocalDateTime startDate, LocalDateTime endDate);

}
