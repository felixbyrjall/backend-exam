package no.pgr209.machinefactory.Subassembly;

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
public class SubassemblyIntegrationTest {

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
    void shouldFetchSubassemblies() throws Exception {
        mockMvc.perform(get("/api/subassembly"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].subassemblyId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].subassemblyId").value(2));
    }

    @Test
    void shouldFetchSubassemblyById() throws Exception {
        mockMvc.perform(get("/api/subassembly/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.subassemblyId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subassemblyName").value("Printer head"));
    }

    @Test
    void shouldCreateSubassembly() throws Exception {
        String subassemblyJson = """
        {
            "subassemblyName": "Printer tags",
            "partId": []
        }
        """;

        // Create the subassembly
        MvcResult createResult = mockMvc.perform(post("/api/subassembly")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subassemblyJson))
                .andExpect(status().isCreated())
                .andReturn();

        // Extract the subassemblyId from the response
        String responseContent = createResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(responseContent);
        int subassemblyId = jsonObject.getInt("subassemblyId");

        mockMvc.perform(get("/api/subassembly/" + subassemblyId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.subassemblyId").value(subassemblyId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subassemblyName").value("Printer tags"));
    }

    @Test
    void shouldUpdateSubassemblyWithAddingPart() throws Exception {
        String subassemblyJson = """
        {
            "subassemblyName": "Printer tags",
            "partId": [2]
        }
        """;

        // Update the subassembly
        mockMvc.perform(put("/api/subassembly/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subassemblyJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/subassembly/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.subassemblyId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subassemblyName").value("Printer tags"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.parts[0].partId").value(2L));
    }

    @Test
    void shouldUpdateSubassemblyWithNewName() throws Exception {
        String subassemblyJson = """
        {
            "subassemblyName": "Super Laser Printer knobs",
            "partId": []
        }
        """;

        // Update the subassembly
        mockMvc.perform(put("/api/subassembly/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subassemblyJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/subassembly/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.subassemblyId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subassemblyName").value("Super Laser Printer knobs"));
    }

    @Test
    void shouldNotFetchNonExistentSubassemblyById() throws Exception {
        mockMvc.perform(get("/api/subassembly/5234"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotCreateSubassemblyWithInvalidPartId() throws Exception {
        String subassemblyJson = String.format("""
        {
            "subassemblyName": "Super Laser Printer knobs",
            "partId": [%d]
        }
        """, 345245L);

        mockMvc.perform(post("/api/subassembly")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subassemblyJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotCreateSubassemblyWithEmptyData() throws Exception {
        String subassemblyJson = """
        {
            "subassemblyName": "",
            "partId": []
        }
        """;

        mockMvc.perform(post("/api/subassembly")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subassemblyJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotUpdatesSubassemblyWithInvalidPartId() throws Exception {
        String subassemblyJson = String.format("""
        {
            "subassemblyName": "Super Laser Printer knobs",
            "partId": [%d]
        }
        """, 45632L);

        mockMvc.perform(put("/api/subassembly/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subassemblyJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteSubassemblyById() throws Exception {
        mockMvc.perform(get("/api/subassembly/2")) // Check if subassembly exist.
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/subassembly/2")) // Delete the subassembly by id.
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/subassembly/2")) // Check if subassembly is removed.
                .andExpect(status().isNotFound());
    }

    @Test // Test DELETE requests and that associated Machine is updated.
    void shouldDeleteSubassemblyByIdAndMakeSubassemblyNullInMachines() throws Exception {
        mockMvc.perform(get("/api/subassembly/1")) // Check if subassembly exist.
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/subassembly/2")) // Check if subassembly exist.
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/machine/1")) // Check if machine exist and that the subassemblies in the order.
                .andExpect(MockMvcResultMatchers.jsonPath("$.subassemblies[0].subassemblyId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subassemblies[1].subassemblyId").value(2L))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/subassembly/1")) // Delete the subassembly by id.
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/subassembly/2")) // Delete the subassembly by id.
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/subassembly/1")) // Check if subassembly exist.
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/subassembly/2")) // Check if subassembly exist.
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/machine/1")) // Check if subassembly is emptied in machine.
                .andExpect(MockMvcResultMatchers.jsonPath("$.subassemblies[0]").doesNotHaveJsonPath())
                .andExpect(status().isOk()); // Making sure machine still exist.
    }

    @Test // Test deleting a subassembly that doesn't exist
    void shouldNotDeleteSubassemblyNotExist() throws Exception {
        mockMvc.perform(get("/api/subassembly/75624"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/subassembly/75624"))
                .andExpect(status().isNotFound());
    }
}
