package no.pgr209.machinefactory.order;

import no.pgr209.machinefactory.repo.*;
import no.pgr209.machinefactory.model.*;
import no.pgr209.machinefactory.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class OrderServiceIntegrationTest {

    @Autowired
    OrderService orderService;

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    CustomerRepo customerRepo;

    private List<Machine> machines = new ArrayList<>();

    @Test
    void shouldFetchOrders(){
        var customer = new Customer("King Henry", "King@henry.com");
        var address = new Address("Tollbugata 78", "Oslo", 22042);
        address = addressRepo.save(address);
        customer = customerRepo.save(customer);
        var order = new Order(LocalDateTime.now());

        var machine = new Machine("3D Printer", "Electronics");
        machines.add(machine);

        order.setAddress(address);
        order.setCustomer(customer);
        order.setMachines(machines);
        orderService.createOrder(order);

        var orders = orderService.getOrders();

        assert orders.size() == 1;
        assert orders.get(0).getCustomer().getCustomerName().equals("King Henry");
        assert orders.get(0).getCustomer().getCustomerName().equals("King Henry");
    }
}
