package no.pgr209.machinefactory.Subassembly;

import no.pgr209.machinefactory.model.*;
import no.pgr209.machinefactory.repo.MachineRepo;
import no.pgr209.machinefactory.repo.PartRepo;
import no.pgr209.machinefactory.repo.SubassemblyRepo;
import no.pgr209.machinefactory.service.DataFeedService;
import no.pgr209.machinefactory.service.MachineService;
import no.pgr209.machinefactory.service.SubassemblyService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SubassemblyServiceUnitTest {
    @Autowired
    DataFeedService dataFeedService;

    @Mock
    private SubassemblyRepo subassemblyRepo;

    @Mock
    private PartRepo partRepo;

    @InjectMocks
    private SubassemblyService subassemblyService;

    @Test
    void shouldReturnAllSubassemblies() {
        List<Subassembly> mockSubassemblies = new ArrayList<>();
        when(subassemblyRepo.findAll()).thenReturn(mockSubassemblies);
        List<Subassembly> subassemblies = subassemblyService.getAllSubassemblies();

        assertEquals(mockSubassemblies, subassemblies);
    }
}
