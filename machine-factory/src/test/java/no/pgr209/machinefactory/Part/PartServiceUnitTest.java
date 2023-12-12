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
import static org.mockito.Mockito.mock;
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
}
