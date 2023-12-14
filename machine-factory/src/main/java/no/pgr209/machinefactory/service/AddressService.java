package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.Address;
import no.pgr209.machinefactory.model.AddressDTO;
import no.pgr209.machinefactory.model.Customer;
import no.pgr209.machinefactory.repo.AddressRepo;
import no.pgr209.machinefactory.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AddressService {
    private final AddressRepo addressRepo;
    private final CustomerRepo customerRepo;

    @Autowired
    public AddressService(AddressRepo addressRepo, CustomerRepo customerRepo) {
        this.addressRepo = addressRepo;
        this.customerRepo = customerRepo;
    }

    public List<Address> getAllAddresses() {
        return addressRepo.findAll();
    }

    public List<Address> getAddressesByPage(int pageNr) {
        return addressRepo.findAll(PageRequest.of(pageNr, 3)).stream().toList();
    }

    public Address getAddressById(Long id) {
        return addressRepo.findById(id).orElse(null);
    }

    public Address createAddress(AddressDTO addressDTO) {
        if (addressDTO.getAddressStreet() == null || addressDTO.getAddressStreet().isEmpty() ||
                addressDTO.getAddressCity() == null || addressDTO.getAddressCity().isEmpty() ||
                addressDTO.getAddressZip() == null || addressDTO.getAddressZip().isEmpty() ||
                addressDTO.getCustomerId() == null || !addressDTO.getCustomerId().stream().allMatch(customerRepo::existsById)) {
            return null;
        }

        Address newAddress = new Address();
        newAddress.setAddressStreet(addressDTO.getAddressStreet());
        newAddress.setAddressCity(addressDTO.getAddressCity());
        newAddress.setAddressZip(addressDTO.getAddressZip());
        newAddress.setCustomers(customerRepo.findAllById(addressDTO.getCustomerId()));

        return addressRepo.save(newAddress);
    }

    public void deleteAddressById(Long id) {
        addressRepo.deleteById(id);
    }

    public boolean addressExists(Long id) {
        return addressRepo.existsById(id);
    }

    public Address updateAddress(Long id, AddressDTO addressDTO) {
        Address existingAddress = addressRepo.findById(id).orElse(null);

        if (existingAddress == null ||
                addressDTO.getAddressStreet() == null || addressDTO.getAddressStreet().isEmpty() ||
                addressDTO.getAddressCity() == null || addressDTO.getAddressCity().isEmpty() ||
                addressDTO.getAddressZip() == null || addressDTO.getAddressZip().isEmpty() ||
                addressDTO.getCustomerId() == null) {
            return null;
        }

        existingAddress.setAddressStreet(addressDTO.getAddressStreet());
        existingAddress.setAddressCity(addressDTO.getAddressCity());
        existingAddress.setAddressZip(addressDTO.getAddressZip());

        List<Long> customerIds = addressDTO.getCustomerId();

        if (!customerIds.isEmpty()) {
            List<Customer> customers = customerRepo.findAllById(customerIds);

            if (customers.size() != customerIds.size()) {
                return null;
            }

            existingAddress.setCustomers(customers);
        } else {
            existingAddress.setCustomers(Collections.emptyList());
        }

        return addressRepo.save(existingAddress);
    }
}
