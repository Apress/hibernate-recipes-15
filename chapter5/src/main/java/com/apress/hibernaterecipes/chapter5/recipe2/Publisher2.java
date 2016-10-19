package com.apress.hibernaterecipes.chapter5.recipe2;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Publisher2 {
    @Id
    @GeneratedValue()
    Long id;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    String address;
    @OneToMany
    List<Book2> books;
}
