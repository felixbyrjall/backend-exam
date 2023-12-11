package no.pgr209.machinefactory.controller;

import no.pgr209.machinefactory.model.Address;
import no.pgr209.machinefactory.model.AddressDTO;
import no.pgr209.machinefactory.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    //Get all addresses
    @GetMapping()
    public ResponseEntity<List<Address>> getAllAddresses() {
        List<Address> allAddresses = addressService.getAllAddresses();

        if(!allAddresses.isEmpty()){
            return new ResponseEntity<>(allAddresses, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Get addresses by page
    @GetMapping("/page/{pageNr}")
    public ResponseEntity<List<Address>> getAddressesByPage(@PathVariable int pageNr) {
        List<Address> addressesByPage = addressService.getAddressesByPage(pageNr);

        if(!addressesByPage.isEmpty()) {
            return new ResponseEntity<>(addressesByPage, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Long id) {
        Address addressById = addressService.getAddressById(id);

        if(addressById != null) {
            return new ResponseEntity<>(addressById, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody AddressDTO addressDTO) {
        Address createdAddress = addressService.createAddress(addressDTO);

        if(createdAddress != null) {
            return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
        } else {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Error", "One of more fields are invalid");
            return new ResponseEntity<>(responseHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddressById(@PathVariable Long id) {
        if (addressService.addressExists(id)) {
            addressService.deleteAddressById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long id, @RequestBody AddressDTO addressDTO) {
        Address updatedAddress = addressService.updateAddress(id, addressDTO);

        if(updatedAddress != null && updatedAddress.getAddressCity() != null && updatedAddress.getAddressStreet() != null && updatedAddress.getAddressZip() != null) {
            return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
