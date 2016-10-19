package com.apress.hibernaterecipes.chapter5.recipe1;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class ReaderErrata1 {
    @Id
    @GeneratedValue()
    Long id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    Book1 book;
    @Temporal(TemporalType.TIMESTAMP)
    Date submitted;
    @Column(nullable = false)
    String content;
}
