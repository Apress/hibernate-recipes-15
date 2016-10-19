package com.apress.hibernaterecipes.chapter8.recipe2;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name = "Book2.byName",
                query = "from Book2 b where b.name=:name")
})
public class Book2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(unique = true, nullable = false)
    String name;
    @ManyToOne(optional = false)
    Publisher2 publisher2;

    public Book2(String name) {
        setName(name);
    }
}
