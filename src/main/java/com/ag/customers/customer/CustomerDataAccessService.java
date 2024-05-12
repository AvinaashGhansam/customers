package com.ag.customers.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerDataAccessService implements CustomerDao {
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
}
