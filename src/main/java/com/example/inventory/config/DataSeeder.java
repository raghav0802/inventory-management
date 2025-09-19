package com.example.inventory.config;

import com.example.inventory.model.*;
import com.example.inventory.repository.*;
import com.example.inventory.util.PasswordUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepo;
    private final SupplierRepository supplierRepo;
    private final UserRepository userRepo;

    public DataSeeder(ProductRepository productRepo, SupplierRepository supplierRepo, UserRepository userRepo) {
        this.productRepo = productRepo;
        this.supplierRepo = supplierRepo;
        this.userRepo = userRepo;
    }

    @Override
    public void run(String... args) {
        // Clear old data (optional, good for dev/testing)
        productRepo.deleteAll();
        supplierRepo.deleteAll();
        userRepo.deleteAll();

        // Seed Suppliers
        Supplier dell = Supplier.builder()
                .companyName("Dell Inc.")
                .contactPerson("John Doe")
                .email("john@dell.com")
                .phone("1234567890")
                .address("Texas, USA")
                .createdAt(LocalDateTime.now())
                .build();

        Supplier hp = Supplier.builder()
                .companyName("HP Inc.")
                .contactPerson("Jane Smith")
                .email("jane@hp.com")
                .phone("9876543210")
                .address("California, USA")
                .createdAt(LocalDateTime.now())
                .build();

        supplierRepo.saveAll(List.of(dell, hp));

        // Seed Products
        Product laptop = Product.builder()
                .name("Dell XPS 15")
                .description("High performance laptop")
                .category("Electronics")
                .sku("XPS15-2023")
                .price(1500.0)
                .quantity(10)
                .supplierId(dell.getId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Product printer = Product.builder()
                .name("HP LaserJet Pro")
                .description("Fast office printer")
                .category("Electronics")
                .sku("HP-LJ-2023")
                .price(400.0)
                .quantity(5)
                .supplierId(hp.getId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        productRepo.saveAll(List.of(laptop, printer));

        // Seed a default User
        User admin = User.builder()
                .username("admin")
                .password(PasswordUtil.hashPassword("admin123")) // ⚡ hashed password
                .role("ADMIN")
                .fullName("System Admin")
                .createdAt(LocalDate.now())
                .build();

        userRepo.save(admin);

        System.out.println("✅ Database seeded with sample data!");
    }
}
