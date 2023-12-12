package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.Address;
import no.pgr209.machinefactory.model.Customer;
import no.pgr209.machinefactory.model.CustomerDTO;
import no.pgr209.machinefactory.repo.AddressRepo;
import no.pgr209.machinefactory.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepo customerRepo;

    private final AddressRepo addressRepo;

    @Autowired
    public CustomerService(CustomerRepo customerRepo, AddressRepo addressRepo) {
        this.customerRepo = customerRepo;
        this.addressRepo = addressRepo;
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

    public Customer createCustomer(CustomerDTO customerDTO) {
        if (customerDTO.getCustomerName() == null || customerDTO.getCustomerName().isEmpty() ||
                customerDTO.getCustomerEmail() == null || customerDTO.getCustomerEmail().isEmpty() ||
                customerDTO.getAddressId() == null || !customerDTO.getAddressId().stream().allMatch(addressRepo::existsById)) {
            return null;
        }

        Customer newCustomer = new Customer();
        newCustomer.setCustomerName(customerDTO.getCustomerName());
        newCustomer.setCustomerEmail(customerDTO.getCustomerEmail());
        newCustomer.setAddresses(addressRepo.findAllById(customerDTO.getAddressId()));

        return customerRepo.save(newCustomer);
    }

    public void deleteCustomerById(Long id) {
        customerRepo.deleteById(id);
    }

    public boolean customerExists(Long id) {
        return customerRepo.existsById(id);
    }

    public Customer updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer existingCustomer = customerRepo.findById(id).orElse(null);

        if (existingCustomer == null ||
                customerDTO.getCustomerName() == null || customerDTO.getCustomerName().isEmpty() ||
                customerDTO.getCustomerEmail() == null || customerDTO.getCustomerEmail().isEmpty() ||
                customerDTO.getAddressId() == null) {
            return null;
        }

        String newCustomerName = customerDTO.getCustomerName();
        String newCustomerEmail = customerDTO.getCustomerEmail();
        List<Long> addressIds = customerDTO.getAddressId();

        existingCustomer.setCustomerName(newCustomerName);
        existingCustomer.setCustomerEmail(newCustomerEmail);

        if (!addressIds.isEmpty()) {
            List<Address> addresses = addressRepo.findAllById(addressIds);

            if (addresses.size() != addressIds.size()) {
                return null;
            }

            existingCustomer.setAddresses(addresses);
        } else {
            existingCustomer.setAddresses(Collections.emptyList());
        }

        return customerRepo.save(existingCustomer);
    }
}
