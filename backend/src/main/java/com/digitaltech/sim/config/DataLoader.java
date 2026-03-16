package com.digitaltech.sim.config;

import com.digitaltech.sim.model.Branch;
import com.digitaltech.sim.model.Inventory;
import com.digitaltech.sim.model.Product;
import com.digitaltech.sim.model.User;
import com.digitaltech.sim.repository.BranchRepository;
import com.digitaltech.sim.repository.InventoryRepository;
import com.digitaltech.sim.repository.ProductRepository;
import com.digitaltech.sim.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Seeds the database with initial data on application startup.
 * All operations are idempotent: data is only inserted if it does not already exist.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Running DataLoader seed...");

        List<Branch> branches = seedBranches();
        List<Product> products = seedProducts();
        seedInventory(branches, products);
        seedAdminUser();

        log.info("DataLoader seed completed.");
    }

    // -------------------------------------------------------------------------
    // Branches
    // -------------------------------------------------------------------------

    private List<Branch> seedBranches() {
        if (branchRepository.count() > 0) {
            log.info("Branches already seeded, skipping.");
            return branchRepository.findAll();
        }

        Branch downtown = new Branch();
        downtown.setName("Downtown");
        downtown.setAddress("123 Main Street, Downtown");
        downtown.setStatus(true);

        Branch northSide = new Branch();
        northSide.setName("North Side");
        northSide.setAddress("456 Oak Avenue, North District");
        northSide.setStatus(true);

        Branch westPark = new Branch();
        westPark.setName("West Park");
        westPark.setAddress("789 Elm Boulevard, West Park");
        westPark.setStatus(true);

        List<Branch> saved = branchRepository.saveAll(List.of(downtown, northSide, westPark));
        log.info("Seeded {} branches.", saved.size());
        return saved;
    }

    // -------------------------------------------------------------------------
    // Products
    // -------------------------------------------------------------------------
    private List<Product> seedProducts() {
        if (productRepository.count() > 0) {
            log.info("Products already seeded, skipping.");
            return productRepository.findAll();
        }

        Product laptop = buildProduct("Laptop Pro 15", "TECH-LP-001", new BigDecimal("1299.99"));
        Product monitor = buildProduct("4K Monitor 27\"", "TECH-MN-002", new BigDecimal("449.99"));
        Product keyboard = buildProduct("Mechanical Keyboard", "PERI-KB-003", new BigDecimal("89.99"));
        Product mouse = buildProduct("Wireless Mouse", "PERI-MS-004", new BigDecimal("49.99"));
        Product headset = buildProduct("Noise-Canceling Headset", "PERI-HS-005", new BigDecimal("129.99"));
        Product webcam = buildProduct("HD Webcam 1080p", "PERI-WC-006", new BigDecimal("79.99"));

        List<Product> saved = productRepository.saveAll(
                List.of(laptop, monitor, keyboard, mouse, headset, webcam));
        log.info("Seeded {} products.", saved.size());
        return saved;
    }

    private Product buildProduct(String name, String sku, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setSku(sku);
        product.setPrice(price);
        product.setStatus(true);
        return product;
    }

    // -------------------------------------------------------------------------
    // Inventory
    // -------------------------------------------------------------------------
    private void seedInventory(List<Branch> branches, List<Product> products) {
        if (inventoryRepository.count() > 0) {
            log.info("Inventory already seeded, skipping.");
            return;
        }

        int[][] quantities = {
            {10, 8, 25, 30, 15, 20},
            { 5, 4, 20, 25, 10, 12},
            { 8, 6, 18, 22, 12, 15},
        };

        for (int b = 0; b < branches.size(); b++) {
            Branch branch = branches.get(b);
            for (int p = 0; p < products.size(); p++) {
                Inventory inv = new Inventory();
                inv.setBranch(branch);
                inv.setProduct(products.get(p));
                inv.setQuantity(quantities[b][p]);
                inventoryRepository.save(inv);
            }
        }

        log.info("Seeded inventory for {} branches × {} products.",
                branches.size(), products.size());
    }

    // -------------------------------------------------------------------------
    // Admin user
    // -------------------------------------------------------------------------
    private void seedAdminUser() {
        String adminUsername = "admin";

        if (userRepository.existsByUsername(adminUsername)) {
            log.info("Admin user already exists, skipping.");
            return;
        }

        User admin = new User();
        admin.setUsername(adminUsername);
        admin.setPassword(passwordEncoder.encode("admin1234"));
        userRepository.save(admin);

        log.info("Admin user seeded with username '{}'.", adminUsername);
    }
}