package com.apress.hibernaterecipes.chapter6.recipe6;

import com.apress.hibernaterecipes.chapter6.recipe1.Chapter1;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
public class Book6NaturalSorting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String title;
    @ElementCollection(fetch = FetchType.EAGER)
    @SortNatural
    @Column(name = "review")
    Set<String> reviews = new TreeSet<>();
}
