package no.pgr209.machinefactory.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "addressId"
)
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
    @ManyToMany(mappedBy = "addresses", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"orders", "addresses"})
    private List<Customer> customers = new ArrayList<>();

    // Constructors, getters, setters, and methods below.
    public Address(String addressStreet, String addressCity, Integer addressZip) {
        this.addressStreet = addressStreet;
        this.addressCity = addressCity;
        this.addressZip = addressZip;
    }
}
