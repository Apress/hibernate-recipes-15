package com.apress.hibernaterecipes.chapter3.recipe2;

import lombok.Data;

@Data
public class Order {
    long id;
    Contact weekdayContact;
    Contact holidayContact;
}
