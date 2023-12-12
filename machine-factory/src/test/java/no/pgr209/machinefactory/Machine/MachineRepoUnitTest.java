package no.pgr209.machinefactory.Machine;

import no.pgr209.machinefactory.model.*;
import no.pgr209.machinefactory.repo.MachineRepo;
import no.pgr209.machinefactory.repo.SubassemblyRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.crypto.Mac;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("dev")
public class MachineRepoUnitTest {

    @Autowired
    MachineRepo machineRepo;

    @Autowired
    SubassemblyRepo subassemblyRepo;

    @Test
    public void save_shouldReturnMachine() {
        Machine machine = new Machine();
        Machine savedMachine = machineRepo.save(machine);

        assertThat(savedMachine).isNotNull();
        assertThat(savedMachine.getMachineId()).isNotNull();
    }

    @Test // Test many-to-many relationship with Subassembly
    public void save_shouldReturnSavedMachineWithSubassemblies() {
        Subassembly subassemblyOne = subassemblyRepo.save(new Subassembly("Printer rod"));
        Subassembly subassemblyTwo = subassemblyRepo.save(new Subassembly("Printer knob"));
        List<Subassembly> allSubassemblies = Arrays.asList(subassemblyOne, subassemblyTwo);

        Machine createMachine = new Machine();
        createMachine.setSubassemblies(allSubassemblies);
        Machine savedMachine = machineRepo.save(createMachine);

        Optional<Machine> findMachine = machineRepo.findById(savedMachine.getMachineId());
        findMachine.ifPresent(address -> assertEquals(allSubassemblies, findMachine.get().getSubassemblies()));
    }

    @Test // Test fetching all machines.
    public void findAll_shouldReturnNonEmptyListOfMachines() {
        Machine firstMachine = new Machine();
        Machine secondMachine = new Machine();
        machineRepo.save(firstMachine);
        machineRepo.save(secondMachine);

        List<Machine> machines = machineRepo.findAll();

        assertThat(machines).isNotNull();
        assertThat(machines.size()).isGreaterThan(0);
    }

    @Test // Test fetching machine by id
    public void findById_shouldReturnMachine() {
        Machine machine = machineRepo.save(new Machine());

        Optional<Machine> foundMachine = machineRepo.findById(machine.getMachineId());

        assertThat(foundMachine).isPresent();
    }

    @Test // Test fetching a non-existent machine
    public void findById_shouldNotReturnNonExistentMachine() {
        Long nonExistentMachine = 3341L;

        Optional<Machine> findMachine = machineRepo.findById(nonExistentMachine);

        assertThat(findMachine).isNotPresent();
    }
}
