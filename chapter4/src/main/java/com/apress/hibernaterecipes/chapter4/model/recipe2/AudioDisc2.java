package com.apress.hibernaterecipes.chapter4.model.recipe2;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AudioDisc2 extends Disc2 {
    @Column
    int trackCount;
    @Column
    String artist;
}
