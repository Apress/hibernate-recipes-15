package com.apress.hibernaterecipes.chapter4.model.recipe1;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class VideoDisc extends Disc {
    @Column
    String director;
    @Column
    String language;
}
