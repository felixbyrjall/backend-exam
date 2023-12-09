package no.pgr209.machinefactory.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomerDTO {
    private String customerName;
    private String customerEmail;
    private List<Long> addressId;
    private List<Long> orderId;
}
