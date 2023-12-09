package no.pgr209.machinefactory.Order;

import no.pgr209.machinefactory.repo.*;
import no.pgr209.machinefactory.model.*;
import no.pgr209.machinefactory.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test") // Seperate CommandLine and Testing.
@Transactional
public class OrderServiceIntegrationTest {

    @Autowired
    OrderService orderService;

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    MachineRepo machineRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    CustomerRepo customerRepo;

    @BeforeEach
    void setUp() {
        orderRepo.deleteAll();
        customerRepo.deleteAll();
        addressRepo.deleteAll();
        machineRepo.deleteAll();
    }

    @Test
    void shouldFetchOrders(){
        Customer customer = customerRepo.save(new Customer("James Jameson", "James@jameson.com"));
        Address address = addressRepo.save(new Address("Karihaugsveien 78", "Skjetten", 2013));
        OrderDTO order = new OrderDTO();

        List<Long> machines = new ArrayList<>();
        var FirstMachine = machineRepo.save(new Machine("3D Printer", "Electronics"));
        var SecondMachine = machineRepo.save(new Machine("Speaker", "Electronics"));

        machines.add(FirstMachine.getMachineId());
        machines.add(SecondMachine.getMachineId());

        order.setAddressId(address.getAddressId());
        order.setCustomerId(customer.getCustomerId());
        order.setMachineId(machines);
        order.setOrderDate(LocalDateTime.now());
        orderService.createOrder(order);

        var orders = orderService.getAllOrders();

        assertEquals(1, orders.size());

        assertEquals("James Jameson", orders.get(0).getCustomer().getCustomerName());
        assertEquals("James@jameson.com", orders.get(0).getCustomer().getCustomerEmail());

        assertEquals("Skjetten", orders.get(0).getAddress().getAddressCity());
        assertEquals("Karihaugsveien 78", orders.get(0).getAddress().getAddressStreet());
        assertEquals(2013, orders.get(0).getAddress().getAddressZip());

        assertEquals("3D Printer", orders.get(0).getMachines().get(0).getMachineName());
        assertEquals("Electronics", orders.get(0).getMachines().get(0).getMachineType());

        assertEquals("Speaker", orders.get(0).getMachines().get(1).getMachineName());
        assertEquals("Electronics", orders.get(0).getMachines().get(1).getMachineType());
    }
}
