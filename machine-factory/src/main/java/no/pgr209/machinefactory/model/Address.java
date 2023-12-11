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
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq_gen")
    @SequenceGenerator(name = "address_seq_gen", sequenceName = "address_seq", allocationSize = 1)
    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "address_street")
    private String addressStreet;

    @Column(name = "address_city")
    private String addressCity;

    @Column(name = "address_zip")
    private Integer addressZip;

    // A customer can have many addresses, and an address has one or more customers.
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "customer_address",
            joinColumns = @JoinColumn(name = "address_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    @JsonIgnoreProperties({"addresses", "orders"})
    private List<Customer> customers = new ArrayList<>();

    // A customer can have many orders
    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"address", "customer", "machines"})
    private List<Order> orders = new ArrayList<>();

    // Constructors, getters, setters, and methods below.
    public Address(String addressStreet, String addressCity, Integer addressZip) {
        this.addressStreet = addressStreet;
        this.addressCity = addressCity;
        this.addressZip = addressZip;
    }
}
