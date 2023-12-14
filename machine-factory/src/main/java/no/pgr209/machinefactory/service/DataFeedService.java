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
        // Initialize Customers and Addresses
        // Customer One
        Customer customerOne = customerRepo.save(new Customer("Ola Nordmann", "ola@nordmann.no"));
        Address addressOne = addressRepo.save(new Address("Storgata 33", "Oslo", "0184"));
        customerOne.getAddresses().add(addressOne);

        // Customer Two
        Customer customerTwo = customerRepo.save(new Customer("Kari Hansen", "kari@hansen.no"));
        Address addressTwo = addressRepo.save(new Address("Hausmanns gate 17", "Oslo", "0598"));
        customerTwo.getAddresses().add(addressOne);
        customerTwo.getAddresses().add(addressTwo);

        // Customer Three
        Customer customerThree = customerRepo.save(new Customer("Lars Olsen", "lars@olsen.no"));
        Address addressThree = addressRepo.save(new Address("Bakkegata 7", "Bergen", "5015"));
        customerThree.getAddresses().add(addressThree);

        // Customer Four
        Customer customerFour = customerRepo.save(new Customer("Ingrid Solberg", "ingrid@solberg.no"));
        Address addressFour = addressRepo.save(new Address("Fjellveien 22", "Stavanger", "4021"));
        customerFour.getAddresses().add(addressFour);

        // Customer Five
        Customer customerFive = customerRepo.save(new Customer("Erik Pedersen", "erik@pedersen.no"));
        Address addressFive = addressRepo.save(new Address("Strandgaten 15", "Trondheim", "7011"));
        customerFive.getAddresses().add(addressFive);

        // Customer Six
        Customer customerSix = customerRepo.save(new Customer("Mona Johansen", "mona@gmail.com"));
        Address addressSix = addressRepo.save(new Address("Strandveien 10", "Bergen", "5020"));
        customerSix.getAddresses().add(addressSix);

        // Customer Seven
        Customer customerSeven = customerRepo.save(new Customer("Andreas Berg", "andreas.berg@yahoo.com"));
        Address addressSeven = addressRepo.save(new Address("Fossveien 5", "Oslo", "2010"));
        customerSeven.getAddresses().add(addressSeven);

        // Customer Eight
        Customer customerEight = customerRepo.save(new Customer("Camilla Larsen", "camilla@online.no"));
        Address addressEight = addressRepo.save(new Address("Bakkegata 15", "Stavanger", "4006"));
        customerEight.getAddresses().add(addressEight);

        // Customer Nine
        Customer customerNine = customerRepo.save(new Customer("Ole Kristiansen", "ole@kristiansen.com"));
        Address addressNine = addressRepo.save(new Address("Solsiden 8", "Trondheim", "0157"));
        customerNine.getAddresses().add(addressNine);

        // Customer Ten
        Customer customerTen = customerRepo.save(new Customer("Ingrid Nygaard", "ingrid@nygaard.no"));
        Address addressTen = addressRepo.save(new Address("Nedre Slottsgate 12", "Oslo", "2015"));
        customerTen.getAddresses().add(addressTen);

        // Initialize Machines
        Machine machineOne = machineRepo.save(new Machine("3D printer", "Electronics"));
        Machine machineTwo = machineRepo.save(new Machine("Laser printer", "Electronics"));
        Machine machineThree = machineRepo.save(new Machine("Circuit Board Assembler", "Assembly"));
        Machine machineFour = machineRepo.save(new Machine("Soldering Robot", "Assembly"));
        Machine machineFive = machineRepo.save(new Machine("Microcontroller Programmer", "Electronics"));
        Machine machineSix = machineRepo.save(new Machine("Surface Mount Technology Machine", "Electronics"));
        Machine machineSeven = machineRepo.save(new Machine("Pick and Place Machine", "Assembly"));
        Machine machineEight = machineRepo.save(new Machine("PCB Etching Machine", "Electronics"));
        Machine machineNine = machineRepo.save(new Machine("Wire Bonding Machine", "Electronics"));
        Machine machineTen = machineRepo.save(new Machine("Automated Testing Equipment", "Electronics"));

        // Gather machines in lists for Orders
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
        Subassembly subassemblyOne = subassemblyRepo.save(new Subassembly("Motion Control System"));
        Subassembly subassemblyTwo = subassemblyRepo.save(new Subassembly("Extruder Assembly"));
        Subassembly subassemblyThree = subassemblyRepo.save(new Subassembly("Robotic Arm System"));
        Subassembly subassemblyFour = subassemblyRepo.save(new Subassembly("Laser Scanning Unit"));
        Subassembly subassemblyFive = subassemblyRepo.save(new Subassembly("Toner Cartridge Assembly"));
        Subassembly subassemblySix = subassemblyRepo.save(new Subassembly("Control System Unit"));
        Subassembly subassemblySeven = subassemblyRepo.save(new Subassembly("Component Placement Arm"));
        Subassembly subassemblyEight = subassemblyRepo.save(new Subassembly("Vision Inspection Module"));
        Subassembly subassemblyNine = subassemblyRepo.save(new Subassembly("Conveyor System"));
        Subassembly subassemblyTen = subassemblyRepo.save(new Subassembly("Soldering Iron Unit"));

        // Add Subassemblies to Machines
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

        // Initialize Parts
        Part partOne = partRepo.save(new Part("Fasteners"));
        Part partTwo = partRepo.save(new Part("Microcontroller"));
        Part partThree = partRepo.save(new Part("Sensor Module"));
        Part partFour = partRepo.save(new Part("Power Supply Unit"));
        Part partFive = partRepo.save(new Part("Display Screen"));
        Part partSix = partRepo.save(new Part("Communication Module"));
        Part partSeven = partRepo.save(new Part("Switches"));
        Part partEight = partRepo.save(new Part("LED indicator"));
        Part partNine = partRepo.save(new Part("Cables and connectors"));
        Part partTen = partRepo.save(new Part("Mounting bracket"));

        // Add Parts to Subassemblies
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

        // Initialize Orders with necessary information: addresses, customers and machines
        // Order One
        Order orderOne = orderRepo.save(new Order(LocalDateTime.parse("2023-01-19T08:45:21.00000")));
        orderOne.setAddress(addressOne);
        orderOne.setCustomer(customerOne);
        orderOne.getMachines().addAll(machinesListOne);

        // Order two
        Order orderTwo = orderRepo.save(new Order(LocalDateTime.parse("2023-03-07T14:22:59.00000")));
        orderTwo.setAddress(addressEight);
        orderTwo.setCustomer(customerTwo);
        orderTwo.getMachines().addAll(machinesListTwo);

        // Order Three
        Order orderThree = orderRepo.save(new Order(LocalDateTime.parse("2023-05-15T18:37:44.00000")));
        orderThree.setAddress(addressThree);
        orderThree.setCustomer(customerThree);
        orderThree.getMachines().addAll(machinesListThree);

        // Order Four
        Order orderFour = orderRepo.save(new Order(LocalDateTime.parse("2023-07-02T22:11:33.00000")));
        orderFour.setAddress(addressFour);
        orderFour.setCustomer(customerFour);
        orderFour.getMachines().addAll(machinesListFour);

        // Order Five
        Order orderFive = orderRepo.save(new Order(LocalDateTime.parse("2023-09-11T11:55:27.00000")));
        orderFive.setAddress(addressFive);
        orderFive.setCustomer(customerFive);
        orderFive.getMachines().addAll(machinesListFive);

        // Order Six
        Order orderSix = orderRepo.save(new Order(LocalDateTime.parse("2023-11-23T05:30:15.00000")));
        orderSix.setAddress(addressNine);
        orderSix.setCustomer(customerSix);
        orderSix.getMachines().addAll(machinesListSix);

        // Order Seven
        Order orderSeven = orderRepo.save(new Order(LocalDateTime.parse("2023-12-31T19:48:02.00000")));
        orderSeven.setAddress(addressSeven);
        orderSeven.setCustomer(customerSeven);
        orderSeven.getMachines().addAll(machinesListSeven);

        // Order Eight
        Order orderEight = orderRepo.save(new Order(LocalDateTime.parse("2023-06-04T03:14:58.00000")));
        orderEight.setAddress(addressEight);
        orderEight.setCustomer(customerEight);
        orderEight.getMachines().addAll(machinesListEight);

        // Order Nine
        Order orderNine = orderRepo.save(new Order(LocalDateTime.parse("2023-08-28T21:09:37.00000")));
        orderNine.setAddress(addressTen);
        orderNine.setCustomer(customerNine);
        orderNine.getMachines().addAll(machinesListNine);

        // Order Ten
        Order orderTen = orderRepo.save(new Order(LocalDateTime.parse("2023-02-10T09:26:47.00000")));
        orderTen.setAddress(addressTen);
        orderTen.setCustomer(customerTen);
        orderTen.getMachines().addAll(machinesListTen);
    }
}
