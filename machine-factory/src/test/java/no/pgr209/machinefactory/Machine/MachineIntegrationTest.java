package no.pgr209.machinefactory.Machine;

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
public class MachineIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test // Fetch all machines, ensure they are returned
    void shouldFetchMachines() throws Exception {
        mockMvc.perform(get("/api/machine"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].machineId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].machineId").value(2));
    }

    @Test // Check that machines are returned from pagination, returning the correct amount (Max: 3 per page)
    void shouldFetchMachinesOnPage() throws Exception {
        mockMvc.perform(get("/api/machine/page/0"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].machineId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].machineId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].machineId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].machineId").doesNotExist());
    }

    @Test // Fetch a machine by id and ensure correct values are returned from it
    void shouldFetchMachineById() throws Exception {
        mockMvc.perform(get("/api/machine/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineName").value("3D printer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineType").value("Electronics"));
    }

    @Test // Test creating a machine
    void shouldCreateMachine() throws Exception {
        String machineJson = """
        {
            "machineName": "Microcontroller Programmer",
            "machineType":  "Electronics",
            "subassemblyId": []
        }
        """;

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineName").value("Microcontroller Programmer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineType").value("Electronics"));
    }

    @Test // Test updating a Machine with a subassembly
    void shouldUpdateMachineWithAddingSubassembly() throws Exception {
        String machineJson = """
        {
            "machineName": "Surface Mount Technology Machine",
            "machineType":  "Electronics",
            "subassemblyId": [1]
        }
        """;

        mockMvc.perform(put("/api/machine/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(machineJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/machine/2"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineId").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineName").value("Surface Mount Technology Machine"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineType").value("Electronics"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subassemblies[0].subassemblyId").value(1L));
    }

    @Test // Test deleting a customer that doesn't exist
    void shouldUpdateMachineWithNewInfo() throws Exception {
        String machineJson = """
        {
            "machineName": "Pick and Place Machine",
            "machineType":  "Assembly",
            "subassemblyId": []
        }
        """;

        mockMvc.perform(put("/api/machine/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(machineJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/machine/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineName").value("Pick and Place Machine"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineType").value("Assembly"));
    }

    @Test // Expect response to be NOT FOUND when fetching a non-existent id
    void shouldNotFetchNonExistentMachineById() throws Exception {
        mockMvc.perform(get("/api/machine/45323"))
                .andExpect(status().isNotFound());
    }

    @Test // Expect response to be NOT FOUND when creating a machine with a subassembly id that do not exist
    void shouldNotCreateMachineWithInvalidSubassemblyId() throws Exception {
        String machineJson = String.format("""
        {
            "machineName": "Soldering Robot",
            "machineType":  "Assembly",
            "subassemblyId": [%d]
        }
        """, 97L);

        mockMvc.perform(post("/api/machine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(machineJson))
                .andExpect(status().isNotFound());
    }

    @Test // Expect response to be NOT FOUND when trying to create a machine without required data
    void shouldNotCreateMachineWithEmptyData() throws Exception {
        String machineJson = """
        {
            "machineName": "",
            "machineType":  "",
            "subassemblyId": []
        }
        """;

        mockMvc.perform(post("/api/machine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(machineJson))
                .andExpect(status().isNotFound());
    }

    @Test  // Expect response to be NOT FOUND when updating a machine with a subassembly id that do not exist
    void shouldNotUpdateMachineWithInvalidSubassemblyId() throws Exception {
        String machineJson = String.format("""
        {
            "machineName": "Robot printer",
            "machineType":  "Electronics",
            "subassemblyId": [%d]
        }
        """, 67);

        mockMvc.perform(put("/api/machine/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(machineJson))
                .andExpect(status().isNotFound());
    }

    @Test // Test deleting a machine and confirm it is removed
    void shouldDeleteMachineById() throws Exception {
        mockMvc.perform(get("/api/machine/2")) // Check if machine exist.
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/machine/2")) // Delete the machine by id.
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/machine/2")) // Check if machine is deleted
                .andExpect(status().isNotFound());
    }

    @Test // Test deleting a machine and check if associated orders are also deleted
    void shouldDeleteMachineByIdAndOrdersAssociated() throws Exception {
        mockMvc.perform(get("/api/machine/1")) // Check if machine exist.
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/order/1")) // Check if order exist and that this is the machine in the order
                .andExpect(MockMvcResultMatchers.jsonPath("$.machines[0].machineId").value(1L))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/machine/1")) // Delete the machine by id
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/machine/1")) // Check if machine is deleted
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/order/1")) // Check if associated order is deleted
                .andExpect(status().isNotFound());
    }

    @Test // Test deleting a machine that doesn't exist
    void shouldNotDeleteMachineNotExist() throws Exception {
        mockMvc.perform(get("/api/machine/32235"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/machine/32235"))
                .andExpect(status().isNotFound());
    }
}
