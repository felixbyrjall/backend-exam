package no.pgr209.machinefactory.Part;

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
public class PartIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test // Fetch all parts, ensure they are returned
    void shouldFetchParts() throws Exception {
        mockMvc.perform(get("/api/part"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].partId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].partId").value(2));
    }

    @Test // Check that parts are returned from pagination, returning the correct amount (Max: 3 per page)
    void shouldFetchPartsOnPage() throws Exception {
        mockMvc.perform(get("/api/part/page/0"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].partId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].partId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].partId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].partId").doesNotExist());
    }

    @Test // Fetch a part by id and ensure correct values are returned from it
    void shouldFetchPartById() throws Exception {
        mockMvc.perform(get("/api/part/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.partId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.partName").value("Fasteners"));
    }

    @Test // Test creating a part
    void shouldCreatePart() throws Exception {
        String partJson = """
        {
            "partName": "Capacitor"
        }
        """;

        MvcResult createResult = mockMvc.perform(post("/api/part")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(partJson))
                .andExpect(status().isCreated())
                .andReturn();

        // Extract the partId from the response
        String responseContent = createResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(responseContent);
        int partId = jsonObject.getInt("partId");

        mockMvc.perform(get("/api/part/" + partId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.partId").value(partId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.partName").value("Capacitor"));
    }

    @Test // Test updating a part
    void shouldUpdatePart() throws Exception {
        String partJson = """
        {
            "partName": "Resistor"
        }
        """;

        mockMvc.perform(put("/api/part/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(partJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/part/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.partId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.partName").value("Resistor"));
    }

    @Test // Expect response to be NOT FOUND when fetching a non-existent id
    void shouldNotFetchNonExistentPartById() throws Exception {
        mockMvc.perform(get("/api/part/16589"))
                .andExpect(status().isNotFound());
    }

    @Test // Expect response to be NOT FOUND when creating a part without required data
    void shouldNotCreatePartWithEmptyData() throws Exception {
        String partJson = """
        {
            "partName": ""
        }
        """;

        mockMvc.perform(post("/api/part")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(partJson))
                .andExpect(status().isNotFound());
    }

    @Test // Test deleting a part and confirm it is removed
    void shouldDeletePartById() throws Exception {
        mockMvc.perform(get("/api/part/2")) // Check if part exist
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/part/2")) // Delete the part by id
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/part/2")) // Check if part is deleted
                .andExpect(status().isNotFound());
    }

    @Test // Test deleting a part and check if associated subassemblies are also updated
    void shouldDeletePartByIdAndMakePartsNullInSubassembly() throws Exception {
        mockMvc.perform(get("/api/part/1")) // Check if part exist
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/subassembly/5")) // Check if subassembly exist and that the part is in it
                .andExpect(MockMvcResultMatchers.jsonPath("$.parts[0].partId").value(1L))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/part/1")) // Delete the part by id
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/part/1")) // Check if part is deleted
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/subassembly/5")) // Check if parts is emptied in subassembly
                .andExpect(MockMvcResultMatchers.jsonPath("$.parts[0]").doesNotHaveJsonPath())
                .andExpect(status().isOk());
    }

    @Test // Test deleting a part that doesn't exist
    void shouldNotDeletePartNotExist() throws Exception {
        mockMvc.perform(get("/api/part/25489"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/part/25489"))
                .andExpect(status().isNotFound());
    }
}
