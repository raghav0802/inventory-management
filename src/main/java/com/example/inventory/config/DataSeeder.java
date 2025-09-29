package com.example.inventory.config;

import com.example.inventory.model.*;
import com.example.inventory.repository.*;
import com.example.inventory.util.PasswordUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepo; // MongoDB Repo
    private final SupplierRepository supplierRepo;
    private final UserRepository userRepo;
    private final ProductSearchRepository productSearchRepo; // Elasticsearch Repo

    public DataSeeder(ProductRepository productRepo,
                      SupplierRepository supplierRepo,
                      UserRepository userRepo,
                      ProductSearchRepository productSearchRepo) {
        this.productRepo = productRepo;
        this.supplierRepo = supplierRepo;
        this.userRepo = userRepo;
        this.productSearchRepo = productSearchRepo;
    }

    @Override
    public void run(String... args) {
        // Clear old data (only in dev mode!)
        productRepo.deleteAll();
        supplierRepo.deleteAll();
        userRepo.deleteAll();
        productSearchRepo.deleteAll();

        // Suppliers
        Supplier dell = supplierRepo.save(Supplier.builder()
                .companyName("Dell Inc.")
                .contactPerson("John Doe")
                .email("john@dell.com")
                .phone("1234567890")
                .address("Texas, USA")
                .createdAt(LocalDateTime.now())
                .build());

        Supplier hp = supplierRepo.save(Supplier.builder()
                .companyName("HP Inc.")
                .contactPerson("Jane Smith")
                .email("jane@hp.com")
                .phone("9876543210")
                .address("California, USA")
                .createdAt(LocalDateTime.now())
                .build());

        // Products (store in Mongo + Elasticsearch)
        Random random = new Random();
        List<Product> mongoProducts = new ArrayList<>();
        List<ProductIndex> esProducts = new ArrayList<>();

        String[] categories = {"Electronics", "Clothing", "Shoes", "Accessories"};
        String[] brands = {"Dell", "HP", "Nike", "Adidas", "Sony", "Samsung"};

        for (int i = 1; i <= 10_000; i++) {
            String category = categories[random.nextInt(categories.length)];
            String brand = brands[random.nextInt(brands.length)];
            String skuPrefix = brand.length() >= 3 ? brand.substring(0, 3) : brand;

            Product product = Product.builder()
                    .name(brand + " " + category + " " + i)
                    .description("High-quality " + category + " product #" + i)
                    .category(category)
                    .sku(skuPrefix.toUpperCase() + "-" + i)
                    .price((double) (50 + random.nextInt(2000)))
                    .quantity(random.nextInt(100))
                    .supplierId((i % 2 == 0) ? dell.getId() : hp.getId())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            mongoProducts.add(product);

            ProductIndex esProduct = ProductIndex.builder()
                    .id(UUID.randomUUID().toString())
                    .name(product.getName())
                    .description(product.getDescription())
                    .category(product.getCategory())
                    .price(product.getPrice())
                    .build();

            esProducts.add(esProduct);
        }

        productRepo.saveAll(mongoProducts);     // ✅ save to MongoDB
        productSearchRepo.saveAll(esProducts);  // ✅ save to Elasticsearch

        // User
        userRepo.save(User.builder()
                .username("admin")
                .password(PasswordUtil.hashPassword("admin123"))
                .role("ADMIN")
                .fullName("System Admin")
                .createdAt(LocalDate.now())
                .build());

        System.out.println("✅ MongoDB and Elasticsearch seeded with sample data!");
    }
}
