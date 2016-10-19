package com.apress.hibernaterecipes.chapter5.recipe3;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.FetchProfiles;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@FetchProfiles({
        @FetchProfile(name = "errata-with-book",
                fetchOverrides = {
                        @FetchProfile.FetchOverride(association = "book",
                                entity = ReaderErrata3Lazy.class,
                                mode = org.hibernate.annotations.FetchMode.JOIN)
                })
})
public class ReaderErrata3Lazy {
    @Id
    @GeneratedValue()
    Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    Book3 book;
    @Temporal(TemporalType.TIMESTAMP)
    Date submitted;
    @Column(nullable = false)
    String content;
}
