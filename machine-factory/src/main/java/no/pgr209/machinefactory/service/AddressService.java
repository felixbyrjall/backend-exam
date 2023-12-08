package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.Address;
import no.pgr209.machinefactory.repo.AddressRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<Address> updateAddress(Long id, Address updatedAddress) {
        Address existingAddress = addressRepo.findById(id).orElse(null);

        if(existingAddress != null) {

            existingAddress.setAddressStreet(updatedAddress.getAddressStreet());
            existingAddress.setAddressCity(updatedAddress.getAddressCity());
            existingAddress.setAddressZip(updatedAddress.getAddressZip());
            return new ResponseEntity<>(addressRepo.save(existingAddress), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
