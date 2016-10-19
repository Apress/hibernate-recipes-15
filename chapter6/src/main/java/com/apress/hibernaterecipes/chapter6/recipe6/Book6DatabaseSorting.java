package com.apress.hibernaterecipes.chapter6.recipe6;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Data
@NoArgsConstructor
public class Book6DatabaseSorting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String title;
    @ElementCollection(fetch = FetchType.EAGER)
    @OrderBy("review")
    @Column(name = "review")
    Set<String> reviews = new HashSet<>();
}
