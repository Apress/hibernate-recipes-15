package com.apress.hibernaterecipes.chapter8.recipe2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookSummary2 {
    String bookName;
    String bookPublisher;
}
