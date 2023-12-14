package no.pgr209.machinefactory.Address;

import no.pgr209.machinefactory.model.Address;
import no.pgr209.machinefactory.model.AddressDTO;
import no.pgr209.machinefactory.model.Customer;
import no.pgr209.machinefactory.repo.AddressRepo;
import no.pgr209.machinefactory.repo.CustomerRepo;
import no.pgr209.machinefactory.service.AddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("dev") // Exclude CommandLineRunner from Unit test, ensuring clean database
public class AddressServiceUnitTest {

    @Autowired
    AddressService addressService;

    @MockBean
    private CustomerRepo customerRepo;

    @MockBean
    private AddressRepo addressRepo;

    @Test // Mock and test fetching all addresses
    void shouldReturnAllAddresses() {
        List<Address> mockAddresses = new ArrayList<>();
        when(addressRepo.findAll()).thenReturn(mockAddresses);
        List<Address> addresses = addressService.getAllAddresses();

        assertEquals(mockAddresses, addresses);
    }

    @Test // Mock and test fetching address by id
    void shouldReturnAddressById() {
        Address mockAddress = new Address();
        when(addressRepo.findById(1L)).thenReturn(Optional.of(mockAddress));
        Address address = addressService.getAddressById(1L);

        assertEquals(mockAddress, address);
    }

    @Test // Comprehensive mock & unit-testing, creating an address.
    void shouldCreateAddress() {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setAddressStreet("Kongens Gate 14");
        addressDTO.setAddressCity("Oslo");
        addressDTO.setAddressZip("0154");
        List<Long> customerIds = List.of(1L, 2L);
        addressDTO.setCustomerId(customerIds);

        Address mockAddress = new Address("Kongens Gate 14", "Oslo", "0154");
        Customer customerOne = new Customer("James Brown", "james@brown.no");
        Customer customerTwo = new Customer("Tom Hardy", "tom@hardy.no");

        when(addressRepo.existsById(1L)).thenReturn(true);
        when(addressRepo.findById(1L)).thenReturn(Optional.of(mockAddress));
        when(customerRepo.existsById(any())).thenReturn(true);
        when(customerRepo.findAllById(customerIds)).thenReturn(List.of(customerOne, customerTwo));

        Address createdAddress = new Address();
        createdAddress.setAddressStreet("Kongens Gate 14");
        createdAddress.setAddressCity("Oslo");
        createdAddress.setAddressZip("0154");
        createdAddress.setCustomers(List.of(customerOne, customerTwo));
        when(addressRepo.save(any())).thenReturn(createdAddress);

        Address resultAddress = addressService.createAddress(addressDTO);

        assertThat(resultAddress).isNotNull();
        assertThat(resultAddress.getAddressStreet()).isEqualTo(mockAddress.getAddressStreet());
        assertThat(resultAddress.getAddressCity()).isEqualTo(mockAddress.getAddressCity());
        assertThat(resultAddress.getAddressZip()).isEqualTo(mockAddress.getAddressZip());
        assertThat(resultAddress.getCustomers()).containsExactlyInAnyOrderElementsOf(List.of(customerOne, customerTwo));
    }
}
