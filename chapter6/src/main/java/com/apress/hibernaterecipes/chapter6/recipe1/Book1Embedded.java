package com.apress.hibernaterecipes.chapter6.recipe1;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Book1Embedded {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String title;
    @ElementCollection
    Set<Chapter1Embedded> chapters = new HashSet<>();
    @ElementCollection
    @Column(name = "review")
    Set<String> reviews = new HashSet<>();
}
