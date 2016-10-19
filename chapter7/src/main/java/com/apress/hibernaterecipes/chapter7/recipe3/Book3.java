package com.apress.hibernaterecipes.chapter7.recipe3;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Book3 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String title;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book3_chapter3")
    Set<Chapter3> chapters = new HashSet<>();
}