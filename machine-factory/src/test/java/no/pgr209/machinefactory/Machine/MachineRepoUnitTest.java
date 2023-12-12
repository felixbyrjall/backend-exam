package no.pgr209.machinefactory.Machine;

import no.pgr209.machinefactory.model.*;
import no.pgr209.machinefactory.repo.MachineRepo;
import no.pgr209.machinefactory.repo.SubassemblyRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

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

    @Test // Create machine, update the Machine type and check if Machine type is updated.
    public void update_shouldUpdateExistingMachine() {

        // Create Machine with information
        Machine machine = machineRepo.save(new Machine("Robot Printer",  "Electronics"));

        Optional<Machine> createdMachine = machineRepo.findById(machine.getMachineId());
        createdMachine.ifPresent(machineMade -> assertEquals("Electronics", createdMachine.get().getMachineType()));

        // Update machine type
        machine.setMachineType("Work Equipment");
        machineRepo.save(machine);

        Optional<Machine> machineUpdated = machineRepo.findById(machine.getMachineId());
        machineUpdated.ifPresent(machineChanged -> assertEquals("Work Equipment", machineUpdated.get().getMachineType()));
    }

    @Test // Create a machine, check if the machine exist, delete the machine and then check if machine still exist.
    public void deleteById_shouldRemoveMachine() {
        Machine machine = machineRepo.save(new Machine());
        Optional<Machine> findMachine = machineRepo.findById(machine.getMachineId());

        assertThat(findMachine).isPresent();

        machineRepo.deleteById(machine.getMachineId());
        Optional<Machine> findDeletedMachine = machineRepo.findById(machine.getMachineId());
        assertThat(findDeletedMachine).isNotPresent();
    }
}
