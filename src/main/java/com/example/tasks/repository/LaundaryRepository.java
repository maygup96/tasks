package com.example.tasks.repository;

import com.example.tasks.entity.Laundary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaundaryRepository extends JpaRepository<Laundary, Long> {
}
