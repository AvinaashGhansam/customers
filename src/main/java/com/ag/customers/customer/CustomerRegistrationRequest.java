package com.ag.customers.customer;

public record CustomerRegistrationRequest (
    String name,
    String email,
    Integer age
) {}
