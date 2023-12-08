package no.pgr209.machinefactory.controller;

import no.pgr209.machinefactory.model.Customer;
import no.pgr209.machinefactory.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    //Get all customers
    @GetMapping()
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    //Get customers by page
    @GetMapping("/page/{pageNr}")
    public List<Customer> getCustomersByPage(@PathVariable int pageNr) {
        return customerService.getCustomersByPage(pageNr);
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

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer) {
        return customerService.updateCustomer(id, updatedCustomer);
    }
}
