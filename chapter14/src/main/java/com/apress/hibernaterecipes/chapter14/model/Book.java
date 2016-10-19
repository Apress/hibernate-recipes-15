package com.apress.hibernaterecipes.chapter14.model;

import lombok.*;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@NoArgsConstructor
@Entity
@EqualsAndHashCode(exclude = "author")
@ToString
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"title", "author"})
})
@NamedQueries(
        {
                @NamedQuery(name = "book.findByIdAndAuthor",
                        query = "from Book b where b.id=:id and b.author.id=:authorId"),
                @NamedQuery(name = "book.findByAuthor",
                        query = "from Book b where b.author.id=:authorId")
        }
)
@XmlRootElement(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    Integer id;
    @Column(unique = true)
    @Getter
    @Setter
    String title;
    @Getter
    @Setter
    int edition;
    @ManyToOne
    @JoinColumn(name = "author")
    @Getter
    @Setter
    Author author;
    @Getter
    @Setter
    int copiesSold;
}
