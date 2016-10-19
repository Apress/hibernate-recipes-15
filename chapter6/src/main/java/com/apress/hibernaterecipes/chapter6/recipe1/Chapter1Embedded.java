package com.apress.hibernaterecipes.chapter6.recipe1;

import lombok.*;
import lombok.experimental.Builder;

import javax.persistence.*;

@Embeddable
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Chapter1Embedded {
    @Getter
    @Setter
    String title;
    @Getter
    @Setter
    String content;
}
