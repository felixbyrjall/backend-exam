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
}
