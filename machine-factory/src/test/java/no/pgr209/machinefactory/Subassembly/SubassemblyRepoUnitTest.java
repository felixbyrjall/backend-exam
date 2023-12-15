package no.pgr209.machinefactory.Subassembly;

import no.pgr209.machinefactory.model.Part;
import no.pgr209.machinefactory.model.Subassembly;
import no.pgr209.machinefactory.repo.PartRepo;
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
public class SubassemblyRepoUnitTest {

    @Autowired
    SubassemblyRepo subassemblyRepo;

    @Autowired
    PartRepo partRepo;

    @Test // Ensure a subassembly is created
    public void save_shouldReturnSubassembly() {
        Subassembly subassembly = subassemblyRepo.save(new Subassembly());

        assertThat(subassembly).isNotNull();
        assertThat(subassembly.getSubassemblyId()).isNotNull();
    }

    @Test // Test many-to-many relationship with Part
    public void save_shouldReturnSavedSubassemblyWithParts() {
        partRepo.save(new Part("LED indicator"));
        partRepo.save(new Part("Cables and connectors"));
        List<Part> Parts = partRepo.findAll();

        Subassembly subassembly = subassemblyRepo.save(new Subassembly());
        subassembly.setParts(Parts);

        Optional<Subassembly> findSubassembly = subassemblyRepo.findById(subassembly.getSubassemblyId());
        findSubassembly.ifPresent(checkSubassembly -> assertEquals(Parts, findSubassembly.get().getParts()));
    }

    @Test // Test findAll and ensure count of subassemblies
    public void findAll_shouldReturnNonEmptyListOfSubassemblies() {
        subassemblyRepo.save(new Subassembly());
        subassemblyRepo.save(new Subassembly());

        List<Subassembly> subassemblies = subassemblyRepo.findAll();

        assertThat(subassemblies).isNotNull();
        assertThat(subassemblies.size()).isEqualTo(2);
    }

    @Test // Test finding a subassembly by id
    public void findById_shouldReturnSubassembly() {
        Subassembly subassembly = subassemblyRepo.save(new Subassembly());

        Optional<Subassembly> foundSubassembly = subassemblyRepo.findById(subassembly.getSubassemblyId());

        assertThat(foundSubassembly).isPresent();
    }

    @Test // Test finding a non-existent machine id
    public void findById_shouldNotReturnNonExistentSubassembly() {
        Long nonExistentSubassembly = 23214L;

        Optional<Subassembly> findSubassembly = subassemblyRepo.findById(nonExistentSubassembly);

        assertThat(findSubassembly).isNotPresent();
    }

    @Test // Create and then update a subassembly
    public void update_shouldUpdateExistingSubassembly() {

        // Create Subassembly with information
        Subassembly subassembly = subassemblyRepo.save(new Subassembly("Robotic Arm System"));

        Optional<Subassembly> createdSubassembly = subassemblyRepo.findById(subassembly.getSubassemblyId());
        createdSubassembly.ifPresent(subassemblyMade -> assertEquals("Robotic Arm System", createdSubassembly.get().getSubassemblyName()));

        // Update subassembly name
        subassembly.setSubassemblyName("Robotic Leg System");

        // Check the name
        Optional<Subassembly> subassemblyUpdated = subassemblyRepo.findById(subassembly.getSubassemblyId());
        subassemblyUpdated.ifPresent(subassemblyChanged -> assertEquals("Robotic Leg System", subassemblyUpdated.get().getSubassemblyName()));
    }

    @Test // Create a Subassembly, check if the subassembly exist, delete the subassembly and then check if subassembly still exist
    public void deleteById_shouldRemoveSubassembly() {
        Subassembly subassembly = subassemblyRepo.save(new Subassembly());
        Optional<Subassembly> findSubassembly = subassemblyRepo.findById(subassembly.getSubassemblyId());

        assertThat(findSubassembly).isPresent();

        subassemblyRepo.deleteById(subassembly.getSubassemblyId());

        Optional<Subassembly> findDeletedSubassembly = subassemblyRepo.findById(subassembly.getSubassemblyId());
        assertThat(findDeletedSubassembly).isNotPresent();
    }

}
