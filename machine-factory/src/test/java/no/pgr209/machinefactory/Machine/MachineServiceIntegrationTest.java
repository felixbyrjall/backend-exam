package no.pgr209.machinefactory.Machine;

import no.pgr209.machinefactory.model.MachineDTO;
import no.pgr209.machinefactory.model.Part;
import no.pgr209.machinefactory.model.Subassembly;
import no.pgr209.machinefactory.model.SubassemblyDTO;
import no.pgr209.machinefactory.repo.*;
import no.pgr209.machinefactory.service.MachineService;
import no.pgr209.machinefactory.service.SubassemblyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class MachineServiceIntegrationTest {

    @Autowired
    MachineService machineService;

    @Autowired
    SubassemblyService subassemblyService;

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    MachineRepo machineRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    SubassemblyRepo subassemblyRepo;

    @BeforeEach // Ensure clean DB.
    void setUp() {
        orderRepo.deleteAll();
        customerRepo.deleteAll();
        addressRepo.deleteAll();
        machineRepo.deleteAll();
    }
    @Test
    void shouldCreateAndFetchMachine() {
        MachineDTO machine = new MachineDTO();
        Subassembly subassemblyOne = subassemblyRepo.save(new Subassembly("Printer rod"));
        Subassembly subassemblyTwo = subassemblyRepo.save(new Subassembly("Printer knob"));
        System.out.println(subassemblyTwo.getSubassemblyId());
        System.out.println(subassemblyOne.getSubassemblyId());

        List<Long> subassemblies = new ArrayList<>();
        subassemblies.add(subassemblyOne.getSubassemblyId());
        subassemblies.add(subassemblyTwo.getSubassemblyId());
        System.out.println(subassemblies);

        machine.setMachineName("Robot printer");
        machine.setMachineType("Electronics");
        machine.setSubassemblyId(subassemblies);
        machineService.createMachine(machine);

        var machines = machineService.getAllMachines();

        assertEquals(1, machines.size());
        assertEquals("Robot printer", machines.get(0).getMachineName());
        assertEquals("Electronics", machines.get(0).getMachineType());
        assertEquals("Printer rod", machines.get(0).getSubassemblies().get(0).getSubassemblyName());
        assertEquals("Printer knob", machines.get(0).getSubassemblies().get(1).getSubassemblyName());
    }

}
