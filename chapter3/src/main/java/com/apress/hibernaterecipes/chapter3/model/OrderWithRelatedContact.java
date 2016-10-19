package com.apress.hibernaterecipes.chapter3.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class OrderWithRelatedContact {
    @Id
    @GeneratedValue
    Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    RelatedContact weekdayContact;
    @ManyToOne(cascade = CascadeType.ALL)
    RelatedContact holidayContact;
}
