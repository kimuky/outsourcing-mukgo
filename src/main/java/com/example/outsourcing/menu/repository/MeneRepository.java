package com.example.outsourcing.menu.repository;

import com.example.outsourcing.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeneRepository extends JpaRepository<Menu, Long> {
}
