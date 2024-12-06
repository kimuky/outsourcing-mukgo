package com.example.outsourcing.advertisement.repository;

import com.example.outsourcing.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
}
