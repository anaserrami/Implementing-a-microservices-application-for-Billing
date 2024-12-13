package com.errami.inventoryservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.errami.inventoryservice.entities.Product;

public interface ProductRepository extends JpaRepository<Product, String> {
}
