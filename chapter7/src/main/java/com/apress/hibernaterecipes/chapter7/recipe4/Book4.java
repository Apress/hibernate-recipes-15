package com.apress.hibernaterecipes.chapter7.recipe4;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name = "Book4.findByTitle",
                query = "from Book4 b where b.title=:title")
})
public class Book4 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String title;
    @ManyToMany
    Set<Chapter4> chapters = new HashSet<>();
}