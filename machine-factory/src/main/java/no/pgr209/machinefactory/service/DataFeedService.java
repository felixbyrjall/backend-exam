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
        Address addressOne = new Address("Storgata 33", "Oslo", "2204");
        customerOne.getAddresses().add(addressOne);
        customerOne = customerRepo.save(customerOne);
        addressOne = addressRepo.save(addressOne);

        // Initialize Second Customer and Address
        Customer customerTwo = new Customer("Kari Hansen", "kari@hansen.no");
        Address addressTwo = new Address("Husmannsgate 14", "Oslo", "2232");
        customerTwo.getAddresses().add(addressOne); // Add same address as first customer.
        customerTwo.getAddresses().add(addressTwo); // Add a second address
        customerTwo = customerRepo.save(customerTwo);
        addressTwo = addressRepo.save(addressTwo);

        // Initialize Machines
        Machine machineOne = new Machine("3D printer", "Electronics");
        Machine machineTwo = new Machine("Laser printer", "Electronics");
        machineOne = machineRepo.save(machineOne);
        machineTwo = machineRepo.save(machineTwo);

        List<Machine> machines = new ArrayList<>();
        machines.add(machineOne);
        machines.add(machineTwo);

        // Initialize Subassembly
        Subassembly subassemblyOne = new Subassembly("Printer head");
        Subassembly subassemblyTwo = new Subassembly("Paper feed");
        Subassembly subassemblyThree = new Subassembly("Cartridge");
        Subassembly subassemblyFour = new Subassembly("Power supply");
        subassemblyOne = subassemblyRepo.save(subassemblyOne);
        subassemblyTwo = subassemblyRepo.save(subassemblyTwo);
        subassemblyThree = subassemblyRepo.save(subassemblyThree);
        subassemblyFour = subassemblyRepo.save(subassemblyFour);

        List<Subassembly> subassemblies = new ArrayList<>();
        subassemblies.add(subassemblyOne);
        subassemblies.add(subassemblyTwo);
        subassemblies.add(subassemblyThree);
        subassemblies.add(subassemblyFour);

        // Add Subassembly to Machine
        machineOne.getSubassemblies().add(subassemblyOne);
        machineOne.getSubassemblies().add(subassemblyTwo);
        machineOne.getSubassemblies().add(subassemblyThree);
        machineTwo.getSubassemblies().add(subassemblyOne);
        machineTwo.getSubassemblies().add(subassemblyFour);
        machineRepo.save(machineOne);
        machineRepo.save(machineOne);

        // Initialize Part and add to Subassembly
        Part partOne = new Part("Printer nozzle");
        Part partTwo = new Part("Printer tag");
        Part partThree = new Part("Printer hex socket");
        partOne = partRepo.save(partOne);
        partTwo = partRepo.save(partTwo);
        partThree = partRepo.save(partThree);
        subassemblyOne.getParts().add(partOne);
        subassemblyOne.getParts().add(partTwo);
        subassemblyTwo.getParts().add(partOne);
        subassemblyTwo.getParts().add(partThree);
        subassemblyThree.getParts().add(partOne);
        subassemblyThree.getParts().add(partTwo);
        subassemblyFour.getParts().add(partThree);

        // Update Subassembly with the Part
        subassemblyRepo.save(subassemblyOne);

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
