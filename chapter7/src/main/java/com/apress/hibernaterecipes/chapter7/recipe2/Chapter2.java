package com.apress.hibernaterecipes.chapter7.recipe2;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(exclude = "book2")
@ToString(exclude = "book2")
public class Chapter2 {
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
    @ManyToOne(optional = false)
    @Getter
    @Setter
    Book2 book2;
}
