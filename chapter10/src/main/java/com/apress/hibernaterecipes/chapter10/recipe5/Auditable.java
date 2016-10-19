package com.apress.hibernaterecipes.chapter10.recipe5;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@MappedSuperclass
public class Auditable {
  @Getter
  @Setter
  @Temporal(TemporalType.TIMESTAMP)
  Date createDate;
}
