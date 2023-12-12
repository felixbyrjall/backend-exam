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
@ActiveProfiles("dev")

public class PartRepoUnitTest {

    @Autowired
    PartRepo partRepo;

    @Test
    public void save_shouldReturnPart() {
        Part part = new Part();
        Part savedPart = partRepo.save(part);

        assertThat(savedPart).isNotNull();
        assertThat(savedPart.getPartId()).isNotNull();
    }

    @Test // Test fetching all parts.
    public void findAll_shouldReturnNonEmptyListOfParts() {
        Part firstPart = new Part();
        Part secondPart = new Part();
        partRepo.save(firstPart);
        partRepo.save(secondPart);

        List<Part> parts = partRepo.findAll();

        assertThat(parts).isNotNull();
        assertThat(parts.size()).isGreaterThan(0);
    }

    @Test // Test fetching part by id
    public void findById_shouldReturnPart() {
        Part part = partRepo.save(new Part());

        Optional<Part> foundPart = partRepo.findById(part.getPartId());

        assertThat(foundPart).isPresent();
    }

    @Test // Test fetching a non-existent part
    public void findById_shouldNotReturnNonExistentPart() {
        Long nonExistentPart = 3555L;

        Optional<Part> findPart = partRepo.findById(nonExistentPart);

        assertThat(findPart).isNotPresent();
    }

    @Test // Create part, update the Part type and check if Part type is updated.
    public void update_shouldUpdateExistingPart() {

        // Create Part with information
        Part part = partRepo.save(new Part("Diode"));

        // Check if the part was created with the correct name
        Optional<Part> createdPart = partRepo.findById(part.getPartId());
        createdPart.ifPresent(partMade -> assertEquals("Diode", createdPart.get().getPartName()));

        // Update part name
        part.setPartName("Inductor");
        partRepo.save(part);

        // Check if the part name got updated
        Optional<Part> partUpdated = partRepo.findById(part.getPartId());
        partUpdated.ifPresent(partChanged -> assertEquals("Inductor", partUpdated.get().getPartName()));
    }

    @Test // Create a part, check if the part exist, delete the part and then check if part still exist.
    public void deleteById_shouldRemovePart() {
        Part part = partRepo.save(new Part());
        Optional<Part> findPart = partRepo.findById(part.getPartId());

        assertThat(findPart).isPresent();

        partRepo.deleteById(part.getPartId());
        Optional<Part> findDeletedPart = partRepo.findById(part.getPartId());
        assertThat(findDeletedPart).isNotPresent();
    }
}
