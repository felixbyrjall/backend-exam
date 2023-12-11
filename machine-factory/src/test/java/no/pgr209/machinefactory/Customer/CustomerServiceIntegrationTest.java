package no.pgr209.machinefactory.Customer;

import no.pgr209.machinefactory.model.Address;
import no.pgr209.machinefactory.model.CustomerDTO;
import no.pgr209.machinefactory.repo.*;
import no.pgr209.machinefactory.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class CustomerServiceIntegrationTest {

    @Autowired
    CustomerService customerService;

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    CustomerRepo customerRepo;

    @BeforeEach // Ensure clean DB.
    void setUp() {
        customerRepo.deleteAll();
        addressRepo.deleteAll();
    }

    @Test // Comprehensive testing - The full cycle of creating a customer and then validate information.
    void shouldCreateAndFetchCustomers() {
        CustomerDTO customer = new CustomerDTO();
        Address addressOne = addressRepo.save(new Address("Hans Petters Gate 13", "Oslo", 1342));
        Address addressTwo = addressRepo.save(new Address("Peters vei 131", "Oslo", 1324));

        List<Long> addresses = new ArrayList<>();
        addresses.add(addressOne.getAddressId());
        addresses.add(addressTwo.getAddressId());

        customer.setCustomerName("James Jameson");
        customer.setCustomerEmail("james@jameson.com");
        customer.setAddressId(addresses);
        customerService.createCustomer(customer);

        var customers = customerService.getAllCustomers();

        assertEquals(1, customers.size());
        assertEquals("James Jameson", customers.get(0).getCustomerName());
        assertEquals("james@jameson.com", customers.get(0).getCustomerEmail());
        assertEquals("Hans Petters Gate 13", customers.get(0).getAddresses().get(0).getAddressStreet());
        assertEquals("Oslo", customers.get(0).getAddresses().get(0).getAddressCity());
        assertEquals(1342, customers.get(0).getAddresses().get(0).getAddressZip());
        assertEquals("Peters vei 131", customers.get(0).getAddresses().get(1).getAddressStreet());
        assertEquals("Oslo", customers.get(0).getAddresses().get(1).getAddressCity());
        assertEquals(1324, customers.get(0).getAddresses().get(1).getAddressZip());
    }
}
