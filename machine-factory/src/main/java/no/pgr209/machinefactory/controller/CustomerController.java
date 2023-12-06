package no.pgr209.machinefactory.controller;

import no.pgr209.machinefactory.model.Customer;
import no.pgr209.machinefactory.service.CustomerService;
import no.pgr209.machinefactory.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final OrderService orderService;

    @Autowired
    public CustomerController(CustomerService customerService, OrderService orderService) {
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping
    public Customer createCustomer(Customer customer) {
        return customerService.createCustomer(customer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomerById(id);
    }

    @PostMapping("/{id}/{orderId}")
    public Customer addOrderToCustomer(@PathVariable Long id, @PathVariable Long orderId) {
        var customer = customerService.getCustomerById(id);
        var order = orderService.getOrderById(orderId);
        customer.getOrders().add(order);
        return customerService.createCustomer(customer);
    }
}
