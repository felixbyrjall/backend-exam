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
@ActiveProfiles("dev") // Exclude CommandLineRunner from Unit test, ensuring clean database
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
        customerDTO.setCustomerName("Lars Olsen");
        customerDTO.setCustomerEmail("lars@olsen.no");
        List<Long> addressIds = List.of(1L, 2L);
        customerDTO.setAddressId(addressIds);

        Customer mockCustomer = new Customer("Lars Olsen", "lars@olsen.no");
        Address firstAddress = new Address("Bakkegata 7", "Bergen", "5015");
        Address secondAddress = new Address("Fjellveien 22", "Stavanger", "4021");

        when(customerRepo.existsById(1L)).thenReturn(true);
        when(customerRepo.findById(1L)).thenReturn(Optional.of(mockCustomer));
        when(addressRepo.existsById(any())).thenReturn(true);
        when(addressRepo.findAllById(addressIds)).thenReturn(List.of(firstAddress, secondAddress));

        Customer createdCustomer = new Customer();
        createdCustomer.setCustomerName("Lars Olsen");
        createdCustomer.setCustomerEmail("lars@olsen.no");
        createdCustomer.setAddresses(List.of(firstAddress, secondAddress));
        when(customerRepo.save(any())).thenReturn(createdCustomer);

        Customer resultCustomer = customerService.createCustomer(customerDTO);

        assertThat(resultCustomer).isNotNull();
        assertThat(resultCustomer.getCustomerName()).isEqualTo(mockCustomer.getCustomerName());
        assertThat(resultCustomer.getCustomerEmail()).isEqualTo(mockCustomer.getCustomerEmail());
        assertThat(resultCustomer.getAddresses()).containsExactlyInAnyOrderElementsOf(List.of(firstAddress, secondAddress));
    }

}
