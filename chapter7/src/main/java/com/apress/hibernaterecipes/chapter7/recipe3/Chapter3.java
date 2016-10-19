package com.apress.hibernaterecipes.chapter7.recipe3;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(exclude = "book3")
@ToString(exclude = "book3")
public class Chapter3 {
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
    Book3 book3;
}
