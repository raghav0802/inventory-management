package com.example.inventory.service;

import com.example.inventory.model.Supplier;
import com.example.inventory.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {
    private final SupplierRepository repo;

    public SupplierService(SupplierRepository repo) {
        this.repo = repo;
    }

    public List<Supplier> getAll() {
        return repo.findAll();
    }

    public Optional<Supplier> getById(String id) {
        return repo.findById(id);
    }

    public Supplier create(Supplier supplier) {
        supplier.setCreatedAt(LocalDate.now());
        return repo.save(supplier);
    }

    public Supplier update(String id, Supplier updated) {
        return repo.findById(id).map(existing -> {
            existing.setCompanyName(updated.getCompanyName());
            existing.setContactPerson(updated.getContactPerson());
            existing.setEmail(updated.getEmail());
            existing.setPhone(updated.getPhone());
            existing.setAddress(updated.getAddress());
            return repo.save(existing);
        }).orElseThrow(() -> new RuntimeException("Supplier not found"));
    }

    public void delete(String id) {
        repo.deleteById(id);
    }
}
