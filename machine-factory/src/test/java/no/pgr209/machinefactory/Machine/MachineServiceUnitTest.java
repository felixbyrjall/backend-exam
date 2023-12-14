package no.pgr209.machinefactory.Machine;

import no.pgr209.machinefactory.model.*;
import no.pgr209.machinefactory.repo.MachineRepo;
import no.pgr209.machinefactory.repo.SubassemblyRepo;
import no.pgr209.machinefactory.service.MachineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("dev")
public class MachineServiceUnitTest {

    @Autowired
    MachineService machineService;

    @MockBean
    private MachineRepo machineRepo;

    @MockBean
    private SubassemblyRepo subassemblyRepo;

    @Test
    void shouldReturnAllMachines() {
        List<Machine> mockMachines = new ArrayList<>();
        when(machineRepo.findAll()).thenReturn(mockMachines);
        List<Machine> machines = machineService.getAllMachines();

        assertEquals(mockMachines, machines);
    }

    @Test
    void shouldReturnMachineById() {
        Machine mockMachine = new Machine();
        when(machineRepo.findById(1L)).thenReturn(Optional.of(mockMachine));
        Machine machine = machineService.getMachineById(1L);

        assertEquals(mockMachine, machine);
    }

    @Test // Comprehensive mock & unit-testing, creating a machine.
    void shouldCreateMachine() {
        MachineDTO machineDTO = new MachineDTO();
        machineDTO.setMachineName("Robot Printer");
        machineDTO.setMachineType("Electronics");
        List<Long> subassemblies = List.of(1L, 2L);
        machineDTO.setSubassemblyId(subassemblies);

        Machine mockMachine = new Machine("Robot Printer", "Electronics");
        Subassembly subassemblyOne = new Subassembly( "Printer knob");
        Subassembly subassemblyTwo = new Subassembly( "Printer logo");

        when(machineRepo.existsById(1L)).thenReturn(true);
        when(machineRepo.findById(1L)).thenReturn(Optional.of(mockMachine));
        when(subassemblyRepo.existsById(any())).thenReturn(true);
        when(subassemblyRepo.findAllById(subassemblies)).thenReturn(List.of(subassemblyOne, subassemblyTwo));

        Machine createdMachine = new Machine();
        createdMachine.setMachineName("Robot Printer");
        createdMachine.setMachineType("Electronics");
        createdMachine.setSubassemblies(List.of(subassemblyOne, subassemblyTwo));
        when(machineRepo.save(any())).thenReturn(createdMachine);

        Machine resultMachine = machineService.createMachine(machineDTO);

        assertThat(resultMachine).isNotNull();
        assertThat(resultMachine.getMachineName()).isEqualTo(mockMachine.getMachineName());
        assertThat(resultMachine.getMachineType()).isEqualTo(mockMachine.getMachineType());
        assertThat(resultMachine.getSubassemblies()).containsExactlyInAnyOrderElementsOf(List.of(subassemblyOne, subassemblyTwo));
    }
}
