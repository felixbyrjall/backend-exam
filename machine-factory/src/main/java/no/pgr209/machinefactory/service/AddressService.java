package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.Address;
import no.pgr209.machinefactory.model.AddressDTO;
import no.pgr209.machinefactory.repo.AddressRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    private final AddressRepo addressRepo;

    @Autowired
    public AddressService(AddressRepo addressRepo) {
        this.addressRepo = addressRepo;
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

    public Address createAddress(Address address) {
        return addressRepo.save(address);
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

            if(addressDTO.getAddressStreet() != null) {
                existingAddress.setAddressStreet(addressDTO.getAddressStreet());
            }

            if(addressDTO.getAddressCity() != null) {
                existingAddress.setAddressCity(addressDTO.getAddressCity());
            }

            if(addressDTO.getAddressZip() != null) {
                existingAddress.setAddressZip(addressDTO.getAddressZip());
            }

            return addressRepo.save(existingAddress);

        } else {
            return null;
        }
    }
}
