package com.errami.billingservice.entities;

import com.errami.billingservice.model.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Collection;
import java.util.Date;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Bill {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    private Date billingDate;
    private String customerID;
    @OneToMany(mappedBy = "bill")
    private Collection<ProductItem> productItems;
    @Transient
    private Customer customer;
}