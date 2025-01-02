package org.example.parsers;

public interface Parser<T> {
    T parse(String input);
}
