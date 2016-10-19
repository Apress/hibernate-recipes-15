package com.apress.hibernaterecipes.chapter3.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class NonEmbeddedOrder {
    @Id
    @GeneratedValue
    Long id;
    @Column
    String weekdayRecipientName;
    @Column
    String weekdayPhone;
    @Column
    String weekdayAddress;
    @Column
    String holidayRecipientName;
    @Column
    String holidayPhone;
    @Column
    String holidayAddress;

}
