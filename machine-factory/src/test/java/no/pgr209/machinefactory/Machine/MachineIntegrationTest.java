package no.pgr209.machinefactory.Machine;

import no.pgr209.machinefactory.service.DataFeedService;
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
    DataFeedService dataFeedService;

    @Autowired
    MockMvc mockMvc;

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

    @Test
    void shouldUpdateMachineWithAddingSubassembly() throws Exception {
        String machineJson = """
        {
            "machineName": "Robot printer",
            "machineType":  "Electronics",
            "subassemblyId": [1]
        }
        """;

        // update the machine
        mockMvc.perform(put("/api/machine/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(machineJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/machine/2"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineId").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineName").value("Robot printer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineType").value("Electronics"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subassemblies[0].subassemblyId").value(1L));
    }

    @Test
    void shouldUpdateMachineWithNewInfo() throws Exception {
        String machineJson = """
        {
            "machineName": "Quantum printer",
            "machineType":  "Qubits",
            "subassemblyId": []
        }
        """;

        // update the machine
        mockMvc.perform(put("/api/machine/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(machineJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/machine/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineName").value("Quantum printer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineType").value("Qubits"));
    }

    @Test
    void shouldNotFetchNonExistentMachineById() throws Exception {
        mockMvc.perform(get("/api/machine/45323"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotCreateMachineWithInvalidSubassemblyId() throws Exception {
        String machineJson = String.format("""
        {
            "machineName": "Robot printer",
            "machineType":  "Electronics",
            "subassemblyId": [%d]
        }
        """, 345245L);

        mockMvc.perform(post("/api/machine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(machineJson))
                .andExpect(status().isNotFound());
    }

    @Test
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

    @Test
    void shouldNotUpdateMachineWithInvalidSubassemblyId() throws Exception {
        String machineJson = String.format("""
        {
            "machineName": "Robot printer",
            "machineType":  "Electronics",
            "subassemblyId": [%d]
        }
        """, 34234L);

        mockMvc.perform(put("/api/machine/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(machineJson))
                .andExpect(status().isNotFound());
    }

    @Test // Test DELETE request for machines.
    void shouldDeleteMachineById() throws Exception {
        mockMvc.perform(get("/api/machine/2")) // Check if machine exist.
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/machine/2")) // Delete the machine by id.
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/machine/2")) // Check if machine is removed.
                .andExpect(status().isNotFound());
    }

    @Test // Test DELETE requests and that associated Orders are deleted.
    void shouldDeleteMachineByIdAndOrdersAssociated() throws Exception {
        mockMvc.perform(get("/api/machine/1")) // Check if machine exist.
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/order/1")) // Check if order exist and that this is the machine in the order.
                .andExpect(MockMvcResultMatchers.jsonPath("$.machines[0].machineId").value(1L))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/machine/1")) // Delete the machine by id.
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/machine/1")) // Check if machine is removed.
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/order/1")) // Check if associated order is removed.
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
