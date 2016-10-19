package com.apress.hibernaterecipes.chapter6.recipe5;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@NoArgsConstructor
@Data
public class Book5 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Getter
    @Setter
    String title;
    @ElementCollection(targetClass = String.class)
    @Column(name = "reference")
    @MapKeyColumn(name = "topic")
    Map<String, String> topicMap = new HashMap<>();
}