package no.pgr209.machinefactory.model;

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
@Table(name = "\"order\"")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq_gen")
    @SequenceGenerator(name = "order_seq_gen", sequenceName = "order_seq", allocationSize = 1)
    @Column(name = "order_id")
    private Long orderId = 0L;

    // Order date
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    // Who ordered - A customer can have many orders.
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // Where is order shipping to - An address can have many orders.
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    // What is ordered - An order has one or more machines
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "machine_id")
    private List<Machine> machines = new ArrayList<>();

    // Constructors, getters, setters, and methods below.
    public void Machine(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
