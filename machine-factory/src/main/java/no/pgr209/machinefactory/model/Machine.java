package no.pgr209.machinefactory.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "machine_seq_gen")
    @SequenceGenerator(name = "machine_seq_gen", sequenceName = "machine_seq", allocationSize = 1)
    @Column(name = "machine_id")
    private Long machineId = 0L;

    @Column(name = "machine_name")
    private String machineName;

    @Column(name = "machine_type")
    private String machineType;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "order_machine",
            joinColumns = @JoinColumn(name = "machine_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    @JsonIgnoreProperties("machines")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "subassembly_id")
    private List<Subassembly> subassemblies = new ArrayList<>();

    // Constructors, getters, setters, and methods below.
    public Machine(String machineName, String machineType) {
        this.machineName = machineName;
        this.machineType = machineType;
    }
}
