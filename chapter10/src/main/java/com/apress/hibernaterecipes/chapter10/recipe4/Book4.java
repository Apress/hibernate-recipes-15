package com.apress.hibernaterecipes.chapter10.recipe4;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@Data
@NoArgsConstructor
@NamedQueries({
    @NamedQuery(name = "book4.findAll", query = "from Book4 b ")
})
@FilterDefs({
    @FilterDef(name = "rank", parameters = @ParamDef(name = "rank", type = "integer"))
})
@Filters({
    @Filter(name = "rank", condition = "rank <= :rank")
})
public class Book4 {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public Integer id;
  String title;
  int rank;

  public Book4(String title, int rank) {
    this.title = title;
    this.rank = rank;
  }
}
