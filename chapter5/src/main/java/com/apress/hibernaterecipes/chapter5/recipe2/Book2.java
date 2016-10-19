package com.apress.hibernaterecipes.chapter5.recipe2;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class Book2 {
    @Id
    @GeneratedValue()
    Long id;
    @Column(nullable = false)
    String title;
    @Column(nullable = false)
    BigDecimal price;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "publisher2_book2")
    Publisher2 publisher;
}
