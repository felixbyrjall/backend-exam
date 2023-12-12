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

    //Get ALL addresses
    public List<Address> getAllAddresses() {
        return addressRepo.findAll();
    }

    //Get addresses by page
    public List<Address> getAddressesByPage(int pageNr) {
        return addressRepo.findAll(PageRequest.of(pageNr, 10)).stream().toList();
    }

    public Address getAddressById(Long id) {
        return addressRepo.findById(id).orElse(null);
    }

    public Address createAddress(AddressDTO addressDTO) {
        Address newAddress = new Address();

        if(addressDTO.getAddressStreet() == null || addressDTO.getAddressStreet().isEmpty()){
            return null;
        }
        newAddress.setAddressStreet(addressDTO.getAddressStreet());

        if(addressDTO.getAddressCity() == null || addressDTO.getAddressCity().isEmpty()){
            return null;
        }
        newAddress.setAddressCity(addressDTO.getAddressCity());

        if(addressDTO.getAddressZip() == null || addressDTO.getAddressZip().isEmpty()){
            return null;
        }
        newAddress.setAddressZip(addressDTO.getAddressZip());

        List<Long> customerIds = addressDTO.getCustomerId();
        if(!customerIds.stream().allMatch(customerRepo::existsById)) {
            return null;
        }
        newAddress.setCustomers(customerRepo.findAllById(customerIds));

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

        if(existingAddress != null) {

            if(addressDTO.getAddressStreet() != null && !addressDTO.getAddressStreet().isEmpty()) {
                existingAddress.setAddressStreet(addressDTO.getAddressStreet());
            } else {
                return null;
            }

            if(addressDTO.getAddressCity() != null && !addressDTO.getAddressCity().isEmpty()) {
                existingAddress.setAddressCity(addressDTO.getAddressCity());
            } else {
                return null;
            }

            if(addressDTO.getAddressZip() != null && !addressDTO.getAddressZip().isEmpty()) {
                existingAddress.setAddressZip(addressDTO.getAddressZip());
            } else {
                return null;
            }

            List<Customer> customers = customerRepo.findAllById(addressDTO.getCustomerId());

            if (!addressDTO.getCustomerId().isEmpty()) {
                if (customers.size() != addressDTO.getCustomerId().size()) {
                    return null;
                }
                existingAddress.setCustomers(customers);
            } else {
                existingAddress.setCustomers(Collections.emptyList());
            }

            return addressRepo.save(existingAddress);

        } else {
            return null;
        }
    }
}
