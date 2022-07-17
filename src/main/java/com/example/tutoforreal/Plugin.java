package com.example.tutoforreal;

import java.io.IOException;

public interface Plugin {
    default void initialize() throws IOException {
        System.out.println("Initialized "+this.getClass().getName());
    }
    default String name(){return getClass().getSimpleName();}
}