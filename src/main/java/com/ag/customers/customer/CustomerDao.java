package com.ag.customers.customer;

import java.util.List;
import java.util.Optional;

/**
 * This interface will be responsible for accessing the database store
 */
public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer id);
    void insertCustomer(Customer customer);
    boolean existsPersonWithEmail(String email);
}
