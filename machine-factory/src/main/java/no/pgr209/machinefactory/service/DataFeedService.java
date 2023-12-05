package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.*;
import no.pgr209.machinefactory.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataFeedService {

    private final AddressRepo addressRepo;
    private final CustomerRepo customerRepo;
    private final MachineRepo machineRepo;
    private final OrderRepo orderRepo;
    private final PartRepo partRepo;
    private final SubassemblyRepo subassemblyRepo;

    public DataFeedService(AddressRepo addressRepo, CustomerRepo customerRepo,
                           MachineRepo machineRepo, OrderRepo orderRepo,
                           PartRepo partRepo, SubassemblyRepo subassemblyRepo) {
        this.addressRepo = addressRepo;
        this.customerRepo = customerRepo;
        this.machineRepo = machineRepo;
        this.orderRepo = orderRepo;
        this.partRepo = partRepo;
        this.subassemblyRepo = subassemblyRepo;
    }

    @Transactional
    public void initializeData() {
        // Initialize First Customer and Address
        Customer customerOne = new Customer("Ola Nordmann", "ola@nordmann.no");
        Address addressOne = new Address("Storgata 33", "Oslo", 2204);
        customerOne.getAddresses().add(addressOne);
        customerOne = customerRepo.save(customerOne);
        addressOne = addressRepo.save(addressOne);


        // Initialize Second Customer and Address
        Customer customerTwo = new Customer("Kari Hansen", "kari@hansen.no");
        Address addressTwo = new Address("Husmannsgate 14", "Oslo", 2232);
        customerTwo.getAddresses().add(addressOne); // Add same address as first customer.
        customerTwo.getAddresses().add(addressTwo); // Add a second address
        customerTwo = customerRepo.save(customerTwo);
        addressTwo = addressRepo.save(addressTwo);

        // Initialize Machines
        Machine machineOne = new Machine("3D printer", "Electronics");
        Machine machineTwo = new Machine("Speaker", "Electronics");
        machineOne = machineRepo.save(machineOne);
        machineTwo = machineRepo.save(machineTwo);

        List<Machine> machines = new ArrayList<>();
        machines.add(machineOne);
        machines.add(machineTwo);

        // Initialize Subassembly
        Subassembly subassembly = new Subassembly("Printer head");
        subassembly = subassemblyRepo.save(subassembly);

        // Add Subassembly to Machine
        machineOne.getSubassemblies().add(subassembly);
        machineTwo.getSubassemblies().add(subassembly);
        machineRepo.save(machineOne);
        machineRepo.save(machineOne);

        // Initialize Part and add to Subassembly
        Part part = new Part("Printer nozzle");
        part = partRepo.save(part);
        subassembly.getParts().add(part);

        // Update Subassembly with the Part
        subassemblyRepo.save(subassembly);

        // Initialize and save Orders
        Order orderOne = new Order(LocalDateTime.now());
        orderOne.setAddress(addressOne);
        orderOne.setCustomer(customerOne);
        orderOne.getMachines().add(machineOne);

        Order orderTwo = new Order(LocalDateTime.now());
        orderTwo.setAddress(addressTwo);
        orderTwo.setCustomer(customerTwo);
        orderTwo.setMachines(machines);

        orderRepo.save(orderOne);
        orderRepo.save(orderTwo);
    }
}
