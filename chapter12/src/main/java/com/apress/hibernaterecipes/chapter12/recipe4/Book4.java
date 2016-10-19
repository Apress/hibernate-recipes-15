package com.apress.hibernaterecipes.chapter12.recipe4;

import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Book4 {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  int id;
  String title;
  @OneToMany
  @Cascade(CascadeType.ALL)
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  Set<Chapter4> chapters = new HashSet<>();
}