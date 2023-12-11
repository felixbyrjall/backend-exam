package no.pgr209.machinefactory.Customer;

import no.pgr209.machinefactory.service.DataFeedService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CustomerIntegrationTest {

    @Autowired
    DataFeedService dataFeedService;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        dataFeedService.initializeData(); // Feed in-memory DB with sample data from DataFeedService.
    }

    @Test // Test connection is OK, fetch all customers.
    void shouldFetchCustomers() throws Exception {
        mockMvc.perform(get("/api/customer"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerId").value(1));
    }

    @Test // Ensure customer are return from pagination, returning the correct customers.
    void shouldFetchCustomersOnPage() throws Exception {
        mockMvc.perform(get("/api/customer/page/0"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerId").value(1));
    }

}
