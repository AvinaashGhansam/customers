package com.ag.customer.service;

import com.ag.AbstractTestContainersUnitTest;
import com.ag.customer.Customer;
import com.ag.customer.repository.CustomerRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class CustomerJDBCDataAccessServiceTest extends AbstractTestContainersUnitTest {
    private CustomerJDBCDataAccessService customerJDBCDataAccessService;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        // We do it like this because we want a new instance for each test
        // For the jdbc template we must define how we will connect to the database since it is not maintained by spring
        customerJDBCDataAccessService = new CustomerJDBCDataAccessService(
                getJdbcTemplate(), customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        // Given
        Customer customer = generateCustomer();
        customerJDBCDataAccessService.insertCustomer(customer);
        // When
        List<Customer> customers = customerJDBCDataAccessService.selectAllCustomers();
        // Then
        assertThat(customers).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        // Given
        Random r = new Random();
        int low = 18, high = 60;
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Integer age = r.nextInt( high - low) + low ;
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                age);
        customerJDBCDataAccessService.insertCustomer(customer);
        Integer id = customerJDBCDataAccessService
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email)).map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //
        // When
        Optional<Customer> actualCustomer = customerJDBCDataAccessService.selectCustomerById(id);
        // Then
        assertThat(actualCustomer).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getEmail()).isEqualTo(email);
            assertThat(c.getAge()).isEqualTo(age);
        });
    }
    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        // Given
        Integer id = -1;
        // When
        Optional<Customer> actual = customerJDBCDataAccessService.selectCustomerById(id);
        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = generateCustomer();
        // When
        customerJDBCDataAccessService.insertCustomer(customer);
        // Then
        assertThat(customerJDBCDataAccessService.selectAllCustomers()).isNotEmpty();
    }

    @Test
    void existsPersonWithEmail() {
        // Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = generateCustomer(email);
        customerJDBCDataAccessService.insertCustomer(customer);
        // When
        boolean actual = customerJDBCDataAccessService.existsPersonWithEmail(email);
        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithEmailReturnsFalseWhenDoesNotExist() {
        // Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        // When
        boolean actual = customerJDBCDataAccessService.existsPersonWithEmail(email);
        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerById() {
        // Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = generateCustomer(email);
        customerJDBCDataAccessService.insertCustomer(customer);
        Integer id = customerJDBCDataAccessService
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email)).map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        customerJDBCDataAccessService.deleteCustomerById(id);
        // Then
        Optional<Customer> actual = customerJDBCDataAccessService.selectCustomerById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void existsCustomerWithId() {
        // Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = generateCustomer(email);
        customerJDBCDataAccessService.insertCustomer(customer);
        Integer id = customerJDBCDataAccessService
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email)).map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        boolean actual = customerJDBCDataAccessService.existsPersonById(id);
        // Then
        assertThat(actual).isTrue();
    }

    @Test void existsPersonByIdReturnsFalseWhenDoesNotExist() {
        // Given
        int id = -1;
        // When
        boolean actual = customerJDBCDataAccessService.existsPersonById(id);
        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void updateCustomerName() {
        // Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = generateCustomer(email);
        customerJDBCDataAccessService.insertCustomer(customer);
        Integer id = customerJDBCDataAccessService
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email)).map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // Update the customer name
        String newName = "foo";
        // Update Customer
        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);
        customerJDBCDataAccessService.updateCustomer(update);
        // Then
        Optional<Customer>actual = customerJDBCDataAccessService.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerEmail() {
        // Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = generateCustomer(email);
        customerJDBCDataAccessService.insertCustomer(customer);
        Integer id = customerJDBCDataAccessService
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email)).map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // Update the customer Email
        String newEmail = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        // Update Customer
        Customer update = new Customer();
        update.setId(id);
        update.setEmail(newEmail);
        customerJDBCDataAccessService.updateCustomer(update);
        // Then
        Optional<Customer>actual = customerJDBCDataAccessService.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(newEmail);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerAge() {
        // Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = generateCustomer(email, 29);
        customerJDBCDataAccessService.insertCustomer(customer);
        Integer id = customerJDBCDataAccessService
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email)).map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // Update the customer age
       Integer newAge = 49;
        // Update Customer
        Customer update = new Customer();
        update.setId(id);
        update.setAge(newAge);
        customerJDBCDataAccessService.updateCustomer(update);
        // Then
        Optional<Customer>actual = customerJDBCDataAccessService.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(newAge);
        });
    }

    @Test
    void willUpdateAllPropertiesCustomer() {
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Integer age = 29;
        Customer customer = generateCustomer(email, 29);
        customerJDBCDataAccessService.insertCustomer(customer);
        Integer id = customerJDBCDataAccessService
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email)).map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // Update the customer age / email
        Integer newAge = 49;
        String newEmail = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        String newName = "foo";
        // Update Customer
        Customer update = new Customer();
        update.setId(id);
        update.setAge(newAge);
        update.setName(newName);
        update.setEmail(newEmail);
        customerJDBCDataAccessService.updateCustomer(update);
        // Then
        Optional<Customer>actual = customerJDBCDataAccessService.selectCustomerById(id);
        assertThat(actual).isPresent().hasValue(update);
    }

    @Test
    void willNotUpdateWhenNothingToUpdate() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        customerJDBCDataAccessService.insertCustomer(customer);
        int id = customerJDBCDataAccessService.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When update without no changes
        Customer update = new Customer();
        update.setId(id);
        customerJDBCDataAccessService.updateCustomer(update);
        // Then
        Optional<Customer> actual = customerJDBCDataAccessService.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
        });
    }
    // Helper Method
    private Customer generateCustomer() {
        // Given
        Random r = new Random();
        int low = 18, high = 60;
        Integer age = r.nextInt( high - low) + low ;
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName() + "-" + UUID.randomUUID();
        return new Customer(name, email, age);
    }
    private Customer generateCustomer(String email) {
        // Given
        Random r = new Random();
        int low = 18, high = 60;
        Integer age = r.nextInt( high - low) + low ;
        String name = FAKER.name().fullName() + "-" + UUID.randomUUID();
        return new Customer(name, email, age);
    }

    private Customer generateCustomer(String email, Integer age) {
        // Given
        String name = FAKER.name().fullName() + "-" + UUID.randomUUID();
        return new Customer(name, email, age);
    }
    private Customer generateCustomer(Integer age) {
        // Given
        String name = FAKER.name().fullName() + "-" + UUID.randomUUID();
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        return new Customer(name, email, age);
    }
}