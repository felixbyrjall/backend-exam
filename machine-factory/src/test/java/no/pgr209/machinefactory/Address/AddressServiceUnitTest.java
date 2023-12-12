package no.pgr209.machinefactory.Address;

import no.pgr209.machinefactory.model.Address;
import no.pgr209.machinefactory.model.Customer;
import no.pgr209.machinefactory.repo.AddressRepo;
import no.pgr209.machinefactory.repo.CustomerRepo;
import no.pgr209.machinefactory.service.AddressService;
import no.pgr209.machinefactory.service.DataFeedService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AddressServiceUnitTest {

    @Autowired
    DataFeedService dataFeedService;

    @Mock
    private CustomerRepo customerRepo;

    @Mock
    private AddressRepo addressRepo;

    @InjectMocks
    private AddressService addressService;

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
}
