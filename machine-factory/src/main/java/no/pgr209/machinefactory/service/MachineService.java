package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.*;
import no.pgr209.machinefactory.repo.MachineRepo;
import no.pgr209.machinefactory.repo.SubassemblyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class MachineService {
    private final MachineRepo machineRepo;
    private final SubassemblyRepo subassemblyRepo;
    private final OrderService orderService;

    @Autowired
    public MachineService(MachineRepo machineRepo, SubassemblyRepo subassemblyRepo, OrderService orderService) {
        this.machineRepo = machineRepo;
        this.subassemblyRepo = subassemblyRepo;
        this.orderService = orderService;
    }

    //Get ALL machines
    public List<Machine> getAllMachines() {
        return machineRepo.findAll();
    }

    //Get machines by page
    public List<Machine> getMachinesByPage(int pageNr) {
        return machineRepo.findAll(PageRequest.of(pageNr, 10)).stream().toList();
    }

    public Machine getMachineById(Long id) {
        return machineRepo.findById(id).orElse(null);
    }

    public Machine createMachine(MachineDTO machineDTO) {
        if (machineDTO.getMachineName() == null || machineDTO.getMachineName().isEmpty() ||
                machineDTO.getMachineType() == null || machineDTO.getMachineType().isEmpty() ||
                machineDTO.getSubassemblyId() == null || !machineDTO.getSubassemblyId().stream().allMatch(subassemblyRepo::existsById)) {
            return null;
        }

        Machine newMachine = new Machine();
        newMachine.setMachineName(machineDTO.getMachineName());
        newMachine.setMachineType(machineDTO.getMachineType());
        newMachine.setSubassemblies(subassemblyRepo.findAllById(machineDTO.getSubassemblyId()));

        return machineRepo.save(newMachine);
    }


    // Delete Orders connected to Machine
    public void deleteMachineById(Long id) {
        List<Order> allOrders = orderService.getAllOrders();
        List<Order> ordersContainingMachine = allOrders.stream()
                .filter(order -> order.getMachines().stream().anyMatch(machine -> machine.getMachineId().equals(id)))
                .toList();

        for (Order order : ordersContainingMachine) { // Delete all orders that contain the machine
            orderService.deleteOrderById(order.getOrderId());
        }

        machineRepo.deleteById(id); // Delete machine after orders are deleted.
    }

    public boolean machineExists(Long id) {
        return machineRepo.existsById(id);
    }

    public Machine updateMachine(Long id, MachineDTO machineDTO) {
        Machine existingMachine = machineRepo.findById(id).orElse(null);

        if (existingMachine == null ||
                machineDTO.getMachineName() == null || machineDTO.getMachineName().isEmpty() ||
                machineDTO.getMachineType() == null || machineDTO.getMachineType().isEmpty() ||
                machineDTO.getSubassemblyId() == null) {
            return null;
        }

        existingMachine.setMachineName(machineDTO.getMachineName());
        existingMachine.setMachineType(machineDTO.getMachineType());

        List<Long> subassemblyIds = machineDTO.getSubassemblyId();

        if (subassemblyIds.isEmpty()) {
            existingMachine.setSubassemblies(Collections.emptyList());
            return machineRepo.save(existingMachine);
        }

        List<Subassembly> subassemblies = subassemblyRepo.findAllById(subassemblyIds);

        if (subassemblies.size() != subassemblyIds.size()) {
            return null;
        }

        existingMachine.setSubassemblies(subassemblies);
        return machineRepo.save(existingMachine);
    }
}
