package no.pgr209.machinefactory.Order;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderIntegrationTest {

    @Autowired
    DataFeedService dataFeedService;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        dataFeedService.initializeData();
    }

    @Test // Testing connection is OK (200), fetching orders (GET)
    void shouldFetchOrders() throws Exception {
        mockMvc.perform(get("/api/order"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                });
    }

    @Test // Testing GET request, ensuring correct values returned.
    void shouldFetchOrderById() throws Exception {
        mockMvc.perform(get("/api/order/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.customerName").value("Ola Nordmann"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address.addressStreet").value("Storgata 33"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machines.[0].machineType").value("Electronics"))
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                });
    }

    @Test // Testing POST request, creating an order, and then fetching it.
    void shouldCreateOrder() throws Exception {
        String orderJson = String.format("""
        {
            "customerId": %d,
            "addressId": %d,
            "machineId": [%d, %d],
            "orderDate": "2023-01-01T00:00:00"
        }
        """, 1L, 1L, 1L, 2L);

        // Create the order
        MvcResult createResult = mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isOk())
                .andReturn();

        // Extract the orderId from the response
        String responseContent = createResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(responseContent);
        int orderId = jsonObject.getInt("orderId");

        // Fetch the created order and check if details match.
        mockMvc.perform(get("/api/order/" + orderId))
                .andExpect(status().isOk())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                });
        /*
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.customerId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.customerName").value("Ola Nordmann"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address.addressId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machines[0].machineId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machines[0].machineId").value(2L));
         */
    }


    @Test // Testing PUT request, updating an order.
    void shouldUpdateOrder() throws Exception {

        String orderJson = String.format("""
        {
            "customerId": %d,
            "addressId": %d,
            "machineId": [%d],
            "orderDate": "2023-01-01T00:00:00"
        }
        """, 2L, 2L, 2L);

        mockMvc.perform(put("/api/order/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isOk())
                .andReturn(); // Necessary?

        // Fetch the updated order and check if details actually match.
        mockMvc.perform(get("/api/order/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.customerId").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.customerName").value("Kari Hansen"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address.addressId").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address.addressStreet").value("Husmannsgate 14"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machines[0].machineId").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machines[0].machineName").value("Speaker"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machines[0].machineType").value("Electronics"));
    }

    // BELOW: Testing the opposite.

    @Test // Expect bad request (400) missing body parameters.
    void shouldNotCreateOrder() throws Exception {

        String orderJson = String.format(""" 
        {
            "customerId": %d,
        }
        """, 1L);

        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isBadRequest());
    }

    @Test // Expect Not Found using non-existent ID
    void shouldNotUpdateNonExistentOrder () throws Exception {
        String orderJson = String.format("""
            {
                "customerId": %d,
                "addressId": %d,
                "machineId": [%d],
                "orderDate": "2023-01-01T00:00:00"
            }
            """, 2L, 2L, 2L);

        mockMvc.perform(put("/api/order/56123564")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isNotFound());
    }

}
