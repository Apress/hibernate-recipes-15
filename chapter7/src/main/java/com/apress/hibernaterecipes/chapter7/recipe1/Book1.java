package com.apress.hibernaterecipes.chapter7.recipe1;

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
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id")
    Set<Chapter1> chapters = new HashSet<>();
}