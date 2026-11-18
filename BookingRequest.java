package com.internship.tractor.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class BookingRequest {
    @NotNull
    private Long equipmentId;
    @NotBlank
    private String customerName;
    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$", message = "Enter a valid 10-digit phone number")
    private String phone;
    @Min(1)
    private int hours;

    public Long getEquipmentId() { return equipmentId; }
    public void setEquipmentId(Long equipmentId) { this.equipmentId = equipmentId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public int getHours() { return hours; }
    public void setHours(int hours) { this.hours = hours; }
}
