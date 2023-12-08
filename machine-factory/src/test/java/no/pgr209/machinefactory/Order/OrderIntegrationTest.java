package no.pgr209.machinefactory.Order;

import no.pgr209.machinefactory.service.DataFeedService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
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

    @Test
    void shouldFetchOrders() throws Exception {
        mockMvc.perform(get("/api/order"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                });
    }

    @Test
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

    @Test
    void shouldCreateOrder() throws Exception {
        long customerId = 1L;
        long addressId = 1L;
        long machineId1 = 1L;
        long machineId2 = 2L;

        String orderJson = String.format("""
            {
                "customerId": %d,
                "addressId": %d,
                "machineId": [%d, %d],
                "orderDate": "2023-01-01T00:00:00"
            }
            """, customerId, addressId, machineId1, machineId2);

        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldUpdateOrder() throws Exception {

        String orderJson = String.format("""
            {
                "customerId": 2,
                "addressId": 2,
                "machineId": [2],
                "orderDate": "2023-01-01T00:00:00"
            }
            """);

        mockMvc.perform(put("/api/order/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.customerName").value("Kari Hansen"))
                .andReturn();
    }

    @Test // Needs to look over.
    void updateOrderNotFound() throws Exception {
        mockMvc.perform(put("/api/order/50"))
                .andExpect(status().is(400));
    }

}
