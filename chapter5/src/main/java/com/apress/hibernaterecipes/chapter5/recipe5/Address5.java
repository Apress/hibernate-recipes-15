package com.apress.hibernaterecipes.chapter5.recipe5;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
public class Address5 implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @OneToOne(mappedBy = "address6")
    Customer5 customer;
    String address;
    String city;
}
