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
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderIntegrationTest {

    @Autowired
    DataFeedService dataFeedService;

    @Autowired
    MockMvc mockMvc;

    // Feed in-memory DB with sample data from DataFeedService
    @BeforeEach
    void setUp() {
        dataFeedService.initializeData();
    }

    @Test // Fetch all orders, ensure orders are returned
    void shouldFetchAllOrders() throws Exception {
        mockMvc.perform(get("/api/order"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].orderId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].orderId").value(2));
    }

    @Test // Check that orders are returned from pagination, returning the correct amount of orders (Max: 3)
    void shouldFetchOrdersOnPage() throws Exception {
        mockMvc.perform(get("/api/order/page/0"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].orderId").value(1));
    }

    @Test // Fetch an order by id and ensure correct values are returned from the order.
    void shouldFetchOrderById() throws Exception {
        mockMvc.perform(get("/api/order/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.customerId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.customerName").value("Ola Nordmann"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.customerEmail").value("ola@nordmann.no"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address.addressId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address.addressStreet").value("Storgata 33"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address.addressCity").value("Oslo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address.addressZip").value("2204"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machines.[0].machineId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machines.[0].machineName").value("3D printer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machines.[0].machineType").value("Electronics"));
    }

    @Test // Check creating an order and then fetching it.
    void shouldCreateOrder() throws Exception {
        String orderJson = String.format("""
        {
            "customerId": %d,
            "addressId": %d,
            "machineId": [%d, %d],
            "orderDate": "2023-01-01T00:00:00"
        }
        """, 1L, 1L, 1L, 2L);

        MvcResult createResult = mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isCreated())
                .andReturn();

        // Extract the orderId from the response
        String responseContent = createResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(responseContent);
        int orderId = jsonObject.getInt("orderId");

        // Fetch the created order and check if details are correct
        mockMvc.perform(get("/api/order/" + orderId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.customerId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.customerName").value("Ola Nordmann"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address.addressId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machines[0].machineId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machines[1].machineId").value(2L));
    }

    @Test // Test updating an order.
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
                .andExpect(status().isOk());

        // Fetch the updated order and check if details are correct
        mockMvc.perform(get("/api/order/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.customerId").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.customerName").value("Kari Hansen"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address.addressId").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address.addressStreet").value("Husmannsgate 14"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machines[0].machineId").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machines[0].machineName").value("Laser printer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machines[0].machineType").value("Electronics"));
    }

    @Test // Expect response to be NOT FOUND when fetching a non-existent id
    void shouldNotFetchNonExistentOrderById () throws Exception {
        mockMvc.perform(get("/api/order/81561"))
                .andExpect(status().isNotFound());
    }

    @Test // Expect response to be NOT FOUND when creating order with data that do not exist
    void shouldNotCreateOrderWithInvalidData() throws Exception {
        String orderJson = String.format("""
        {
            "customerId": %d,
            "addressId": %d,
            "machineId": [%d],
            "orderDate": "2023-01-01T00:00:00"
        }
        """, 77L, 91L, 86L);

        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isNotFound());
    }

    @Test // Expect response to be NOT FOUND when trying to update a non-existent order id
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

    @Test // Expect response to be NOT FOUND when updating an order with data that do not exist
    void shouldNotUpdateOrderWithInvalidData() throws Exception {

        String orderJson = String.format("""
        {
            "customerId": %d,
            "addressId": %d,
            "machineId": [%d],
            "orderDate": "2023-01-01T00:00:00"
        }
        """, 91L, 63L, 87L);

        mockMvc.perform(put("/api/order/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isNotFound());
    }

    @Test // Test deleting order and confirm the order is removed
    void shouldDeleteOrderById() throws Exception {
        mockMvc.perform(get("/api/order/2")) // Check if order exist
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/order/2")) // Delete the order by id
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/order/2")) // Confirm the order is removed
                .andExpect(status().isNotFound());
    }

    @Test // Test deleting an order that doesn't exist
    void shouldNotDeleteNonExistentOrder() throws Exception {
        mockMvc.perform(get("/api/order/23165"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/order/23165"))
                .andExpect(status().isNotFound());
    }
}
