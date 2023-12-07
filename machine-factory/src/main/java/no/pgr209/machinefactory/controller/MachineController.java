package no.pgr209.machinefactory.controller;

import no.pgr209.machinefactory.model.Machine;
import no.pgr209.machinefactory.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/machine")
public class MachineController {
    private final MachineService machineService;

    @Autowired
    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }

    //Get all machines
    @GetMapping()
    public List<Machine> getAllMachines() {
        return machineService.getAllMachines();
    }

    //Get machines by page
    @GetMapping("/page/{pageNr}")
    public List<Machine> getMachinesByPage(@PathVariable int pageNr) {
        return machineService.getMachinesByPage(pageNr);
    }

    @GetMapping("/{id}")
    public Machine getMachineById(@PathVariable Long id) {
        return machineService.getMachineById(id);
    }

    @PostMapping
    public Machine createMachine(Machine machine) {
        return machineService.createMachine(machine);
    }

    @DeleteMapping("/{id}")
    public void deleteMachineById(@PathVariable Long id) {
        machineService.deleteMachineById(id);
    }
}
