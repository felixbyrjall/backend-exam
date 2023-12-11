package no.pgr209.machinefactory.Customer;

import no.pgr209.machinefactory.service.DataFeedService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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

    @Test // Ensure customer are returned from pagination, returning the correct customers.
    void shouldFetchCustomersOnPage() throws Exception {
        mockMvc.perform(get("/api/customer/page/0"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerId").value(1));
    }

    @Test // Test GET - Customer by id, ensure correct customer information are returned.
    void shouldFetchCustomerById() throws Exception {
        mockMvc.perform(get("/api/customer/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("Ola Nordmann"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerEmail").value("ola@nordmann.no"));
    }

    @Test
    void shouldCreateCustomer() throws Exception {
        // Create customer, given that an address has also been created.
        String customerJson = String.format("""
        {
            "customerName": "James Brown",
            "customerEmail": "james@brown.com",
            "addressId": [%d]
        }
        """, 1L);

        // Create the customer
        MvcResult createResult = mockMvc.perform(post("/api/customer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(customerJson))
            .andExpect(status().isCreated())
            .andReturn();

        // Extract the customerId from the response
        String responseContent = createResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(responseContent);
        int customerId = jsonObject.getInt("customerId");

        // Fetch the created customer and check if details match.
        mockMvc.perform(get("/api/customer/" + customerId))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(customerId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("James Brown"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.customerEmail").value("james@brown.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.addresses[0].addressId").value(1L));
    }
}
