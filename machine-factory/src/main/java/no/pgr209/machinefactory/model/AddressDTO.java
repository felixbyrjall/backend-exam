package no.pgr209.machinefactory.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddressDTO {
    private String addressStreet;
    private String addressCity;
    private String addressZip;
    private List<Long> customerId;
}
