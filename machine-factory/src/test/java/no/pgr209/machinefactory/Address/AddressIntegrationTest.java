package no.pgr209.machinefactory.Address;

import no.pgr209.machinefactory.service.DataFeedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AddressIntegrationTest {

    @Autowired
    DataFeedService dataFeedService;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        dataFeedService.initializeData(); // Feed in-memory DB with sample data from DataFeedService.
    }

    @Test
    void shouldFetchAddresses() throws Exception {
        mockMvc.perform(get("/api/address"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].addressId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].addressId").value(2));
    }
}
