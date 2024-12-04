package com.example.outsourcing.user.repository;

import com.example.outsourcing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
