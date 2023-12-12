package no.pgr209.machinefactory.Address;

import no.pgr209.machinefactory.model.AddressDTO;
import no.pgr209.machinefactory.model.Customer;
import no.pgr209.machinefactory.repo.AddressRepo;
import no.pgr209.machinefactory.repo.CustomerRepo;
import no.pgr209.machinefactory.service.AddressService;
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
public class AddressServiceIntegrationTest {

    @Autowired
    AddressService addressService;

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    CustomerRepo customerRepo;

    @BeforeEach // Ensure clean DB.
    void setUp() {
        customerRepo.deleteAll();
        addressRepo.deleteAll();
    }

    @Test // Comprehensive testing - The full cycle of creating an address and then validate information.
    void shouldCreateAndFetchAddressInformation() {
        AddressDTO address = new AddressDTO();
        Customer customerOne = customerRepo.save(new Customer("James Brown", "james@brown.no"));
        Customer customerTwo = customerRepo.save(new Customer("Tom Hardy", "tom@hardy.no"));

        List<Long> customers = new ArrayList<>();
        customers.add(customerOne.getCustomerId());
        customers.add(customerTwo.getCustomerId());

        address.setAddressStreet("Hegdehaugsveien 21");
        address.setAddressCity("Oslo");
        address.setAddressZip("0143");
        address.setCustomerId(customers);
        addressService.createAddress(address);

        var addresses = addressService.getAllAddresses();

        assertEquals(1, addresses.size());
        assertEquals("Hegdehaugsveien 21", addresses.get(0).getAddressStreet());
        assertEquals("Oslo", addresses.get(0).getAddressCity());
        assertEquals("0143", addresses.get(0).getAddressZip());
        assertEquals("James Brown", addresses.get(0).getCustomers().get(0).getCustomerName());
        assertEquals("james@brown.no", addresses.get(0).getCustomers().get(0).getCustomerEmail());
        assertEquals("Tom Hardy", addresses.get(0).getCustomers().get(1).getCustomerName());
        assertEquals("tom@hardy.no", addresses.get(0).getCustomers().get(1).getCustomerEmail());
    }
}
