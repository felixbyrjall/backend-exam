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

        // Customer Three
        Customer customerThree = new Customer("Lars Olsen", "lars@olsen.no");
        Address addressThree = new Address("Bakkegata 7", "Bergen", "5015");
        customerThree.getAddresses().add(addressThree);
        customerThree = customerRepo.save(customerThree);
        addressThree = addressRepo.save(addressThree);

        // Customer Four
        Customer customerFour = new Customer("Ingrid Solberg", "ingrid@solberg.no");
        Address addressFour = new Address("Fjellveien 22", "Stavanger", "4021");
        customerFour.getAddresses().add(addressFour);
        customerFour = customerRepo.save(customerFour);
        addressFour = addressRepo.save(addressFour);

        // Customer Five
        Customer customerFive = new Customer("Erik Pedersen", "erik@pedersen.no");
        Address addressFive = new Address("Strandgaten 15", "Trondheim", "7011");
        customerFive.getAddresses().add(addressFive);
        customerFive = customerRepo.save(customerFive);
        addressFive = addressRepo.save(addressFive);

        // Customer Six
        Customer customerSix = new Customer("Mona Johansen", "mona.j@gmail.com");
        Address addressSix = new Address("Strandveien 10", "Bergen", "5020");
        customerSix.getAddresses().add(addressSix);
        customerSix = customerRepo.save(customerSix);
        addressSix = addressRepo.save(addressSix);

        // Customer Seven
        Customer customerSeven = new Customer("Andreas Berg", "andreas.berg@yahoo.com");
        Address addressSeven = new Address("Fossveien 5", "Oslo", "2010");
        customerSeven.getAddresses().add(addressSeven);
        customerSeven = customerRepo.save(customerSeven);
        addressSeven = addressRepo.save(addressSeven);

        // Customer Eight
        Customer customerEight = new Customer("Camilla Larsen", "camilla_l@online.no");
        Address addressEight = new Address("Bakkegata 15", "Stavanger", "4006");
        customerEight.getAddresses().add(addressEight);
        customerEight = customerRepo.save(customerEight);
        addressEight = addressRepo.save(addressEight);

        // Customer Nine
        Customer customerNine = new Customer("Ole Kristiansen", "ole@kristiansenogco.com");
        Address addressNine = new Address("Solsiden 8", "Trondheim", "7030");
        customerNine.getAddresses().add(addressNine);
        customerNine = customerRepo.save(customerNine);
        addressNine = addressRepo.save(addressNine);

        // Customer Ten
        Customer customerTen = new Customer("Ingrid Nygaard", "ingrid@nygaard.no");
        Address addressTen = new Address("Nedre Slottsgate 12", "Oslo", "2015");
        customerTen.getAddresses().add(addressTen);
        customerTen = customerRepo.save(customerTen);
        addressTen = addressRepo.save(addressTen);

        // Initialize Machines
        Machine machineOne = new Machine("3D printer", "Electronics");
        machineOne = machineRepo.save(machineOne);

        Machine machineTwo = new Machine("Laser printer", "Electronics");
        machineTwo = machineRepo.save(machineTwo);

        Machine machineThree = new Machine("Circuit Board Assembler", "Assembly");
        machineThree = machineRepo.save(machineThree);

        Machine machineFour = new Machine("Soldering Robot", "Assembly");
        machineFour = machineRepo.save(machineFour);

        Machine machineFive = new Machine("Microcontroller Programmer", "Electronics");
        machineFive = machineRepo.save(machineFive);

        Machine machineSix = new Machine("Surface Mount Technology Machine", "Electronics");
        machineSix = machineRepo.save(machineSix);

        Machine machineSeven = new Machine("Pick and Place Machine", "Assembly");
        machineSeven = machineRepo.save(machineSeven);

        Machine machineEight = new Machine("PCB Etching Machine", "Electronics");
        machineEight = machineRepo.save(machineEight);

        Machine machineNine = new Machine("Wire Bonding Machine", "Electronics");
        machineNine = machineRepo.save(machineNine);

        Machine machineTen = new Machine("Automated Testing Equipment", "Electronics");
        machineTen = machineRepo.save(machineTen);


        List<Machine> machinesOne = new ArrayList<>();
        machinesOne.add(machineOne);
        machinesOne.add(machineTwo);

        // Initialize Subassembly
        Subassembly subassemblyOne = new Subassembly("Printer head");
        Subassembly subassemblyTwo = new Subassembly("Paper feed");
        Subassembly subassemblyThree = new Subassembly("Cartridge");
        Subassembly subassemblyFour = new Subassembly("Power supply");
        subassemblyOne = subassemblyRepo.save(subassemblyOne);
        subassemblyTwo = subassemblyRepo.save(subassemblyTwo);
        subassemblyThree = subassemblyRepo.save(subassemblyThree);
        subassemblyFour = subassemblyRepo.save(subassemblyFour);

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
        orderTwo.setMachines(machinesOne);

        orderRepo.save(orderOne);
        orderRepo.save(orderTwo);
    }
}
