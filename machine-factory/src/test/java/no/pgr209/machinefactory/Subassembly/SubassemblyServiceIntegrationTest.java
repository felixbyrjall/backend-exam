package no.pgr209.machinefactory.Subassembly;

import no.pgr209.machinefactory.model.Part;
import no.pgr209.machinefactory.model.SubassemblyDTO;
import no.pgr209.machinefactory.repo.*;
import no.pgr209.machinefactory.service.PartService;
import no.pgr209.machinefactory.service.SubassemblyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class SubassemblyServiceIntegrationTest {

    @Autowired
    SubassemblyService subassemblyService;

    @Autowired
    PartService partService;

    @Autowired
    MachineRepo machineRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    SubassemblyRepo subassemblyRepo;

    @Autowired
    PartRepo partRepo;

    @BeforeEach
    void setUp() {
        orderRepo.deleteAll();
        machineRepo.deleteAll();
        subassemblyRepo.deleteAll();
        partRepo.deleteAll();
    }
    @Test
    void shouldCreateAndFetchSubassembly() {
        SubassemblyDTO subassembly = new SubassemblyDTO();
        Part partOne = partRepo.save(new Part("Rod"));

        List<Long> parts = new ArrayList<>();
        parts.add(partOne.getPartId());

        subassembly.setSubassemblyName("Something");
        subassembly.setPartId(parts);
        subassemblyService.createSubassembly(subassembly);

        var subassemblies = subassemblyService.getAllSubassemblies();

        assertEquals(1, subassemblies.size());
    }

}
