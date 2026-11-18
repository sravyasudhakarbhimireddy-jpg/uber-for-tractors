package com.internship.tractor.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import com.internship.tractor.model.Equipment;
import com.internship.tractor.repository.EquipmentRepository;
import com.internship.tractor.repository.BookingRepository;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {
    private final EquipmentRepository equipmentRepository;
    private final BookingRepository bookingRepository;

    public EquipmentController(EquipmentRepository equipmentRepository, BookingRepository bookingRepository) {
        this.equipmentRepository = equipmentRepository;
        this.bookingRepository = bookingRepository;
    }

    @GetMapping
    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Equipment addEquipment(@Valid @RequestBody Equipment equipment) {
        equipment.setId(null);
        return equipmentRepository.save(equipment);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEquipment(@PathVariable Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipment not found"));
        boolean hasBookings = bookingRepository.findAll().stream()
                .anyMatch(booking -> booking.getEquipment().getId().equals(id));
        if (hasBookings) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete equipment that has bookings");
        }
        equipmentRepository.delete(equipment);
    }
}
