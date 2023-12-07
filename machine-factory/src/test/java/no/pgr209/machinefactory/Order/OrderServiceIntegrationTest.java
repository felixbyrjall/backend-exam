package no.pgr209.machinefactory.Order;

import no.pgr209.machinefactory.repo.*;
import no.pgr209.machinefactory.model.*;
import no.pgr209.machinefactory.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test") // Seperate CommandLine and Testing.
public class OrderServiceIntegrationTest {

    @Autowired
    OrderService orderService;

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Test
    @Transactional
    void shouldFetchOrders(){
        var order = new Order(LocalDateTime.now());

        Customer customer = customerRepo.save(new Customer("James Jameson", "James@jameson.com"));
        Address address = addressRepo.save(new Address("Karihaugsveien 78", "Skjetten", 2013));

        List<Machine> machines = new ArrayList<>();
        var FirstMachine = new Machine("3D Printer", "Electronics");
        var SecondMachine = new Machine("Speaker", "Electronics");

        machines.add(FirstMachine);
        machines.add(SecondMachine);

        order.setAddress(address);
        order.setCustomer(customer);
        order.setMachines(machines);
        orderService.createOrder(order);

        var ordersOnAllPages = orderService.getOrders(Pageable.unpaged());
        List<Order> orders = ordersOnAllPages.getContent();

        assert orders.size() == 1; // Test count
        assert orders.get(0).getCustomer().getCustomerName().equals("James Jameson"); // Test Customer
        assert orders.get(0).getCustomer().getCustomerEmail().equals("James@jameson.com");
        assert orders.get(0).getAddress().getAddressCity().equals("Skjetten"); // Test address
        assert orders.get(0).getAddress().getAddressStreet().equals("Karihaugsveien 78");
        assert orders.get(0).getAddress().getAddressZip().equals(2013);
        assert orders.get(0).getMachines().get(0).getMachineName().equals("3D Printer"); // Test machine
        assert orders.get(0).getMachines().get(0).getMachineType().equals("Electronics");
        assert orders.get(0).getMachines().get(1).getMachineName().equals("Speaker"); // Test second index machine
        assert orders.get(0).getMachines().get(1).getMachineType().equals("Electronics");
    }
}
