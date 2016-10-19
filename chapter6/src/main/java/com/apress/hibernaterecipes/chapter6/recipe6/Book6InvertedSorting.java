package com.apress.hibernaterecipes.chapter6.recipe6;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SortComparator;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Data
@NoArgsConstructor
public class Book6InvertedSorting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String title;
    @ElementCollection(fetch = FetchType.EAGER)
    @SortComparator(InvertedStringComparator.class)
    @Column(name = "review")
    Set<String> reviews = new TreeSet<>();
}
