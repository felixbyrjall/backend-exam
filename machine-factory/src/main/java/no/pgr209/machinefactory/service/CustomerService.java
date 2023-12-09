package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.Customer;
import no.pgr209.machinefactory.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public boolean customerExists(Long id) {
        return customerRepo.existsById(id);
    }

    public ResponseEntity<Customer> updateCustomer(Long id, Customer updatedCustomer) {
        Customer existingCustomer = customerRepo.findById(id).orElse(null);

        if(existingCustomer != null) {

            existingCustomer.setCustomerName(updatedCustomer.getCustomerName());
            existingCustomer.setCustomerEmail(updatedCustomer.getCustomerEmail());
            return new ResponseEntity<>(customerRepo.save(existingCustomer), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
