package no.pgr209.machinefactory.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq_gen")
    @SequenceGenerator(name = "order_seq_gen", sequenceName = "order_seq", allocationSize = 1)
    @Column(name = "order_id")
    private Long orderId = 0L;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    // An order has one or more machines
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "machine_id")
    private List<Machine> machines = new ArrayList<>();

    // Constructors, getters, setters, and methods below.
    public void Machine(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
