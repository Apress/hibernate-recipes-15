package com.apress.hibernaterecipes.chapter8.recipe2;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name = "Publisher2.byName",
                query = "from Publisher2 p where p.name=:name")
})
public class Publisher2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(unique = true, nullable = false)
    String name;

    public Publisher2(String name) {
        setName(name);
    }
}
