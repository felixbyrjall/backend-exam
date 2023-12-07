package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.Customer;
import no.pgr209.machinefactory.model.Order;
import no.pgr209.machinefactory.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepo customerRepo;

    @Autowired
    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    //Get ALL customers
    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    //Get customers by page
    public List<Customer> getCustomersByPage(int pageNr) {
        return customerRepo.findAll(PageRequest.of(pageNr, 10)).stream().toList();
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
