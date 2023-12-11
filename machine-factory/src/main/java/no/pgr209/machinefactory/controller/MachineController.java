package no.pgr209.machinefactory.controller;

import no.pgr209.machinefactory.model.Machine;
import no.pgr209.machinefactory.model.MachineDTO;
import no.pgr209.machinefactory.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<List<Machine>> getMachinesByPage(@PathVariable int pageNr) {
        List<Machine> machinesByPage = machineService.getMachinesByPage(pageNr);

        if(!machinesByPage.isEmpty()) {
            return new ResponseEntity<>(machinesByPage, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Machine> getMachineById(@PathVariable Long id) {
        Machine machineById = machineService.getMachineById(id);

        if(machineById != null) {
            return new ResponseEntity<>(machineById, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Machine> createMachine(@RequestBody MachineDTO machineDTO) {
        Machine createdMachine = machineService.createMachine(machineDTO);

        if(createdMachine != null) {
            return new ResponseEntity<>(createdMachine, HttpStatus.CREATED);
        } else {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Error", "One or more fields are invalid");
            return new ResponseEntity<>(responseHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMachineById(@PathVariable Long id) {
        if (machineService.machineExists(id)) {
            machineService.deleteMachineById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Machine> updateMachine(@PathVariable Long id, @RequestBody MachineDTO machineDTO) {
        Machine updatedMachine = machineService.updateMachine(id, machineDTO);

        if(updatedMachine != null && updatedMachine.getMachineName() != null && updatedMachine.getMachineType() != null && updatedMachine.getSubassemblies() != null) {
            return new ResponseEntity<>(updatedMachine, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
