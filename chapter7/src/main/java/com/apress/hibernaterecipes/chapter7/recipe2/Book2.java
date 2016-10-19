package com.apress.hibernaterecipes.chapter7.recipe2;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Book2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String title;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book2")
    Set<Chapter2> chapters = new HashSet<>();
}