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

    @Test // Test connection is OK, fetch all customers, and ensure customers are returned.
    void shouldFetchCustomers() throws Exception {
        mockMvc.perform(get("/api/customer"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].customerId").value(2));
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

    @Test // Testing POST request, creating a customer.
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

    @Test // Testing PUT request, updating a customer.
    void shouldUpdateCustomer() throws Exception {
        String customerJson = String.format("""
        {
            "customerName": "Tom Hardy",
            "customerEmail": "tom@hardy.com",
            "addressId": [%d]
        }
        """, 2L);

        mockMvc.perform(put("/api/customer/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isOk());

        // Fetch the updated customer and check if details actually match.
        mockMvc.perform(get("/api/customer/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("Tom Hardy"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerEmail").value("tom@hardy.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addresses[0].addressId").value(2L));
    }

    @Test // Expect fetch to be NOT FOUND using non-existent ID customer
    void shouldNotFetchNonExistentCustomerById () throws Exception {
        mockMvc.perform(get("/api/customer/81561"))
                .andExpect(status().isNotFound());
    }

    @Test // Expect NOT FOUND when creating a customer with non-existent parameters and invalid data
    void shouldNotCreateCustomerWithInvalidData() throws Exception {
        String customerJson = String.format("""
        {
            "customerName": "James Brown",
            "customerEmail": "james@brown.com",
            "addressId": [%d]
        }
        """, 3425L);

        mockMvc.perform(post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isNotFound());
    }

    @Test // Test DELETE request for customers.
    void shouldDeleteCustomerById() throws Exception {
        mockMvc.perform(get("/api/customer/2")) // Check if customer exist.
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/customer/2")) // Delete the customer by id.
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/customer/2")) // Check if order is removed.
                .andExpect(status().isNotFound());
    }

    @Test // Test DELETE requests and that associated Orders are deleted.
    void shouldDeleteCustomerByIdAndOrdersAssociated() throws Exception {
        mockMvc.perform(get("/api/customer/1")) // Check if customer exist.
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/order/1")) // Check if order exist and that this is the customer's order.
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.customerId").value(1L))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/customer/1")) // Delete the customer by id.
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/customer/1")) // Check if customer is removed.
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/order/1")) // Check if associated order is removed.
                .andExpect(status().isNotFound());
    }

    @Test // Test deleting a customer that doesn't exist
    void shouldNotDeleteCustomerNotExist() throws Exception {
        mockMvc.perform(get("/api/customer/23165"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/customer/23165"))
                .andExpect(status().isNotFound());
    }
}
