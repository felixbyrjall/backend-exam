package no.pgr209.machinefactory.Customer;

import no.pgr209.machinefactory.model.Address;
import no.pgr209.machinefactory.model.Customer;
import no.pgr209.machinefactory.model.Order;
import no.pgr209.machinefactory.repo.AddressRepo;
import no.pgr209.machinefactory.repo.CustomerRepo;
import no.pgr209.machinefactory.repo.OrderRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("dev") // Exclude CommandLineRunner from Unit test, ensuring clean database
public class CustomerRepoUnitTest {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Test // Test saving, creating a customer in the db.
    public void save_shouldReturnCustomer() {
        Customer customer = customerRepo.save(new Customer());

        assertThat(customer).isNotNull();
        assertThat(customer.getCustomerId()).isNotNull();
    }

    @Test // Test many-to-many relationship with Address
    public void save_shouldReturnCustomerWithAddresses() {
        addressRepo.save(new Address("Kongens Gate 15", "Oslo", "0153"));
        addressRepo.save(new Address("Dronningens Gate 21", "Oslo", "0154"));
        List<Address> allAddresses = addressRepo.findAll();

        Customer customer = customerRepo.save(new Customer());
        customer.setAddresses(allAddresses);

        Optional<Customer> findCustomer = customerRepo.findById(customer.getCustomerId());
        findCustomer.ifPresent(checkCustomer -> assertEquals(allAddresses, findCustomer.get().getAddresses()));
    }

    @Test // Test one-to-many relationship with Order
    public void save_shouldReturnCustomerWithOrders() {
        orderRepo.save(new Order());
        orderRepo.save(new Order());
        List<Order> allOrders = orderRepo.findAll();

        Customer customer = customerRepo.save(new Customer());
        customer.setOrders(allOrders);

        Optional<Customer> findCustomer = customerRepo.findById(customer.getCustomerId());
        findCustomer.ifPresent(checkCustomer -> assertEquals(allOrders, findCustomer.get().getOrders()));
    }

    @Test // Test fetching all customers.
    public void findAll_shouldReturnNonEmptyListOfCustomers() {
        customerRepo.save(new Customer());
        customerRepo.save(new Customer());
        List<Customer> customers = customerRepo.findAll();

        assertThat(customers).isNotNull();
        assertThat(customers.size()).isEqualTo(2);
    }

    @Test // Test fetching customer by id
    public void findById_shouldReturnCustomer() {
        Customer customer = customerRepo.save(new Customer());

        Optional<Customer> foundCustomer = customerRepo.findById(customer.getCustomerId());
        assertThat(foundCustomer).isPresent();
    }

    @Test // Test fetching a non-existent customer
    public void findById_shouldNotReturnNonExistentCustomer() {
        Long nonExistentCustomer = 23413L;

        Optional<Customer> findCustomer = customerRepo.findById(nonExistentCustomer);
        assertThat(findCustomer).isNotPresent();
    }

    @Test // Create and then update a customer
    public void update_shouldUpdateExistingCustomer() {

        // Create a customer with information
        Customer customer = customerRepo.save(new Customer("Olav Hagen", "olav@hagen.no"));

        Optional<Customer> createdCustomer = customerRepo.findById(customer.getCustomerId());
        createdCustomer.ifPresent(customerMade -> assertEquals("Olav Hagen", createdCustomer.get().getCustomerName()));

        // Update the name of the customer
        customer.setCustomerName("Tim Olsen");

        // Check the name after update
        Optional<Customer> CustomerUpdated = customerRepo.findById(customer.getCustomerId());
        CustomerUpdated.ifPresent(customerChanged -> assertEquals("Tim Olsen", CustomerUpdated.get().getCustomerName()));
    }

    @Test // Create a customer, check if customer exist, delete the customer and then check if customer still exist
    public void deleteById_shouldRemoveCustomer() {
        Customer customer = customerRepo.save(new Customer());
        Optional<Customer> findCustomer = customerRepo.findById(customer.getCustomerId());

        assertThat(findCustomer).isPresent();

        customerRepo.deleteById(customer.getCustomerId());

        Optional<Customer> findDeletedCustomer = customerRepo.findById(customer.getCustomerId());
        assertThat(findDeletedCustomer).isNotPresent();
    }
}
