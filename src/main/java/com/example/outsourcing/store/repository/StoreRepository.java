package com.example.outsourcing.store.repository;


import com.example.outsourcing.entity.Store;
import com.example.outsourcing.entity.User;
import com.example.outsourcing.error.errorcode.ErrorCode;
import com.example.outsourcing.error.exception.CustomException;
import com.example.outsourcing.status.StoreStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    long countByUserAndStatusNot(User user, StoreStatus storeStatus);

    default Store findByOrElseThrow(Long storeId) {
        return findById(storeId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_STORE)
        );
    }
}
