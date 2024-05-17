package com.ag.customer.service;

import com.ag.customer.repository.CustomerDao;
import com.ag.customer.Customer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao {
    private static List<Customer> customers;

    static {
        customers = new ArrayList<>();
        customers.add(new Customer(1, "Avinaash", "avinaash@io.com", 31));
        customers.add(new Customer(2, "Leevy", "leevy@io.com", 30));
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers
                .stream()
                .filter((customer -> customer.getId().equals(id)))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customers.stream().anyMatch(customer -> customer.getEmail().equals(email));
    }

    @Override
    public void deleteCustomerById(Integer id) {
        customers.removeIf(customer -> customer.getId().equals(id));
    }

    @Override
    public boolean existsPersonById(Integer id) {
       return customers.stream().anyMatch(customer -> customer.getId().equals(id));
    }

    @Override
    public void updateCustomer(Customer updatedCustomer) {
        customers.add(updatedCustomer);
    }
}
