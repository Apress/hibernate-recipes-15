package com.apress.hibernaterecipes.chapter7.recipe1;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
public class Chapter1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String title;
    String content;
}
