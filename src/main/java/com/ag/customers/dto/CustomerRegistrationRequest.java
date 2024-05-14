package com.ag.customers.dto;

public record CustomerRegistrationRequest (
    String name,
    String email,
    Integer age
) {}
