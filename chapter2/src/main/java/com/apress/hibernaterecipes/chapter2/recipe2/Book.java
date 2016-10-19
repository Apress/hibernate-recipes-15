package com.apress.hibernaterecipes.chapter2.recipe2;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Book {
    @Id
    ISBN id;
    @Column
    String title;

    public Book() {
    }

    public Book(int ean, int group, int publisher, int titleRef, int checkDigit, String title) {
        // ean is ignored; it's always 978.
        id = new ISBN(ean, group, publisher, titleRef, checkDigit);
        this.title = title;
    }

    public ISBN getId() {
        return id;
    }

    public void setId(ISBN id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
