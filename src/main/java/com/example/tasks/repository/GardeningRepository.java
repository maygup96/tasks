package com.example.tasks.repository;

import com.example.tasks.entity.Gardening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GardeningRepository extends JpaRepository<Gardening, Long> {
}
