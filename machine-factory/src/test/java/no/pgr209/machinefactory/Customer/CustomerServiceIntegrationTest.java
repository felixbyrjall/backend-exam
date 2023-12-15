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
    MachineRepo machineRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    CustomerRepo customerRepo;

    @BeforeEach // Ensure clean DB for test environment
    void setUp() {
        orderRepo.deleteAll();
        customerRepo.deleteAll();
        addressRepo.deleteAll();
        machineRepo.deleteAll();
    }

    @Test // Comprehensive testing - The full cycle of creating a customer and then validate information.
    void shouldCreateAndFetchCustomer() {
        CustomerDTO customer = new CustomerDTO();
        Address addressOne = addressRepo.save(new Address("Storgata 33", "Oslo", "0184"));
        Address addressTwo = addressRepo.save(new Address("Hausmanns gate 17", "Oslo", "0598"));

        List<Long> addresses = new ArrayList<>();
        addresses.add(addressOne.getAddressId());
        addresses.add(addressTwo.getAddressId());

        customer.setCustomerName("Ola Nordmann");
        customer.setCustomerEmail("ola@nordmann.no");
        customer.setAddressId(addresses);
        customerService.createCustomer(customer);

        var customers = customerService.getAllCustomers();

        assertEquals(1, customers.size());
        assertEquals("Ola Nordmann", customers.get(0).getCustomerName());
        assertEquals("ola@nordmann.no", customers.get(0).getCustomerEmail());
        assertEquals("Storgata 33", customers.get(0).getAddresses().get(0).getAddressStreet());
        assertEquals("Oslo", customers.get(0).getAddresses().get(0).getAddressCity());
        assertEquals("0184", customers.get(0).getAddresses().get(0).getAddressZip());
        assertEquals("Hausmanns gate 17", customers.get(0).getAddresses().get(1).getAddressStreet());
        assertEquals("Oslo", customers.get(0).getAddresses().get(1).getAddressCity());
        assertEquals("0598", customers.get(0).getAddresses().get(1).getAddressZip());
    }
}
