package no.pgr209.machinefactory.Part;

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
public class PartIntegrationTest {

    @Autowired
    DataFeedService dataFeedService;

    @Autowired
    MockMvc mockMvc;

    // Feed in-memory DB with sample data from DataFeedService.
    @BeforeEach
    void setUp() {
        dataFeedService.initializeData();
    }

    @Test
    void shouldFetchParts() throws Exception {
        mockMvc.perform(get("/api/part"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].partId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].partId").value(2));
    }

    @Test
    void shouldFetchPartsOnPage() throws Exception {
        mockMvc.perform(get("/api/part/page/0"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].partId").value(1));
    }

    @Test
    void shouldFetchPartById() throws Exception {
        mockMvc.perform(get("/api/part/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.partId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.partName").value("Printer nozzle"));
    }

    @Test
    void shouldCreatePart() throws Exception {
        String partJson = """
        {
            "partName": "Capacitor"
        }
        """;

        // Create the part
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

    @Test
    void shouldUpdatePartWithNewInfo() throws Exception {
        String partJson = """
        {
            "partName": "Resistor"
        }
        """;

        // update the part
        mockMvc.perform(put("/api/part/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(partJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/part/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.partId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.partName").value("Resistor"));
    }

    @Test
    void shouldNotFetchNonExistentPartById() throws Exception {
        mockMvc.perform(get("/api/part/12345"))
                .andExpect(status().isNotFound());
    }

    @Test
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

    @Test // Test DELETE request for parts.
    void shouldDeletePartById() throws Exception {
        mockMvc.perform(get("/api/part/2")) // Check if part exist.
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/part/2")) // Delete the part by id.
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/part/2")) // Check if part is removed.
                .andExpect(status().isNotFound());
    }

    @Test // Test DELETE requests and that associated subassemblies are updated.
    void shouldDeletePartByIdAndMakePartsNullInSubassembly() throws Exception {
        mockMvc.perform(get("/api/part/3")) // Check if part exist.
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/subassembly/4")) // Check if machine exist and that the subassemblies in the order.
                .andExpect(MockMvcResultMatchers.jsonPath("$.parts[0].partId").value(3L))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/part/3")) // Delete the part by id.
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/part/3")) // Check if part is deleted.
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/subassembly/4")) // Check if parts is emptied in subassembly.
                .andExpect(MockMvcResultMatchers.jsonPath("$.parts[0]").doesNotHaveJsonPath())
                .andExpect(status().isOk());
    }

    @Test // Test deleting a part that doesn't exist
    void shouldNotDeletePartNotExist() throws Exception {
        mockMvc.perform(get("/api/part/12345"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/part/12345"))
                .andExpect(status().isNotFound());
    }
}
