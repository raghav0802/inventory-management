package com.example.inventory.controller;

import com.example.inventory.model.Supplier;
import com.example.inventory.service.SupplierService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {
    private final SupplierService service;

    public SupplierController(SupplierService service) {
        this.service = service;
    }

    @GetMapping
    public List<Supplier> getSuppliers() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Supplier getSupplier(@PathVariable String id) {
        return service.getById(id).orElseThrow(() -> new RuntimeException("Supplier not found"));
    }

    @PostMapping
    public Supplier addSupplier(@RequestBody Supplier supplier) {
        return service.create(supplier);
    }

    @PutMapping("/{id}")
    public Supplier updateSupplier(@PathVariable String id, @RequestBody Supplier supplier) {
        return service.update(id, supplier);
    }

    @DeleteMapping("/{id}")
    public void deleteSupplier(@PathVariable String id) {
        service.delete(id);
    }
}
