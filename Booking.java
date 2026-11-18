package com.internship.tractor.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    private String customerName;
    private String phone;
    private int hours;
    private double totalPrice;
    private LocalDateTime bookingTime;
    private String status;

    public Booking() {}

    @PrePersist
    public void beforeSave() {
        if (bookingTime == null) bookingTime = LocalDateTime.now();
        if (status == null) status = "CONFIRMED";
    }

    public Long getId() { return id; }
    public Equipment getEquipment() { return equipment; }
    public void setEquipment(Equipment equipment) { this.equipment = equipment; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public int getHours() { return hours; }
    public void setHours(int hours) { this.hours = hours; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public LocalDateTime getBookingTime() { return bookingTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
