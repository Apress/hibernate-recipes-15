package com.apress.hibernaterecipes.chapter7.recipe4;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(exclude = "books")
@ToString(exclude = "books")
@NamedQueries({
        @NamedQuery(name = "Chapter4.findByTitle",
                query = "from Chapter4 c where c.title=:title")
})
public class Chapter4 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    int id;
    @Getter
    @Setter
    String title;
    @Getter
    @Setter
    String content;
    @ManyToMany
    @Getter
    @Setter
    Set<Book4> books = new HashSet<>();
}
