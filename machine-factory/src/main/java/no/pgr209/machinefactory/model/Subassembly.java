package no.pgr209.machinefactory.model;

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
public class Subassembly {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subassembly_seq_gen")
    @SequenceGenerator(name = "subassembly_seq_gen", sequenceName = "subassembly_seq", allocationSize = 1)
    @Column(name = "subassembly_id")
    private Long subassemblyId = 0L;

    @Column(name = "subassembly_name")
    private String subassemblyName;

    @ManyToOne
    @JoinColumn(name = "machine_id")
    private Machine machine;

    // A subassembly has one or more parts
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "subassembly_id")
    private List<Part> parts = new ArrayList<>();

    // Constructors, getters, setters, and methods below.
    public Subassembly(String subassemblyName) {
        this.subassemblyName = subassemblyName;
    }
}
