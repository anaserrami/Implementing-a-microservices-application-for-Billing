package com.errami.billingservice;

import com.errami.billingservice.entities.Bill;
import com.errami.billingservice.entities.ProductItem;
import com.errami.billingservice.feign.CustomerRestClient;
import com.errami.billingservice.feign.ProductItemRestClient;
import com.errami.billingservice.model.Customer;
import com.errami.billingservice.model.Product;
import com.errami.billingservice.repositories.BillRepository;
import com.errami.billingservice.repositories.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.PagedModel;

import java.util.Date;
import java.util.List;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(BillRepository billRepository,
                            ProductItemRepository productItemRepository,
                            CustomerRestClient customerRestClient,
                            ProductItemRestClient productItemRestClient) {
        return args -> {
            // Fetch paged customers dynamically
            PagedModel<Customer> pagedCustomers = customerRestClient.getAllCustomers();
            List<Customer> customers = pagedCustomers.getContent().stream().toList();

            if (customers.isEmpty()) {
                System.out.println("No customers found. Please ensure the Customer-Service has data.");
            } else {
                // Pick the first customer
                Customer customer = customers.get(0);
                System.out.println("Selected Customer: " + customer);

                // Create and save a new bill for the customer
                Bill bill = billRepository.save(new Bill(null, new Date(), customer.getId(), null, null));
                System.out.println("Bill created with ID: " + bill.getId());

                // Fetch paged products from Inventory-Service
                System.out.println("Fetching products from Inventory-Service...");
                PagedModel<Product> productPagedModel = productItemRestClient.pageProducts(0, 5);
                productPagedModel.forEach(p -> {
                    ProductItem productItem = new ProductItem();
                    productItem.setProductID(p.getId());
                    productItem.setPrice(p.getPrice());
                    productItem.setQuantity(1 + new Random().nextInt(100));
                    productItem.setBill(bill);
                    productItemRepository.save(productItem);
                    System.out.println("Saved ProductItem with Product ID: " + p.getId());
                });

                // Fetch and display all ProductItems for the created bill
                System.out.println("Fetching all ProductItems for Bill ID: " + bill.getId());
                productItemRepository.findByBillId(bill.getId()).forEach(pi -> {
                    System.out.println("ProductItem: ProductID = " + pi.getProductID() +
                            ", Quantity = " + pi.getQuantity() +
                            ", Price = " + pi.getPrice());
                });
            }
        };
    }
}