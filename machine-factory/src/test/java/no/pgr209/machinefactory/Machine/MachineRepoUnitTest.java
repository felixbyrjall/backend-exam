package no.pgr209.machinefactory.Machine;

import no.pgr209.machinefactory.model.*;
import no.pgr209.machinefactory.repo.MachineRepo;
import no.pgr209.machinefactory.repo.SubassemblyRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("dev") // Exclude CommandLineRunner from Unit test, ensuring clean database
public class MachineRepoUnitTest {

    @Autowired
    MachineRepo machineRepo;

    @Autowired
    SubassemblyRepo subassemblyRepo;

    @Test // Ensure a machine is created
    public void save_shouldReturnMachine() {
        Machine machine = machineRepo.save(new Machine());

        assertThat(machine).isNotNull();
        assertThat(machine.getMachineId()).isNotNull();
    }

    @Test // Test many-to-many relationship with Subassembly
    public void save_shouldReturnMachineWithSubassemblies() {
        subassemblyRepo.save(new Subassembly("Switches"));
        subassemblyRepo.save(new Subassembly("Fasteners"));
        List<Subassembly> subassemblies = subassemblyRepo.findAll();

        Machine machine = machineRepo.save(new Machine());
        machine.setSubassemblies(subassemblies);

        Optional<Machine> findMachine = machineRepo.findById(machine.getMachineId());
        findMachine.ifPresent(checkMachine -> assertEquals(subassemblies, findMachine.get().getSubassemblies()));
    }

    @Test // Test findAll and ensure count of machines
    public void findAll_shouldReturnNonEmptyListOfMachines() {
        machineRepo.save(new Machine());
        machineRepo.save(new Machine());

        List<Machine> machines = machineRepo.findAll();

        assertThat(machines).isNotNull();
        assertThat(machines.size()).isEqualTo(2);
    }

    @Test // Test finding a machine by id
    public void findById_shouldReturnMachine() {
        Machine machine = machineRepo.save(new Machine());

        Optional<Machine> foundMachine = machineRepo.findById(machine.getMachineId());
        assertThat(foundMachine).isPresent();
    }

    @Test // Test finding a non-existent machine
    public void findById_shouldNotReturnNonExistentMachine() {
        Long nonExistentMachine = 3341L;

        Optional<Machine> findMachine = machineRepo.findById(nonExistentMachine);

        assertThat(findMachine).isNotPresent();
    }

    @Test // Create and then update a machine
    public void update_shouldUpdateExistingMachine() {

        // Create Machine with information
        Machine machine = machineRepo.save(new Machine("Soldering Robot",  "Assembly"));

        Optional<Machine> createdMachine = machineRepo.findById(machine.getMachineId());
        createdMachine.ifPresent(machineMade -> assertEquals("Assembly", createdMachine.get().getMachineType()));

        // Update machine type
        machine.setMachineType("Electronics");

        // Check machine type
        Optional<Machine> machineUpdated = machineRepo.findById(machine.getMachineId());
        machineUpdated.ifPresent(machineChanged -> assertEquals("Electronics", machineUpdated.get().getMachineType()));
    }

    @Test // Create a machine, check if the machine exist, delete the machine and then check if machine still exist
    public void deleteById_shouldRemoveMachine() {
        Machine machine = machineRepo.save(new Machine());
        Optional<Machine> findMachine = machineRepo.findById(machine.getMachineId());

        assertThat(findMachine).isPresent();

        machineRepo.deleteById(machine.getMachineId());

        Optional<Machine> findDeletedMachine = machineRepo.findById(machine.getMachineId());
        assertThat(findDeletedMachine).isNotPresent();
    }
}
