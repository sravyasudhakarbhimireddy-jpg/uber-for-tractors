package com.internship.tractor.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import com.internship.tractor.dto.BookingRequest;
import com.internship.tractor.model.Booking;
import com.internship.tractor.model.Equipment;
import com.internship.tractor.repository.BookingRepository;
import com.internship.tractor.repository.EquipmentRepository;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingRepository bookingRepository;
    private final EquipmentRepository equipmentRepository;

    public BookingController(BookingRepository bookingRepository, EquipmentRepository equipmentRepository) {
        this.bookingRepository = bookingRepository;
        this.equipmentRepository = equipmentRepository;
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingRepository.findAllByOrderByBookingTimeDesc();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Booking createBooking(@Valid @RequestBody BookingRequest request) {
        Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipment not found"));
        if (!equipment.isAvailable()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This equipment is not available");
        }
        if (request.getHours() < 1 || request.getHours() > 24) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hours must be between 1 and 24");
        }

        Booking booking = new Booking();
        booking.setEquipment(equipment);
        booking.setCustomerName(request.getCustomerName().trim());
        booking.setPhone(request.getPhone());
        booking.setHours(request.getHours());
        booking.setTotalPrice(equipment.getPricePerHour() * request.getHours());
        return bookingRepository.save(booking);
    }

    @PutMapping("/{id}/cancel")
    public Booking cancelBooking(@PathVariable Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        booking.setStatus("CANCELLED");
        return bookingRepository.save(booking);
    }
}
