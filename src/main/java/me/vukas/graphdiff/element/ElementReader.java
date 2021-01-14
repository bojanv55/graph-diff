package me.vukas.graphdiff.element;

public interface ElementReader {
    boolean canReadFrom(String type);
    ElementReader readerFor(String type);
    Iterable<? extends ReadableElement> elementsFor(Object object);
}
