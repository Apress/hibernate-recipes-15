package com.apress.hibernaterecipes.chapter12.recipe4;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
@Data
@NoArgsConstructor
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Chapter4 {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  int id;
  String name;

  public Chapter4(String name) {
    setName(name);
  }
}
