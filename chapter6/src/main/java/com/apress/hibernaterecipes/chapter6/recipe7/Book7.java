package com.apress.hibernaterecipes.chapter6.recipe7;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Book7 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String title;
    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "review")
    Set<String> reviews = new HashSet<>();
}