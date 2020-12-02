package com.digitalacademy.customer.customer.repositories;

import com.digitalacademy.customer.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends
        JpaRepository<Customer, Long> {

    Customer findAllById(Long id);
}
