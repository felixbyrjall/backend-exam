package no.pgr209.machinefactory.controller;

import no.pgr209.machinefactory.model.Address;
import no.pgr209.machinefactory.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Address> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    //Get addresses by page
    @GetMapping("/page/{pageNr}")
    public List<Address> getAddressesByPage(@PathVariable int pageNr) {
        return addressService.getAddressesByPage(pageNr);
    }

    @GetMapping("/{id}")
    public Address getAddressById(@PathVariable Long id) {
        return addressService.getAddressById(id);
    }

    @PostMapping
    public Address createAddress(Address address) {
        return addressService.createAddress(address);
    }

    @DeleteMapping("/{id}")
    public void deleteAddressById(@PathVariable Long id) {
        addressService.deleteAddressById(id);
    }
}
