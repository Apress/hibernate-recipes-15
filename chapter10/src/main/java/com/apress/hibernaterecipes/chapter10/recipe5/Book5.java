package com.apress.hibernaterecipes.chapter10.recipe5;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Book5 extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public Integer id;
  String title;
  int rank;

  public Book5(String title, int rank) {
    this.title = title;
    this.rank = rank;
  }
}
