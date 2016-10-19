package com.apress.hibernaterecipes.chapter5.recipe4;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
public class Customer4 implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    @OneToOne(cascade = CascadeType.ALL)
    private Address4 address4;
}
