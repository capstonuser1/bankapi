package com.example.bankapi.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class CustomerDto implements Serializable {
    private long customerId;
    private String customerNumber;
    private String fullName;
    private String email;
    private LocalDate createdDate;

    public CustomerDto() {
    }

    public CustomerDto(long customerId, String customerNumber, String fullName, String email, LocalDate createdDate) {
        this.customerId = customerId;
        this.customerNumber = customerNumber;
        this.fullName = fullName;
        this.email = email;
        this.createdDate = createdDate;
    }

    public long getCustomerId() {
        return customerId;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }
}
