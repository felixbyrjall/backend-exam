package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.Machine;
import no.pgr209.machinefactory.repo.MachineRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MachineService {
    private final MachineRepo machineRepo;

    @Autowired
    public MachineService(MachineRepo machineRepo) {
        this.machineRepo = machineRepo;
    }

    public List<Machine> getAllMachines() {
        return machineRepo.findAll();
    }

    public Machine getMachineById(Long id) {
        return machineRepo.findById(id).orElse(null);
    }

    public Machine createMachine(Machine machine) {
        return machineRepo.save(machine);
    }

    public void deleteMachine(Long id) {
        machineRepo.deleteById(id);
    }
}
