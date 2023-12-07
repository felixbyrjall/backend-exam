package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.Customer;
import no.pgr209.machinefactory.model.Order;
import no.pgr209.machinefactory.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepo customerRepo;

    @Autowired
    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public Page<Customer> getCustomers(Pageable pageable) {
        return customerRepo.findAll(pageable);
    }

    public Customer getCustomerById(Long id) {
        return customerRepo.findById(id).orElse(null);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepo.save(customer);
    }

    public void deleteCustomerById(Long id) {
        customerRepo.deleteById(id);
    }

    //Add a customer to an order
    public Customer addOrderToCustomer(Order order, Long id) {
        var customer = customerRepo.findById(id).orElseThrow();
        customer.getOrders().add(order);
        return customerRepo.save(customer);
    }
}
