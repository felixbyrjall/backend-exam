package no.pgr209.machinefactory.controller;

import no.pgr209.machinefactory.model.Machine;
import no.pgr209.machinefactory.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Machine>> getAllMachines() {
        List<Machine> allMachines = machineService.getAllMachines();

        if(!allMachines.isEmpty()){
            return new ResponseEntity<>(allMachines, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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

    @PutMapping("/{id}")
    public ResponseEntity<Machine> updateMachine(@PathVariable Long id, @RequestBody Machine updatedMachine) {
        return machineService.updateMachine(id, updatedMachine);
    }
}
