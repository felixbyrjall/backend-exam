package no.pgr209.machinefactory.Address;

import org.json.JSONObject;
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
    MockMvc mockMvc;

    @Test // Fetch all addresses, ensure they are returned
    void shouldFetchAddresses() throws Exception {
        mockMvc.perform(get("/api/address"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].addressId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].addressId").value(2));
    }

    @Test // Check that addresses are returned from pagination, returning the correct amount (Max: 3 per page)
    void shouldFetchAddressOnPage() throws Exception {
        mockMvc.perform(get("/api/address/page/0"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].addressId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].addressId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].addressId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].addressId").doesNotExist());
    }

    @Test // Fetch an address by id and ensure correct values are returned from it
    void shouldFetchAddressById() throws Exception {
        mockMvc.perform(get("/api/address/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressStreet").value("Storgata 33"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressCity").value("Oslo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressZip").value("0184"));
    }

    @Test // Test creating an address and then fetch it
    void shouldCreateAddress() throws Exception {
        String addressJson = """
        {
            "addressStreet": "Kongens gate 15",
            "addressCity": "Oslo",
            "addressZip":  "0153",
            "customerId": []
        }
        """;

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressZip").value("0153"));
    }

    @Test // Test updating an address
    void shouldUpdateAddressWithAddingCustomer() throws Exception {
        String addressJson = """
        {
            "addressStreet": "Kongens gate 15",
            "addressCity": "Oslo",
            "addressZip":  "0153",
            "customerId": [2]
        }
        """;

        mockMvc.perform(put("/api/address/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson))
                .andExpect(status().isOk());

        // Fetch the updated address and check if details are correct
        mockMvc.perform(get("/api/address/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressStreet").value("Kongens gate 15"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressCity").value("Oslo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressZip").value("0153"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customers[0].customerId").value(2L));
    }

    @Test
    void shouldUpdateAddressWithNewInfo() throws Exception {
        String addressJson = """
        {
            "addressStreet": "Haugesgate 24",
            "addressCity": "Drammen",
            "addressZip":  "3016",
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressStreet").value("Haugesgate 24"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressCity").value("Drammen"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressZip").value("3016"));
    }

    @Test // Expect response to be NOT FOUND when fetching a non-existent id
    void shouldNotFetchNonExistentAddressById() throws Exception {
        mockMvc.perform(get("/api/address/24325"))
                .andExpect(status().isNotFound());
    }

    @Test // Expect response to be NOT FOUND when creating an address with a customer id that do not exist
    void shouldNotCreateAddressWithInvalidCustomerId() throws Exception {
        String addressJson = String.format("""
        {
            "addressStreet": "Haugesgate 24",
            "addressCity": "Drammen",
            "addressZip":  "3016",
            "customerId": [%d]
        }
        """, 86L);

        mockMvc.perform(post("/api/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson))
                .andExpect(status().isNotFound());
    }

    @Test // Expect response to be NOT FOUND when trying to create an address without required data
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

    @Test // Expect response to be NOT FOUND when updating an address with a customer that do not exist
    void shouldNotUpdateAddressWithInvalidCustomerId() throws Exception {
        String addressJson = String.format("""
        {
            "addressStreet": "Haugesgate 24",
            "addressCity": "Drammen",
            "addressZip":  "3016",
            "customerId": [%d]
        }
        """, 76L);

        mockMvc.perform(put("/api/address/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson))
                .andExpect(status().isNotFound());
    }

    @Test // Expect response to be NOT FOUND when trying to update an address without data
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

    @Test // Test deleting an address and confirm it is removed
    void shouldDeleteAddressById() throws Exception {
        mockMvc.perform(get("/api/address/2")) // Check if address exist
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/address/2")) // Delete the address by id
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/address/2")) // Confirm the address is deleted
                .andExpect(status().isNotFound());
    }

    @Test // Test deleting an address and check if associated orders are also deleted
    void shouldDeleteAddressByIdAndOrdersAssociated() throws Exception {
        mockMvc.perform(get("/api/address/1")) // Check if address exist
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/order/1")) // Check if order exist and that this is the connected order
                .andExpect(MockMvcResultMatchers.jsonPath("$.address.addressId").value(1L))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/address/1")) // Delete the address by id
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/address/1")) // Check if address is deleted
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/order/1")) // Check if associated order is deleted
                .andExpect(status().isNotFound());
    }

    @Test // Test deleting an address that doesn't exist
    void shouldNotDeleteAddressNotExist() throws Exception {
        mockMvc.perform(get("/api/address/2435423"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/address/245235"))
                .andExpect(status().isNotFound());
    }
}
