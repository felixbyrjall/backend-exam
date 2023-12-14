package no.pgr209.machinefactory.Subassembly;

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
public class SubassemblyIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test // Fetch all subassemblies, ensure they are returned
    void shouldFetchSubassemblies() throws Exception {
        mockMvc.perform(get("/api/subassembly"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].subassemblyId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].subassemblyId").value(2));
    }

    @Test // Check that subassemblies are returned from pagination, returning the correct amount (Max: 3 per page)
    void shouldFetchSubassembliesOnPage() throws Exception {
        mockMvc.perform(get("/api/subassembly/page/0"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].subassemblyId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].subassemblyId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].subassemblyId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].subassemblyId").doesNotExist());
    }

    @Test // Fetch a subassembly by id and ensure correct values are returned from it
    void shouldFetchSubassemblyById() throws Exception {
        mockMvc.perform(get("/api/subassembly/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.subassemblyId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subassemblyName").value("Motion Control System"));
    }

    @Test // Test creating a subassembly
    void shouldCreateSubassembly() throws Exception {
        String subassemblyJson = """
        {
            "subassemblyName": "Laser Scanning Unit",
            "partId": []
        }
        """;

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.subassemblyName").value("Laser Scanning Unit"));
    }

    @Test // Test updating subassembly name
    void shouldUpdateSubassembly() throws Exception {
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

    @Test // Test update subassembly part
    void shouldUpdateSubassemblyWithAddingPart() throws Exception {
        String subassemblyJson = """
        {
            "subassemblyName": "Toner Cartridge Assembly",
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.subassemblyName").value("Toner Cartridge Assembly"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.parts[0].partId").value(2L));
    }

    @Test // Expect response to be NOT FOUND when fetching a non-existent id
    void shouldNotFetchNonExistentSubassemblyById() throws Exception {
        mockMvc.perform(get("/api/subassembly/5234"))
                .andExpect(status().isNotFound());
    }

    @Test // Expect response to be NOT FOUND when creating a subassembly with a part id that do not exist
    void shouldNotCreateSubassemblyWithInvalidPartId() throws Exception {
        String subassemblyJson = String.format("""
        {
            "subassemblyName": "Super Laser Printer knobs",
            "partId": [%d]
        }
        """, 49L);

        mockMvc.perform(post("/api/subassembly")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subassemblyJson))
                .andExpect(status().isNotFound());
    }

    @Test // Expect response to be NOT FOUND when trying to create a subassembly without required data
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

    @Test // Expect response to be NOT FOUND when updating a subassembly with a part id that do not exist
    void shouldNotUpdatesSubassemblyWithInvalidPartId() throws Exception {
        String subassemblyJson = String.format("""
        {
            "subassemblyName": "Super Laser Printer knobs",
            "partId": [%d]
        }
        """, 88L);

        mockMvc.perform(put("/api/subassembly/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subassemblyJson))
                .andExpect(status().isNotFound());
    }

    @Test // Test deleting a subassembly and confirm it is removed
    void shouldDeleteSubassemblyById() throws Exception {
        mockMvc.perform(get("/api/subassembly/2")) // Check if subassembly exist
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/subassembly/2")) // Delete the subassembly by id
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/subassembly/2")) // Check if subassembly is deleted
                .andExpect(status().isNotFound());
    }

    @Test // Test deleting a machine and check if associated machines is updated
    void shouldDeleteSubassemblyByIdAndMakeSubassemblyNullInMachines() throws Exception {
        mockMvc.perform(get("/api/subassembly/1")) // Check if subassembly exist
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/subassembly/2")) // Check if subassembly exist
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/machine/1")) // Check if machine exist and that the subassemblies in the order.
                .andExpect(MockMvcResultMatchers.jsonPath("$.subassemblies[0].subassemblyId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subassemblies[1].subassemblyId").value(2L))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/subassembly/1")) // Delete the subassembly by id
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/subassembly/2")) // Delete the subassembly by id
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/subassembly/1")) // Check if subassembly exist
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/subassembly/2")) // Check if subassembly exist
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/machine/1")) // Check if subassembly is emptied in machine
                .andExpect(MockMvcResultMatchers.jsonPath("$.subassemblies[0]").doesNotHaveJsonPath())
                .andExpect(status().isOk()); // Making sure machine still exist
    }

    @Test // Test deleting a subassembly that doesn't exist
    void shouldNotDeleteSubassemblyNotExist() throws Exception {
        mockMvc.perform(get("/api/subassembly/75624"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/subassembly/75624"))
                .andExpect(status().isNotFound());
    }
}
