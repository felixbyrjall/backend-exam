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
        Machine newMachine = new Machine();

        if(machineDTO.getMachineName() == null){
            return null;
        }
        newMachine.setMachineName(machineDTO.getMachineName());

        if(machineDTO.getMachineType() == null){
            return null;
        }
        newMachine.setMachineType(machineDTO.getMachineType());

        List<Long> subassemblyIds = machineDTO.getSubassemblyId();
        if(!subassemblyIds.stream().allMatch(subassemblyRepo::existsById)) {
            return null;
        }
        newMachine.setSubassemblies(subassemblyRepo.findAllById(subassemblyIds));

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

        if(existingMachine != null) {

            if(machineDTO.getMachineName() != null){
                existingMachine.setMachineName(machineDTO.getMachineName());
            }

            if(machineDTO.getMachineType() != null){
                existingMachine.setMachineType(machineDTO.getMachineType());
            }

            List<Subassembly> subassemblies = subassemblyRepo.findAllById(machineDTO.getSubassemblyId());

            if (!machineDTO.getSubassemblyId().isEmpty()) {
                if (subassemblies.size() != machineDTO.getSubassemblyId().size()) {
                    return null;
                }
                existingMachine.setSubassemblies(subassemblies);
            } else {
                existingMachine.setSubassemblies(Collections.emptyList());
            }

            return machineRepo.save(existingMachine);

        } else {
            return null;
        }
    }
}
