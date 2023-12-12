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

    @BeforeEach
    void setUp() {
        dataFeedService.initializeData();
    }

    @Test
    void shouldFetchSubassemblies() throws Exception {
        mockMvc.perform(get("/api/subassembly"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].subassembly").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].subassembly").value(2));
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
}
