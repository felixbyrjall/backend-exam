package no.pgr209.machinefactory.Address;

import no.pgr209.machinefactory.service.DataFeedService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    void shouldFetchAddressOnPage() throws Exception {
        mockMvc.perform(get("/api/address/page/0"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].addressId").value(1));
    }

    @Test
    void shouldFetchAddressById() throws Exception {
        mockMvc.perform(get("/api/address/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressStreet").value("Storgata 33"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressCity").value("Oslo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressZip").value("2204"));
    }

    @Test
    void shouldCreateAddress() throws Exception {
        String addressJson = """
        {
            "addressStreet": "Kongens gate 15",
            "addressCity": "Oslo",
            "addressZip":  "0154",
            "customerId": []
        }
        """;

        // Create the address
        MvcResult createResult = mockMvc.perform(post("/api/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson))
                .andExpect(status().isCreated())
                .andReturn();

        // Extract the addressId from the response
        String responseContent = createResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(responseContent);
        int addressId = jsonObject.getInt("addressId");

        mockMvc.perform(get("/api/address/" + addressId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressId").value(addressId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressStreet").value("Kongens gate 15"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressCity").value("Oslo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressZip").value("0154"));
    }

    @Test
    void shouldUpdateAddressWithAddingCustomer() throws Exception {
        String addressJson = """
        {
            "addressStreet": "Kongens gate 15",
            "addressCity": "Oslo",
            "addressZip":  "0154",
            "customerId": [2]
        }
        """;

        mockMvc.perform(put("/api/address/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/address/2"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressId").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressStreet").value("Kongens gate 15"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressCity").value("Oslo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressZip").value("0154"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customers[0].customerId").value(2L));
    }

    @Test
    void shouldUpdateAddressWithNewInfo() throws Exception {
        String addressJson = """
        {
            "addressStreet": "Drammens gate 24",
            "addressCity": "Drammen",
            "addressZip":  "0584",
            "customerId": []
        }
        """;

        mockMvc.perform(put("/api/address/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/address/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressStreet").value("Drammens gate 24"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressCity").value("Drammen"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressZip").value("0584"));
    }

    @Test
    void shouldNotFetchNonExistentAddressById() throws Exception {
        mockMvc.perform(get("/api/address/24325"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotCreateAddressWithInvalidCustomerId() throws Exception {
        String addressJson = String.format("""
        {
            "addressStreet": "Drammens gate 24",
            "addressCity": "Drammen",
            "addressZip":  "0584",
            "customerId": [%d]
        }
        """, 345245L);

        mockMvc.perform(post("/api/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotCreateAddressWithEmptyData() throws Exception {
        String addressJson = """
        {
            "addressStreet": "",
            "addressCity": "",
            "addressZip":  "",
            "customerId": []
        }
        """;

        mockMvc.perform(post("/api/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotUpdateAddressWithInvalidCustomerId() throws Exception {
        String addressJson = String.format("""
        {
            "addressStreet": "Drammens gate 24",
            "addressCity": "Drammen",
            "addressZip":  "0584",
            "customerId": [%d]
        }
        """, 4325L);

        mockMvc.perform(put("/api/address/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotUpdateAddressWithNoData() throws Exception {
        String addressJson = """
        {
            "addressStreet": "",
            "addressCity": "",
            "addressZip":  "",
            "customerId": []
        }
        """;

        mockMvc.perform(put("/api/address/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson))
                .andExpect(status().isNotFound());
    }

    @Test // Test DELETE request for addresses.
    void shouldDeleteAddressById() throws Exception {
        mockMvc.perform(get("/api/address/2")) // Check if address exist.
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/address/2")) // Delete the address by id.
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/address/2")) // Check if address is removed.
                .andExpect(status().isNotFound());
    }

    @Test // Test DELETE requests and that associated Orders are deleted.
    void shouldDeleteAddressByIdAndOrdersAssociated() throws Exception {
        mockMvc.perform(get("/api/address/1")) // Check if address exist.
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/order/1")) // Check if order exist and that this is the address order.
                .andExpect(MockMvcResultMatchers.jsonPath("$.address.addressId").value(1L))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/address/1")) // Delete the address by id.
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/address/1")) // Check if address is removed.
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/order/1")) // Check if associated order is removed.
                .andExpect(status().isNotFound());
    }

    @Test // Test deleting a address that doesn't exist
    void shouldNotDeleteAddressNotExist() throws Exception {
        mockMvc.perform(get("/api/customer/2435423"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/customer/245235"))
                .andExpect(status().isNotFound());
    }
}
