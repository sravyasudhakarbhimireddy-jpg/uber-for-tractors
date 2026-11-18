package com.internship.tractor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.internship.tractor.model.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {}
