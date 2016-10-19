package com.apress.hibernaterecipes.chapter6.recipe6;

import java.util.Comparator;

public class InvertedStringComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        // natural ordering is o1.compareTo(o2)
        return o2.compareTo(o1);
    }
}
