package no.pgr209.machinefactory.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "part_seq_gen")
    @SequenceGenerator(name = "part_seq_gen", sequenceName = "part_seq", allocationSize = 1)
    @Column(name = "part_id")
    private Long partId = 0L;

    @Column(name = "part_name")
    private String partName;

    // Constructors, getters, setters, and methods below.
    public Part(String partName) {
        this.partName = partName;
    }
}
