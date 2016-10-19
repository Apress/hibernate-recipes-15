package com.apress.hibernaterecipes.chapter5.recipe3;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Book3 {
    @Id
    @GeneratedValue()
    Long id;
    @Column(nullable = false)
    String title;
}
