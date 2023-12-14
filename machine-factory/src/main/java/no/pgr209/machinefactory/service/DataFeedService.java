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

        // Creating list of machines, to add to orders further down
        List<Machine> machinesListOne = new ArrayList<>();
        machinesListOne.add(machineOne);
        machinesListOne.add(machineTwo);
        machinesListOne.add(machineFour);

        List<Machine> machinesListTwo = new ArrayList<>();
        machinesListTwo.add(machineThree);

        List<Machine> machinesListThree = new ArrayList<>();
        machinesListThree.add(machineFive);
        machinesListThree.add(machineSix);
        machinesListThree.add(machineOne);
        machinesListThree.add(machineTen);
        machinesListThree.add(machineSeven);

        List<Machine> machinesListFour = new ArrayList<>();
        machinesListFour.add(machineSeven);
        machinesListFour.add(machineFive);

        List<Machine> machinesListFive = new ArrayList<>();
        machinesListFive.add(machineNine);
        machinesListFive.add(machineTen);
        machinesListFive.add(machineEight);

        List<Machine> machinesListSix = new ArrayList<>();
        machinesListSix.add(machineOne);
        machinesListSix.add(machineEight);
        machinesListSix.add(machineFive);

        List<Machine> machinesListSeven = new ArrayList<>();
        machinesListSeven.add(machineFive);
        machinesListSeven.add(machineOne);
        machinesListSeven.add(machineTwo);

        List<Machine> machinesListEight = new ArrayList<>();
        machinesListEight.add(machineTen);

        List<Machine> machinesListNine = new ArrayList<>();
        machinesListNine.add(machineEight);

        List<Machine> machinesListTen = new ArrayList<>();
        machinesListTen.add(machineThree);
        machinesListTen.add(machineTen);

        // Initialize Subassembly
        Subassembly subassemblyOne = new Subassembly("Motion Control System");
        subassemblyOne = subassemblyRepo.save(subassemblyOne);

        Subassembly subassemblyTwo = new Subassembly("Extruder Assembly");
        subassemblyTwo = subassemblyRepo.save(subassemblyTwo);

        Subassembly subassemblyThree = new Subassembly("Robotic Arm System");
        subassemblyThree = subassemblyRepo.save(subassemblyThree);

        Subassembly subassemblyFour = new Subassembly("Laser Scanning Unit");
        subassemblyFour = subassemblyRepo.save(subassemblyFour);

        Subassembly subassemblyFive = new Subassembly("Toner Cartridge Assembly");
        subassemblyFive = subassemblyRepo.save(subassemblyFive);

        Subassembly subassemblySix = new Subassembly("Control System Unit");
        subassemblySix = subassemblyRepo.save(subassemblySix);

        Subassembly subassemblySeven = new Subassembly("Component Placement Arm");
        subassemblySeven = subassemblyRepo.save(subassemblySeven);

        Subassembly subassemblyEight = new Subassembly("Vision Inspection Module");
        subassemblyEight = subassemblyRepo.save(subassemblyEight);

        Subassembly subassemblyNine = new Subassembly("Conveyor System");
        subassemblyNine = subassemblyRepo.save(subassemblyNine);

        Subassembly subassemblyTen = new Subassembly("Soldering Iron Unit");
        subassemblyTen = subassemblyRepo.save(subassemblyTen);

        // Add Subassembly to Machine
        machineOne.getSubassemblies().add(subassemblyOne);
        machineOne.getSubassemblies().add(subassemblyTwo);

        machineTwo.getSubassemblies().add(subassemblyFour);
        machineTwo.getSubassemblies().add(subassemblyFive);
        machineTwo.getSubassemblies().add(subassemblySix);

        machineThree.getSubassemblies().add(subassemblySeven);
        machineThree.getSubassemblies().add(subassemblyEight);
        machineThree.getSubassemblies().add(subassemblyNine);

        machineFour.getSubassemblies().add(subassemblyThree);
        machineFour.getSubassemblies().add(subassemblyTen);
        machineFour.getSubassemblies().add(subassemblyEight);
        machineFour.getSubassemblies().add(subassemblySix);

        machineFive.getSubassemblies().add(subassemblySix);

        machineSix.getSubassemblies().add(subassemblySeven);
        machineSix.getSubassemblies().add(subassemblyEight);
        machineSix.getSubassemblies().add(subassemblyNine);

        machineSeven.getSubassemblies().add(subassemblySeven);
        machineSeven.getSubassemblies().add(subassemblyEight);
        machineSeven.getSubassemblies().add(subassemblyThree);
        machineSeven.getSubassemblies().add(subassemblyNine);
        machineSeven.getSubassemblies().add(subassemblySix);

        machineEight.getSubassemblies().add(subassemblyNine);
        machineEight.getSubassemblies().add(subassemblySix);

        machineNine.getSubassemblies().add(subassemblyThree);
        machineNine.getSubassemblies().add(subassemblySix);

        machineTen.getSubassemblies().add(subassemblySix);
        machineTen.getSubassemblies().add(subassemblyNine);
        machineTen.getSubassemblies().add(subassemblyThree);

        // Save all machines with subassemblies to repo
        machineRepo.save(machineOne);
        machineRepo.save(machineTwo);
        machineRepo.save(machineThree);
        machineRepo.save(machineFour);
        machineRepo.save(machineFive);
        machineRepo.save(machineSix);
        machineRepo.save(machineSeven);
        machineRepo.save(machineEight);
        machineRepo.save(machineNine);
        machineRepo.save(machineTen);

        // Initialize Part and add to Subassembly
        Part partOne = new Part("Fasteners");
        Part partTwo = new Part("Microcontroller");
        Part partThree = new Part("Sensor Module");
        Part partFour = new Part("Power Supply Unit");
        Part partFive = new Part("Display Screen");
        Part partSix = new Part("Communication Module");
        Part partSeven = new Part("Switches");
        Part partEight = new Part("LED indicator");
        Part partNine = new Part("Cables and connectors");
        Part partTen = new Part("Mounting bracket");

        partOne = partRepo.save(partOne);
        partTwo = partRepo.save(partTwo);
        partThree = partRepo.save(partThree);
        partFour = partRepo.save(partFour);
        partFive = partRepo.save(partFive);
        partSix = partRepo.save(partSix);
        partSeven = partRepo.save(partSeven);
        partEight = partRepo.save(partEight);
        partNine = partRepo.save(partNine);
        partTen = partRepo.save(partTen);

        subassemblyOne.getParts().add(partOne);
        subassemblyOne.getParts().add(partFour);

        subassemblyTwo.getParts().add(partOne);
        subassemblyTwo.getParts().add(partTwo);

        subassemblyThree.getParts().add(partTwo);
        subassemblyThree.getParts().add(partThree);
        subassemblyThree.getParts().add(partOne);
        subassemblyThree.getParts().add(partFour);

        subassemblyFour.getParts().add(partThree);
        subassemblyFour.getParts().add(partNine);

        subassemblyFive.getParts().add(partOne);

        subassemblySix.getParts().add(partTwo);
        subassemblySix.getParts().add(partFour);
        subassemblySix.getParts().add(partNine);

        subassemblySeven.getParts().add(partTwo);
        subassemblySeven.getParts().add(partOne);
        subassemblySeven.getParts().add(partSix);

        subassemblyEight.getParts().add(partThree);
        subassemblyEight.getParts().add(partEight);
        subassemblyEight.getParts().add(partTen);
        subassemblyEight.getParts().add(partFive);

        subassemblyNine.getParts().add(partNine);
        subassemblyNine.getParts().add(partFour);
        subassemblyNine.getParts().add(partSeven);

        subassemblyTen.getParts().add(partOne);
        subassemblyTen.getParts().add(partSeven);
        subassemblyTen.getParts().add(partFour);
        subassemblyTen.getParts().add(partFive);

        // Update Subassembly with the Part
        subassemblyRepo.save(subassemblyOne);
        subassemblyRepo.save(subassemblyTwo);
        subassemblyRepo.save(subassemblyThree);
        subassemblyRepo.save(subassemblyFour);
        subassemblyRepo.save(subassemblyFive);
        subassemblyRepo.save(subassemblySix);
        subassemblyRepo.save(subassemblySeven);
        subassemblyRepo.save(subassemblyEight);
        subassemblyRepo.save(subassemblyNine);
        subassemblyRepo.save(subassemblyTen);

        // Initialize and save Orders
        Order orderOne = new Order(LocalDateTime.parse("2023-01-19T08:45:21.123456"));
        orderOne.setAddress(addressOne);
        orderOne.setCustomer(customerOne);
        orderOne.getMachines().addAll(machinesListOne);

        Order orderTwo = new Order(LocalDateTime.parse("2023-03-07T14:22:59.987654"));
        orderTwo.setAddress(addressEight);
        orderTwo.setCustomer(customerTwo);
        orderTwo.getMachines().addAll(machinesListTwo);

        // Order Three
        Order orderThree = new Order(LocalDateTime.parse("2023-05-15T18:37:44.567890"));
        orderThree.setAddress(addressThree);
        orderThree.setCustomer(customerThree);
        orderThree.getMachines().addAll(machinesListThree);

// Order Four
        Order orderFour = new Order(LocalDateTime.parse("2023-07-02T22:11:33.456789"));
        orderFour.setAddress(addressFour);
        orderFour.setCustomer(customerFour);
        orderFour.getMachines().addAll(machinesListFour);

// Order Five
        Order orderFive = new Order(LocalDateTime.parse("2023-09-11T11:55:27.345678"));
        orderFive.setAddress(addressFive);
        orderFive.setCustomer(customerFive);
        orderFive.getMachines().addAll(machinesListFive);

// Order Six
        Order orderSix = new Order(LocalDateTime.parse("2023-11-23T05:30:15.678901"));
        orderSix.setAddress(addressNine);
        orderSix.setCustomer(customerSix);
        orderSix.getMachines().addAll(machinesListSix);

// Order Seven
        Order orderSeven = new Order(LocalDateTime.parse("2023-12-31T19:48:02.234567"));
        orderSeven.setAddress(addressSeven);
        orderSeven.setCustomer(customerSeven);
        orderSeven.getMachines().addAll(machinesListSeven);

// Order Eight
        Order orderEight = new Order(LocalDateTime.parse("2023-06-04T03:14:58.876543"));
        orderEight.setAddress(addressEight);
        orderEight.setCustomer(customerEight);
        orderEight.getMachines().addAll(machinesListEight);

// Order Nine
        Order orderNine = new Order(LocalDateTime.parse("2023-08-28T21:09:37.345678"));
        orderNine.setAddress(addressTen);
        orderNine.setCustomer(customerNine);
        orderNine.getMachines().addAll(machinesListNine);

// Order Ten
        Order orderTen = new Order(LocalDateTime.parse("2023-02-10T09:26:47.901234"));
        orderTen.setAddress(addressTen);
        orderTen.setCustomer(customerTen);
        orderTen.getMachines().addAll(machinesListTen);


        orderRepo.save(orderOne);
        orderRepo.save(orderTwo);
        orderRepo.save(orderThree);
        orderRepo.save(orderFour);
        orderRepo.save(orderFive);
        orderRepo.save(orderSix);
        orderRepo.save(orderSeven);
        orderRepo.save(orderEight);
        orderRepo.save(orderNine);
        orderRepo.save(orderTen);
    }
}
