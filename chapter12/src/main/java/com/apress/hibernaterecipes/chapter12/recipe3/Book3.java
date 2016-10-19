package com.apress.hibernaterecipes.chapter12.recipe3;

import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Data
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Book3 {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  int id;
  String title;
  @ManyToOne
  Publisher3 publisher;
}
