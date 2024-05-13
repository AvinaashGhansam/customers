package com.ag.customers.customer;

import com.ag.customers.exceptions.DuplicateResourceException;
import com.ag.customers.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Business Logic
 * The service gets its data from the CustomerDao
 */
@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao, CustomerRepository customerRepository) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomerById(Integer id) {
        return customerDao.selectCustomerById(id).orElseThrow(() -> new ResourceNotFoundException(" customer with id[%s] not found".formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        // If the email exist, throw an exception
        if (customerDao.existsPersonWithEmail(customerRegistrationRequest.email())) {
            throw new DuplicateResourceException("Customer with email %s already exist".formatted(customerRegistrationRequest.email()));
        }
        // Create the customer and save them to the database
        customerDao.insertCustomer(new Customer(customerRegistrationRequest.name(), customerRegistrationRequest.email(), customerRegistrationRequest.age()));
    }

}
