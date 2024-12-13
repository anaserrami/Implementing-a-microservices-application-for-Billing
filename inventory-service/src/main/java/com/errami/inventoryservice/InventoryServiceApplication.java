package com.errami.inventoryservice;

import com.errami.inventoryservice.entities.Product;
import com.errami.inventoryservice.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(ProductRepository productRepository) {
        return args -> {
            for (int i = 0; i < 10; i++) {
                Product product = Product.builder()
                        .name("Product" + i)
                        .price(Math.random() * 1000)
                        .quantity((int) (Math.random() * 50))
                        .build();
                productRepository.save(product);
            }
            productRepository.findAll().forEach(product -> {
                System.out.println(product.toString());
            });
        };
    }
}
