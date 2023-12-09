package no.pgr209.machinefactory.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MachineDTO {
    private String machineName;
    private String machineType;
    private List<Long> subassemblyId;
}
