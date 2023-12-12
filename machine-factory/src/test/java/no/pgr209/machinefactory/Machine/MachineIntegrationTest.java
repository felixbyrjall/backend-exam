package no.pgr209.machinefactory.Machine;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MachineIntegrationTest {

    @Autowired
    DataFeedService dataFeedService;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        dataFeedService.initializeData();
    }

    @Test
    void shouldFetchMachines() throws Exception {
        mockMvc.perform(get("/api/machine"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].machineId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].machineId").value(2));
    }

    @Test
    void shouldFetchMachinesOnPage() throws Exception {
        mockMvc.perform(get("/api/machine/page/0"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].machineId").value(1));
    }

    @Test
    void shouldFetchMachineById() throws Exception {
        mockMvc.perform(get("/api/machine/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineName").value("3D printer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineType").value("Electronics"));
    }

    @Test
    void shouldCreateMachine() throws Exception {
        String machineJson = """
        {
            "machineName": "Robot printer",
            "machineType":  "Electronics",
            "subassemblyId": []
        }
        """;

        // Create the machine
        MvcResult createResult = mockMvc.perform(post("/api/machine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(machineJson))
                .andExpect(status().isCreated())
                .andReturn();

        // Extract the machineId from the response
        String responseContent = createResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(responseContent);
        int machineId = jsonObject.getInt("machineId");

        mockMvc.perform(get("/api/machine/" + machineId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineId").value(machineId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineName").value("Robot printer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineType").value("Electronics"));
    }

}
