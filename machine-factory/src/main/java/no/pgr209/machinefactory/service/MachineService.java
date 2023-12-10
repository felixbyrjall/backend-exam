package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.Machine;
import no.pgr209.machinefactory.model.MachineDTO;
import no.pgr209.machinefactory.repo.MachineRepo;
import no.pgr209.machinefactory.repo.SubassemblyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MachineService {
    private final MachineRepo machineRepo;
    private final SubassemblyRepo subassemblyRepo;

    @Autowired
    public MachineService(MachineRepo machineRepo, SubassemblyRepo subassemblyRepo) {
        this.machineRepo = machineRepo;
        this.subassemblyRepo = subassemblyRepo;
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

    public void deleteMachineById(Long id) {
        machineRepo.deleteById(id);
    }

    public boolean machineExists(Long id) {
        return machineRepo.existsById(id);
    }

    public ResponseEntity<Machine> updateMachine(Long id, Machine updatedMachine) {
        Machine existingMachine = machineRepo.findById(id).orElse(null);

        if(existingMachine != null) {

            existingMachine.setMachineName(updatedMachine.getMachineName());
            existingMachine.setMachineType(updatedMachine.getMachineType());
            return new ResponseEntity<>(machineRepo.save(existingMachine), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
