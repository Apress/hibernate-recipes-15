package com.apress.hibernaterecipes.chapter13.recipe3;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class Book3Timestamp {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  int id;
  @Column(unique = true)
  String title;
  @Version
  Timestamp timestamp;
}
