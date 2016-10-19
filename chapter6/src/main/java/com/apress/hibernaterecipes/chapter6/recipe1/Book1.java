package com.apress.hibernaterecipes.chapter6.recipe1;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Book1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String title;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book")
    Set<Chapter1> chapters = new HashSet<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "review")
    Set<String> reviews = new HashSet<>();
}
