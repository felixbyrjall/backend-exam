package no.pgr209.machinefactory.Subassembly;

import no.pgr209.machinefactory.model.*;
import no.pgr209.machinefactory.repo.PartRepo;
import no.pgr209.machinefactory.repo.SubassemblyRepo;
import no.pgr209.machinefactory.service.SubassemblyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("dev") // Exclude CommandLineRunner from Unit test, ensuring clean database
public class SubassemblyServiceUnitTest {

    @Autowired
    SubassemblyService subassemblyService;

    @MockBean
    private SubassemblyRepo subassemblyRepo;

    @MockBean
    private PartRepo partRepo;

    @Test // Mock and test fetching all subassemblies
    void shouldReturnAllSubassemblies() {
        List<Subassembly> mockSubassemblies = new ArrayList<>();
        when(subassemblyRepo.findAll()).thenReturn(mockSubassemblies);
        List<Subassembly> subassemblies = subassemblyService.getAllSubassemblies();

        assertEquals(mockSubassemblies, subassemblies);
    }

    @Test // Mock and test fetching subassembly by id
    void shouldReturnSubassemblyById() {
        Subassembly mockSubassembly = new Subassembly();
        when(subassemblyRepo.findById(1L)).thenReturn(Optional.of(mockSubassembly));
        Subassembly subassembly = subassemblyService.getSubassemblyById(1L);

        assertEquals(mockSubassembly, subassembly);
    }

    @Test // Comprehensive mock & unit-testing, creating a subassembly.
    void shouldCreateSubassembly() {
        SubassemblyDTO subassemblyDTO = new SubassemblyDTO();
        subassemblyDTO.setSubassemblyName("Voltage Regulator");
        List<Long> parts = List.of(1L, 2L);
        subassemblyDTO.setPartId(parts);

        Subassembly mockSubassembly = new Subassembly("Voltage Regulator");
        Part partOne = new Part( "Capacitor");
        Part partTwo = new Part( "Resistor");

        when(subassemblyRepo.existsById(1L)).thenReturn(true);
        when(subassemblyRepo.findById(1L)).thenReturn(Optional.of(mockSubassembly));
        when(partRepo.existsById(any())).thenReturn(true);
        when(partRepo.findAllById(parts)).thenReturn(List.of(partOne, partTwo));

        Subassembly createdSubassembly = new Subassembly();
        createdSubassembly.setSubassemblyName("Voltage Regulator");
        createdSubassembly.setParts(List.of(partOne, partTwo));
        when(subassemblyRepo.save(any())).thenReturn(createdSubassembly);

        Subassembly resultSubassembly = subassemblyService.createSubassembly(subassemblyDTO);

        assertThat(resultSubassembly).isNotNull();
        assertThat(resultSubassembly.getSubassemblyName()).isEqualTo(mockSubassembly.getSubassemblyName());
        assertThat(resultSubassembly.getParts()).containsExactlyInAnyOrderElementsOf(List.of(partOne, partTwo));
    }
}
