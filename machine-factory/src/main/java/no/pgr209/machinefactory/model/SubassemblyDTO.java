package no.pgr209.machinefactory.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubassemblyDTO {
    private String subassemblyName;
    private List<Long> partId;
}
