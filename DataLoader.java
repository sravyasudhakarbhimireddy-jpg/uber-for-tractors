package com.internship.tractor.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.internship.tractor.model.Equipment;
import com.internship.tractor.repository.EquipmentRepository;

@Component
public class DataLoader implements CommandLineRunner {
    private final EquipmentRepository repository;

    public DataLoader(EquipmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            repository.save(new Equipment("Mahindra 575 DI Tractor", "Tractor", 500, "Hyderabad", true));
            repository.save(new Equipment("John Deere 5050D Tractor", "Tractor", 650, "Medchal", true));
            repository.save(new Equipment("Combine Harvester", "Harvester", 900, "Rangareddy", true));
            repository.save(new Equipment("Rotavator", "Farm Implement", 300, "Shamshabad", true));
            repository.save(new Equipment("Mini Excavator", "Excavator", 1100, "Hyderabad", true));
        }
    }
}
