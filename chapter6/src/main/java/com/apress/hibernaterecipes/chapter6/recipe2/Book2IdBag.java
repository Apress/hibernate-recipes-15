package com.apress.hibernaterecipes.chapter6.recipe2;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Book2IdBag {
    int id;
    String title;
    List<String> reviews = new ArrayList<>();
}