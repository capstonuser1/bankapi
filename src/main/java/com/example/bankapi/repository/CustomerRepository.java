package com.example.bankapi.repository;

import com.example.bankapi.entity.Customer;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query(value = "SELECT * FROM Customers c",  nativeQuery = true )
    List<Customer> getCustomers();

    @Query(value = "SELECT * FROM Customers c WHERE c.customer_number = :customerNumber", nativeQuery = true)
    Customer getCustomer(@Param("customerNumber") String customerNumber);
}
