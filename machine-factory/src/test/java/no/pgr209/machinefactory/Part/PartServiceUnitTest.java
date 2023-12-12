package no.pgr209.machinefactory.Part;

import no.pgr209.machinefactory.model.*;
import no.pgr209.machinefactory.repo.PartRepo;
import no.pgr209.machinefactory.service.DataFeedService;
import no.pgr209.machinefactory.service.PartService;
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
public class PartServiceUnitTest {


    @Autowired
    DataFeedService dataFeedService;

    @Mock
    private PartRepo partRepo;

    @InjectMocks
    private PartService partService;

    @Test
    void shouldReturnAllParts() {
        List<Part> mockParts = new ArrayList<>();
        when(partRepo.findAll()).thenReturn(mockParts);
        List<Part> parts = partService.getAllParts();

        assertEquals(mockParts, parts);
    }

    @Test
    void shouldReturnPartById() {
        Part mockPart = new Part();
        when(partRepo.findById(1L)).thenReturn(Optional.of(mockPart));
        Part part = partService.getPartById(1L);

        assertEquals(mockPart, part);
    }

    @Test // Comprehensive mock & unit-testing, creating a machine.
    void shouldCreatePart() {
        PartDTO partDTO = new PartDTO();
        partDTO.setPartName("Light-Emitting Diode");

        Part mockPart = new Part("Light-Emitting Diode");

        when(partRepo.existsById(1L)).thenReturn(true);
        when(partRepo.findById(1L)).thenReturn(Optional.of(mockPart));

        Part createdPart = new Part();
        createdPart.setPartName("Light-Emitting Diode");
        when(partRepo.save(any())).thenReturn(createdPart);

        Part resultPart = partService.createPart(partDTO);

        assertThat(resultPart).isNotNull();
        assertThat(resultPart.getPartName()).isEqualTo(mockPart.getPartName());
    }
}
