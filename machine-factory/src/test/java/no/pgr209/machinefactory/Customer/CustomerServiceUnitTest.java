package no.pgr209.machinefactory.Customer;

import static org.junit.jupiter.api.Assertions.assertEquals;


import no.pgr209.machinefactory.model.Customer;
import no.pgr209.machinefactory.repo.CustomerRepo;
import no.pgr209.machinefactory.service.CustomerService;
import no.pgr209.machinefactory.service.DataFeedService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CustomerServiceUnitTest {

    @Autowired
    DataFeedService dataFeedService;

    @Mock
    private CustomerRepo customerRepo;

    @InjectMocks
    private CustomerService customerService;

    @Test // Mock and test fetching all customers
    void shouldReturnAllCustomers() {
        List<Customer> mockCustomers = new ArrayList<>();
        when(customerRepo.findAll()).thenReturn(mockCustomers);
        List<Customer> customers = customerService.getAllCustomers();

        assertEquals(mockCustomers, customers);
    }

}
