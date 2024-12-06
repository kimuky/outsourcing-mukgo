package com.example.outsourcing.menu.repository;

import com.example.outsourcing.entity.Menu;
import com.example.outsourcing.error.errorcode.ErrorCode;
import com.example.outsourcing.error.exception.CustomException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    default Menu findByMenuOrElseThrow(Long menuId) {
        return findById(menuId).orElseThrow(
                () -> new CustomException(ErrorCode.MENU_NOT_FOUND)
        );
    }
}
