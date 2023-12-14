package no.pgr209.machinefactory.Part;

import no.pgr209.machinefactory.model.PartDTO;
import no.pgr209.machinefactory.repo.*;
import no.pgr209.machinefactory.service.PartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class PartServiceIntegrationTest {

    @Autowired
    PartService partService;

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    MachineRepo machineRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    SubassemblyRepo subassemblyRepo;

    @Autowired
    PartRepo partRepo;


    @BeforeEach // Ensure clean DB.
    void setUp() {
        orderRepo.deleteAll();
        machineRepo.deleteAll();
        subassemblyRepo.deleteAll();
        partRepo.deleteAll();
    }

    @Test
    void shouldCreateAndFetchPart() {
        PartDTO part = new PartDTO();

        part.setPartName("Voltage Regulator");
        partService.createPart(part);

        var parts = partService.getAllParts();

        assertEquals(1, parts.size());
        assertEquals("Voltage Regulator", parts.get(0).getPartName());
    }
}
