package no.pgr209.machinefactory.Order;

import no.pgr209.machinefactory.repo.*;
import no.pgr209.machinefactory.model.*;
import no.pgr209.machinefactory.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
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

    @BeforeEach // Ensure clean DB for test environment
    void setUp() {
        orderRepo.deleteAll();
        customerRepo.deleteAll();
        addressRepo.deleteAll();
        machineRepo.deleteAll();
    }

    @Test // Comprehensive testing - The full cycle of creating an order and then validate information
    void shouldCreateAndFetchOrders(){
        Customer customer = customerRepo.save(new Customer("Lars Olsen", "lars@olsen.no"));
        Address address = addressRepo.save(new Address("Bakkegata 7", "Bergen", "5015"));
        OrderDTO order = new OrderDTO();

        List<Long> machines = new ArrayList<>();
        var FirstMachine = machineRepo.save(new Machine("Laser printer", "Electronics"));
        var SecondMachine = machineRepo.save(new Machine("Circuit Board Assembler", "Assembly"));

        machines.add(FirstMachine.getMachineId());
        machines.add(SecondMachine.getMachineId());

        order.setAddressId(address.getAddressId());
        order.setCustomerId(customer.getCustomerId());
        order.setMachineId(machines);
        order.setOrderDate(LocalDateTime.now());
        orderService.createOrder(order);

        var orders = orderService.getAllOrders();

        assertEquals(1, orders.size());

        assertEquals("Lars Olsen", orders.get(0).getCustomer().getCustomerName());
        assertEquals("lars@olsen.no", orders.get(0).getCustomer().getCustomerEmail());

        assertEquals("Bergen", orders.get(0).getAddress().getAddressCity());
        assertEquals("Bakkegata 7", orders.get(0).getAddress().getAddressStreet());
        assertEquals("5015", orders.get(0).getAddress().getAddressZip());

        assertEquals("Laser printer", orders.get(0).getMachines().get(0).getMachineName());
        assertEquals("Electronics", orders.get(0).getMachines().get(0).getMachineType());

        assertEquals("Circuit Board Assembler", orders.get(0).getMachines().get(1).getMachineName());
        assertEquals("Assembly", orders.get(0).getMachines().get(1).getMachineType());
    }
}