package no.pgr209.machinefactory.Customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import no.pgr209.machinefactory.model.Address;
import no.pgr209.machinefactory.model.Customer;
import no.pgr209.machinefactory.model.CustomerDTO;
import no.pgr209.machinefactory.repo.AddressRepo;
import no.pgr209.machinefactory.repo.CustomerRepo;
import no.pgr209.machinefactory.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("dev")
public class CustomerServiceUnitTest {

    @Autowired
    CustomerService customerService;

    @MockBean
    private CustomerRepo customerRepo;

    @MockBean
    private AddressRepo addressRepo;

    @Test // Mock and test fetching all customers
    void shouldReturnAllCustomers() {
        List<Customer> mockCustomers = new ArrayList<>();
        when(customerRepo.findAll()).thenReturn(mockCustomers);
        List<Customer> customers = customerService.getAllCustomers();

        assertEquals(mockCustomers, customers);
    }

    @Test // Mock and test fetching customer by id
    void shouldReturnCustomerById() {
        Customer mockCustomer = new Customer();
        when(customerRepo.findById(1L)).thenReturn(Optional.of(mockCustomer));
        Customer customer = customerService.getCustomerById(1L);

        assertEquals(mockCustomer, customer);
    }

    @Test // Comprehensive mock & unit-testing, creating a customer.
    void shouldCreateCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomerName("Tom Hardy");
        customerDTO.setCustomerEmail("tom@hardy.com");
        List<Long> addressIds = List.of(1L, 2L);
        customerDTO.setAddressId(addressIds);

        Customer mockCustomer = new Customer("Tom Hardy", "tom@hardy.com");
        Address firstAddress = new Address("Asker Gate 4", "Asker", "0152");
        Address secondAddress = new Address("Georg Gate 12", "Oslo", "1350");

        when(customerRepo.existsById(1L)).thenReturn(true);
        when(customerRepo.findById(1L)).thenReturn(Optional.of(mockCustomer));
        when(addressRepo.existsById(any())).thenReturn(true);
        when(addressRepo.findAllById(addressIds)).thenReturn(List.of(firstAddress, secondAddress));

        Customer createdCustomer = new Customer();
        createdCustomer.setCustomerName("Tom Hardy");
        createdCustomer.setCustomerEmail("tom@hardy.com");
        createdCustomer.setAddresses(List.of(firstAddress, secondAddress));
        when(customerRepo.save(any())).thenReturn(createdCustomer);

        Customer resultCustomer = customerService.createCustomer(customerDTO);

        assertThat(resultCustomer).isNotNull();
        assertThat(resultCustomer.getCustomerName()).isEqualTo(mockCustomer.getCustomerName());
        assertThat(resultCustomer.getCustomerEmail()).isEqualTo(mockCustomer.getCustomerEmail());
        assertThat(resultCustomer.getAddresses()).containsExactlyInAnyOrderElementsOf(List.of(firstAddress, secondAddress));
    }

}
