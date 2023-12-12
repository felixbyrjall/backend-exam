package no.pgr209.machinefactory.Subassembly;

import no.pgr209.machinefactory.model.Machine;
import no.pgr209.machinefactory.model.Part;
import no.pgr209.machinefactory.model.Subassembly;
import no.pgr209.machinefactory.repo.PartRepo;
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
public class SubassemblyRepoUnitTest {

    @Autowired
    SubassemblyRepo subassemblyRepo;

    @Autowired
    PartRepo partRepo;

    @Test
    public void save_shouldReturnSubassembly() {
        Subassembly subassembly = new Subassembly();
        Subassembly savedSubassembly = subassemblyRepo.save(subassembly);

        assertThat(savedSubassembly).isNotNull();
        assertThat(savedSubassembly.getSubassemblyId()).isNotNull();
    }

    @Test // Test many-to-many relationship with Subassembly
    public void save_shouldReturnSavedSubassemblyWithParts() {
        Part partOne = partRepo.save(new Part("Printer nozzle"));
        Part partTwo = partRepo.save(new Part("Printer tag"));
        List<Part> allParts = Arrays.asList(partOne, partTwo);

        Subassembly createSubassembly = new Subassembly();
        createSubassembly.setParts(allParts);
        Subassembly savedSubassembly = subassemblyRepo.save(createSubassembly);

        Optional<Subassembly> findSubassembly = subassemblyRepo.findById(savedSubassembly.getSubassemblyId());
        findSubassembly.ifPresent(subassembly -> assertEquals(allParts, findSubassembly.get().getParts()));
    }

    @Test
    public void findAll_shouldReturnNonEmptyListOfMachines() {
        Subassembly subassemblyOne = new Subassembly();
        Subassembly subassemblyTwo = new Subassembly();
        subassemblyRepo.save(subassemblyOne);
        subassemblyRepo.save(subassemblyTwo);

        List<Subassembly> subassemblies = subassemblyRepo.findAll();

        assertThat(subassemblies).isNotNull();
        assertThat(subassemblies.size()).isGreaterThan(0);
    }

    @Test // Test fetching subassembly by id
    public void findById_shouldReturnSubassembly() {
        Subassembly subassembly = subassemblyRepo.save(new Subassembly());

        Optional<Subassembly> foundSubassembly = subassemblyRepo.findById(subassembly.getSubassemblyId());

        assertThat(foundSubassembly).isPresent();
    }

    @Test // Test fetching a non-existent subassembly
    public void findById_shouldNotReturnNonExistentSubassembly() {
        Long nonExistentSubassembly = 23214L;

        Optional<Subassembly> findSubassembly = subassemblyRepo.findById(nonExistentSubassembly);

        assertThat(findSubassembly).isNotPresent();
    }

    @Test // Create Subassembly, update the Subassembly name and check if Subassembly name is updated.
    public void update_shouldUpdateExistingMachine() {

        // Create Subassembly with information
        Subassembly subassembly = subassemblyRepo.save(new Subassembly("Printer head"));

        Optional<Subassembly> createdSubassembly = subassemblyRepo.findById(subassembly.getSubassemblyId());
        createdSubassembly.ifPresent(subassemblyMade -> assertEquals("Printer head", createdSubassembly.get().getSubassemblyName()));

        // Update Subassembly name
        subassembly.setSubassemblyName("Printer tags");
        subassemblyRepo.save(subassembly);

        Optional<Subassembly> subassemblyUpdated = subassemblyRepo.findById(subassembly.getSubassemblyId());
        subassemblyUpdated.ifPresent(subassemblyChanged -> assertEquals("Printer tags", subassemblyUpdated.get().getSubassemblyName()));
    }

    @Test // Create a Subassembly, check if the Subassembly exist, delete the Subassembly and then check if Subassembly still exist.
    public void deleteById_shouldRemoveSubassembly() {
        Subassembly subassembly = subassemblyRepo.save(new Subassembly());
        Optional<Subassembly> findSubassembly = subassemblyRepo.findById(subassembly.getSubassemblyId());

        assertThat(findSubassembly).isPresent();

        subassemblyRepo.deleteById(subassembly.getSubassemblyId());
        Optional<Subassembly> findDeletedSubassembly = subassemblyRepo.findById(subassembly.getSubassemblyId());
        assertThat(findDeletedSubassembly).isNotPresent();
    }

}
