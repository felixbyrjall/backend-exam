package no.pgr209.machinefactory.controller;

import no.pgr209.machinefactory.model.Customer;
import no.pgr209.machinefactory.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> allCustomers = customerService.getAllCustomers();

        if(!allCustomers.isEmpty()) {
            return new ResponseEntity<>(allCustomers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Get customers by page
    @GetMapping("/page/{pageNr}")
    public ResponseEntity<List<Customer>> getCustomersByPage(@PathVariable int pageNr) {
        List<Customer> customersByPage = customerService.getCustomersByPage(pageNr);

        if(!customersByPage.isEmpty()) {
            return new ResponseEntity<>(customersByPage, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
