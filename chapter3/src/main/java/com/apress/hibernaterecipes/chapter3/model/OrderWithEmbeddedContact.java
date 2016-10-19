package com.apress.hibernaterecipes.chapter3.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class OrderWithEmbeddedContact {
    @Id
    @GeneratedValue
    Long id;
    @Embedded
    EmbeddedContact weekdayContact;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "holidayname")),
            @AttributeOverride(name = "address", column = @Column(name = "holidayaddress")),
            @AttributeOverride(name = "phone", column = @Column(name = "holidayphone")),
    })
    EmbeddedContact holidayContact;
}
