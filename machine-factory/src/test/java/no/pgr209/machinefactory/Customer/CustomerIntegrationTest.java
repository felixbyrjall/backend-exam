package no.pgr209.machinefactory.Customer;

import org.json.JSONObject;
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
    MockMvc mockMvc;

    @Test // Fetch all customers, ensure they are returned
    void shouldFetchCustomers() throws Exception {
        mockMvc.perform(get("/api/customer"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].customerId").value(2));
    }

    @Test // Check that customers are returned from pagination, returning the correct amount (Max: 3 per page)
    void shouldFetchCustomersOnPage() throws Exception {
        mockMvc.perform(get("/api/customer/page/0"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].customerId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].customerId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].customerId").doesNotExist());
    }

    @Test // Fetch a customer by id and ensure correct values are returned from it
    void shouldFetchCustomerById() throws Exception {
        mockMvc.perform(get("/api/customer/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("Ola Nordmann"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerEmail").value("ola@nordmann.no"));
    }

    @Test // Test creating a customer
    void shouldCreateCustomer() throws Exception {
        String customerJson = """
        {
            "customerName": "Tom Hansen",
            "customerEmail": "tom@hansen.no",
            "addressId": []
        }
        """;

        MvcResult createResult = mockMvc.perform(post("/api/customer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(customerJson))
            .andExpect(status().isCreated())
            .andReturn();

        // Extract the customerId from the response
        String responseContent = createResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(responseContent);
        int customerId = jsonObject.getInt("customerId");

        mockMvc.perform(get("/api/customer/" + customerId))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(customerId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("Tom Hansen"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.customerEmail").value("tom@hansen.no"));
    }

    @Test // Test updating a customer
    void shouldUpdateCustomer() throws Exception {
        String customerJson = """
        {
            "customerName": "Tommy Hansen",
            "customerEmail": "tom@hansen.no",
            "addressId": [2]
        }
        """;

        mockMvc.perform(put("/api/customer/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isOk());

        // Fetch the updated customer and check if details are correct
        mockMvc.perform(get("/api/customer/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("Tommy Hansen"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerEmail").value("tom@hansen.no"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addresses[0].addressId").value(2L));
    }

    @Test // Test updating a customer without addresses
    void shouldUpdateCustomerWithAnotherAddress() throws Exception {
        String customerJson = """
        {
            "customerName": "Tom Hansen",
            "customerEmail": "tom@hansen.no",
            "addressId": []
        }
        """;

        mockMvc.perform(put("/api/customer/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isOk());

        // Fetch the updated customer and check if details are correct
        mockMvc.perform(get("/api/customer/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("Tom Hansen"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerEmail").value("tom@hansen.no"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addresses[0].addressId").doesNotExist());

    }

    @Test // Expect response to be NOT FOUND when fetching a non-existent id
    void shouldNotFetchNonExistentCustomerById () throws Exception {
        mockMvc.perform(get("/api/customer/81561"))
                .andExpect(status().isNotFound());
    }

    @Test // Expect response to be NOT FOUND when creating an address with an address id that do not exist
    void shouldNotCreateCustomerWithInvalidAddressId() throws Exception {
        String customerJson = String.format("""
        {
            "customerName": "Erik Olsen",
            "customerEmail": "erik@olsen.no",
            "addressId": [%d]
        }
        """, 77L);

        mockMvc.perform(post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isNotFound());
    }

    @Test // Expect response to be NOT FOUND when trying to create a customer without required data
    void shouldNotCreateCustomerWithEmptyData() throws Exception {
        String customerJson = """
        {
            "customerName": "",
            "customerEmail": "",
            "addressId": []
        }
        """;

        mockMvc.perform(post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isNotFound());
    }

    @Test // Expect response to be NOT FOUND when trying to update a customer that do not exist
    void shouldNotUpdateNonExistentCustomer() throws Exception {
        String customerJson = """
        {
            "customerName": "Erik Svein Olsen",
            "customerEmail": "erik.svein@olsen.no",
            "addressId": []
        }
        """;

        mockMvc.perform(put("/api/customer/234345")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isNotFound());
    }

    @Test // Expect NOT FOUND when updating a customer with an address that do not exist
    void shouldNotUpdateCustomerWithInvalidData() throws Exception {
        String customerJson = String.format("""
        {
            "customerName": "James Brown",
            "customerEmail": "james@brown.com",
            "addressId": [%d]
        }
        """, 54323L);

        mockMvc.perform(put("/api/customer/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isNotFound());
    }

    @Test // Expect NOT FOUND when updating a customer with no data
    void shouldNotUpdateCustomerWithEmptyData() throws Exception {
        String customerJson = """
        {
            "customerName": "",
            "customerEmail": "",
            "addressId": []
        }
        """;

        mockMvc.perform(put("/api/customer/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isNotFound());
    }

    @Test // Test deleting a customer and confirm it is removed
    void shouldDeleteCustomerById() throws Exception {
        mockMvc.perform(get("/api/customer/2")) // Check if customer exist
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/customer/2")) // Delete the customer by id
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/customer/2")) // Check if customer is deleted
                .andExpect(status().isNotFound());
    }

    @Test // Test deleting a customer and check if associated orders are also deleted
    void shouldDeleteCustomerByIdAndOrdersAssociated() throws Exception {
        mockMvc.perform(get("/api/customer/1")) // Check if customer exist
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/order/1")) // Check if order exist and that this is the connected order
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.customerId").value(1L))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/customer/1")) // Delete the customer by id
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/customer/1")) // Check if customer is deleted
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/order/1")) // Check if associated order is deleted
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
