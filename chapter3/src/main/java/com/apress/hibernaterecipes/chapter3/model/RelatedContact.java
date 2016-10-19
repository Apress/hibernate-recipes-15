package com.apress.hibernaterecipes.chapter3.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class RelatedContact {
    @Id
    @GeneratedValue
    Long id;
    @Column
    String name;
    @Column
    String address;
    @Column
    String phone;
}
