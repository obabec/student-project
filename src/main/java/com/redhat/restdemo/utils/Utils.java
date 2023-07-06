package com.redhat.restdemo.utils;

public class Utils {
    public static <T> Long countIterable(Iterable<T> objects) {
        Long counter = 0L;
        for (Object object: objects) {
            counter++;
        }
        return counter;
    }
}
