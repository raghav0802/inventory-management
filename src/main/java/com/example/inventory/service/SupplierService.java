package com.example.inventory.service;

import com.example.inventory.dto.SupplierDto;
import com.example.inventory.mapper.SupplierMapper;
import com.example.inventory.model.Supplier;
import com.example.inventory.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SupplierService {
    private final SupplierRepository repo;

    public SupplierService(SupplierRepository repo) {
        this.repo = repo;
    }


    public List<SupplierDto> getAllSuppliers() {
        return repo.findAll()
                .stream()
                .map(SupplierMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<SupplierDto> getSupplierById(String id) {
        return repo.findById(id).map(SupplierMapper::toDto);
    }

    public Optional<SupplierDto> getSupplierByCompanyName(String companyName) {
        return repo.findByCompanyName(companyName).map(SupplierMapper::toDto);
    }

    public SupplierDto createSupplier(SupplierDto dto) {
        Supplier supplier = SupplierMapper.toEntity(dto);
        supplier.setCreatedAt(LocalDateTime.now());


        Supplier saved = repo.save(supplier);
        return SupplierMapper.toDto(saved);
    }

    public Optional<SupplierDto> updateSupplier(String id, SupplierDto dto) {
        return repo.findById(id).map(existing -> {
            existing.setCompanyName(dto.getCompanyName());
            existing.setContactPerson(dto.getContactPerson());
            existing.setEmail(dto.getEmail());
            existing.setPhone(dto.getPhone());
            existing.setAddress(dto.getAddress());


            Supplier updated = repo.save(existing);
            return SupplierMapper.toDto(updated);
        });
    }

    public boolean deleteSupplier(String id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }
}
