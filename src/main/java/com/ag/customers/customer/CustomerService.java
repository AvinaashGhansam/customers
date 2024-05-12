package com.ag.customers.customer;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Business Logic
 * The service gets its data from the CustomerDao
 */
@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }
    public Customer getCustomerById(Integer id) {
        return customerDao.selectCustomerById(id).orElseThrow(() -> new IllegalArgumentException(" customer with [%s] not found".formatted(id)));
    }
}
