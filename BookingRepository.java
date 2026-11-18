package com.internship.tractor.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.internship.tractor.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByOrderByBookingTimeDesc();
}
