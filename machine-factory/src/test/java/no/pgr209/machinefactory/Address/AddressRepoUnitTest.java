package no.pgr209.machinefactory.Address;

import no.pgr209.machinefactory.model.Address;
import no.pgr209.machinefactory.model.Customer;
import no.pgr209.machinefactory.repo.AddressRepo;
import no.pgr209.machinefactory.repo.CustomerRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("dev")
public class AddressRepoUnitTest {

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Test
    public void save_shouldReturnAddress() {
        Address address = new Address();
        Address savedAddress = addressRepo.save(address);

        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getAddressId()).isNotNull();
    }

    @Test // Test many-to-many relationship with customer
    public void save_shouldReturnSavedAddressWithCustomer() {
        Customer customerOne = customerRepo.save(new Customer("James Brown", "james@brown.no"));
        Customer customerTwo = customerRepo.save(new Customer("Tom Hardy", "tom@hardy.no"));
        List<Customer> allAddresses = Arrays.asList(customerOne, customerTwo);

        Address createAddress = new Address();
        createAddress.setCustomers(allAddresses);
        Address savedAddress = addressRepo.save(createAddress);

        Optional<Address> findAddress = addressRepo.findById(savedAddress.getAddressId());
        findAddress.ifPresent(customer -> assertEquals(allAddresses, findAddress.get().getCustomers()));
    }
}
