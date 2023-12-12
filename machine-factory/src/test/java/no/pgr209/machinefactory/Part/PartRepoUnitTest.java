package no.pgr209.machinefactory.Part;

import no.pgr209.machinefactory.model.*;
import no.pgr209.machinefactory.repo.MachineRepo;
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
}
