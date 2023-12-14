package no.pgr209.machinefactory.Address;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("dev") // Exclude CommandLineRunner from Unit test, ensuring clean database
public class AddressRepoUnitTest {

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    OrderRepo orderRepo;

    @Test
    public void save_shouldReturnAddress() {
        Address address = addressRepo.save(new Address());

        assertThat(address).isNotNull();
        assertThat(address.getAddressId()).isNotNull();
    }

    @Test // Test many-to-many relationship with customer
    public void save_shouldReturnAddressWithCustomer() {
        customerRepo.save(new Customer("James Brown", "james@brown.no"));
        customerRepo.save(new Customer("Tom Hardy", "tom@hardy.no"));
        List<Customer> allCustomers = customerRepo.findAll();

        Address createAddress = new Address();
        createAddress.setCustomers(allCustomers);
        Address savedAddress = addressRepo.save(createAddress);

        Optional<Address> findAddress = addressRepo.findById(savedAddress.getAddressId());
        findAddress.ifPresent(address -> assertEquals(allCustomers, findAddress.get().getCustomers()));
    }

    @Test // Test one-to-many relationship with order
    public void save_shouldReturnAddressWithOrders() {
        orderRepo.save(new Order());
        orderRepo.save(new Order());
        List<Order> allOrders = orderRepo.findAll();

        Address address = addressRepo.save(new Address());
        address.setOrders(allOrders);

        Optional<Address> findAddress = addressRepo.findById(address.getAddressId());
        findAddress.ifPresent(checkAddress -> assertEquals(allOrders, findAddress.get().getOrders()));
    }

    @Test // Test fetching all addresses.
    public void findAll_shouldReturnNonEmptyListOfAddresses() {
        addressRepo.save(new Address());
        addressRepo.save(new Address());

        List<Address> addresses = addressRepo.findAll();

        assertThat(addresses).isNotNull();
        assertThat(addresses.size()).isEqualTo(2);
    }

    @Test // Test fetching address by id
    public void findById_shouldReturnAddress() {
        Address address = addressRepo.save(new Address());

        Optional<Address> foundAddress = addressRepo.findById(address.getAddressId());

        assertThat(foundAddress).isPresent();
    }

    @Test // Test fetching a non-existent address
    public void findById_shouldNotReturnNonExistentAddress() {
        Long nonExistentAddress = 3341L;

        Optional<Address> findAddress = addressRepo.findById(nonExistentAddress);

        assertThat(findAddress).isNotPresent();
    }

    @Test // Create address, update the street and check if street is updated.
    public void update_shouldUpdateExistingAddress() {

        // Create address with information
        Address address = addressRepo.save(new Address("Vollebekk 14", "Oslo", "0139"));

        Optional<Address> createdAddress = addressRepo.findById(address.getAddressId());
        createdAddress.ifPresent(addressMade -> assertEquals("Vollebekk 14", createdAddress.get().getAddressStreet()));

        // Update the street
        address.setAddressStreet("Vollebekk 50");

        // Check address street
        Optional<Address> addressUpdated = addressRepo.findById(address.getAddressId());
        addressUpdated.ifPresent(addressChanged -> assertEquals("Vollebekk 50", addressUpdated.get().getAddressStreet()));
    }

    @Test // Create an address, check if the address exist, delete the address and then check if address still exist.
    public void deleteById_shouldRemoveAddress() {
        Address address = addressRepo.save(new Address());
        Optional<Address> findAddress = addressRepo.findById(address.getAddressId());

        assertThat(findAddress).isPresent();

        addressRepo.deleteById(address.getAddressId());

        Optional<Address> findDeletedAddress = addressRepo.findById(address.getAddressId());
        assertThat(findDeletedAddress).isNotPresent();
    }
}
