package com.apress.hibernaterecipes.chapter11.recipe2;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@NamedQueries(
    {
        @NamedQuery(name = "Book2.findLikeTitle", query = "select b from Book2 b where b.title like :title")
    }
)
public class Book2 {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;
  String title;
  double price;
}
