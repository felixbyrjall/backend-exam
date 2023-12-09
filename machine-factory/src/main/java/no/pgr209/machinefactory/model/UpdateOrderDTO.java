package no.pgr209.machinefactory.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class UpdateOrderDTO {
    private Long customerId;
    private Long addressId;
    private List<Long> machineId;
    private LocalDateTime orderDate;
}
