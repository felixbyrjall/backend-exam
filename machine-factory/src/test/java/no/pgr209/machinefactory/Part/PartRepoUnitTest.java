package no.pgr209.machinefactory.Part;

import no.pgr209.machinefactory.model.*;
import no.pgr209.machinefactory.repo.PartRepo;
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
public class PartRepoUnitTest {

    @Autowired
    PartRepo partRepo;

    @Test // Ensure a part is created
    public void save_shouldReturnPart() {
        Part part = partRepo.save(new Part());

        assertThat(part).isNotNull();
        assertThat(part.getPartId()).isNotNull();
    }

    @Test // Test findAll and ensure count of parts
    public void findAll_shouldReturnNonEmptyListOfParts() {
        partRepo.save(new Part());
        partRepo.save(new Part());

        List<Part> parts = partRepo.findAll();

        assertThat(parts).isNotNull();
        assertThat(parts.size()).isEqualTo(2);
    }

    @Test // Test finding a part by id
    public void findById_shouldReturnPart() {
        Part part = partRepo.save(new Part());

        Optional<Part> foundPart = partRepo.findById(part.getPartId());

        assertThat(foundPart).isPresent();
    }

    @Test // Test finding a non-existent part id
    public void findById_shouldNotReturnNonExistentPart() {
        Long nonExistentPart = 3555L;

        Optional<Part> findPart = partRepo.findById(nonExistentPart);

        assertThat(findPart).isNotPresent();
    }

    @Test // Create and then update a part
    public void update_shouldUpdateExistingPart() {

        // Create Part with information
        Part part = partRepo.save(new Part("Mounting bracket"));

        // Check if the part was created with the correct name
        Optional<Part> createdPart = partRepo.findById(part.getPartId());
        createdPart.ifPresent(partMade -> assertEquals("Mounting bracket", createdPart.get().getPartName()));

        // Update part name
        part.setPartName("LED indicator");

        // Check the name
        Optional<Part> partUpdated = partRepo.findById(part.getPartId());
        partUpdated.ifPresent(partChanged -> assertEquals("LED indicator", partUpdated.get().getPartName()));
    }

    @Test // Create a part, check if the part exist, delete the part and then check if part still exist
    public void deleteById_shouldRemovePart() {
        Part part = partRepo.save(new Part());
        Optional<Part> findPart = partRepo.findById(part.getPartId());

        assertThat(findPart).isPresent();

        partRepo.deleteById(part.getPartId());

        Optional<Part> findDeletedPart = partRepo.findById(part.getPartId());
        assertThat(findDeletedPart).isNotPresent();
    }
}
