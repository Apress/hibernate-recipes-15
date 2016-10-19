package com.apress.hibernaterecipes.chapter6.recipe3;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class Book3 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Getter
    @Setter
    String title;
    @ElementCollection
    @OrderColumn(columnDefinition = "int", name = "order_column")
    @Column(name = "review")
    List<String> reviews = new ArrayList<>();
}