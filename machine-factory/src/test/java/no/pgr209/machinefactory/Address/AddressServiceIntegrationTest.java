package no.pgr209.machinefactory.Address;

import no.pgr209.machinefactory.model.AddressDTO;
import no.pgr209.machinefactory.model.Customer;
import no.pgr209.machinefactory.repo.AddressRepo;
import no.pgr209.machinefactory.repo.CustomerRepo;
import no.pgr209.machinefactory.repo.MachineRepo;
import no.pgr209.machinefactory.repo.OrderRepo;
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

    @Test // Comprehensive testing - The full cycle of creating an address and then validate information.
    void shouldCreateAndFetchAddressInformation() {
        AddressDTO address = new AddressDTO();
        Customer customerOne = customerRepo.save(new Customer("Ola Nordmann", "ola@nordmann.no"));
        Customer customerTwo = customerRepo.save(new Customer("Kari Hansen", "kari@hansen.no"));

        List<Long> customers = new ArrayList<>();
        customers.add(customerOne.getCustomerId());
        customers.add(customerTwo.getCustomerId());

        address.setAddressStreet("Hausmanns gate 17");
        address.setAddressCity("Oslo");
        address.setAddressZip("0598");
        address.setCustomerId(customers);
        addressService.createAddress(address);

        var addresses = addressService.getAllAddresses();

        assertEquals(1, addresses.size());
        assertEquals("Hausmanns gate 17", addresses.get(0).getAddressStreet());
        assertEquals("Oslo", addresses.get(0).getAddressCity());
        assertEquals("0598", addresses.get(0).getAddressZip());
        assertEquals("Ola Nordmann", addresses.get(0).getCustomers().get(0).getCustomerName());
        assertEquals("ola@nordmann.no", addresses.get(0).getCustomers().get(0).getCustomerEmail());
        assertEquals("Kari Hansen", addresses.get(0).getCustomers().get(1).getCustomerName());
        assertEquals("kari@hansen.no", addresses.get(0).getCustomers().get(1).getCustomerEmail());
    }
}
