package com.apress.hibernaterecipes.chapter2.recipe0;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Product {
    @Id
    long sku;
    @Column
    String title;
    @Column
    String description;

    public long getSku() {
        return sku;
    }

    public void setSku(long id) {
        this.sku = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
