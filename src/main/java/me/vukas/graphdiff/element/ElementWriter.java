package me.vukas.graphdiff.element;

import me.vukas.graphdiff.snapshot.Schema;

import java.util.Iterator;

public interface ElementWriter {
    boolean canWriteTo(String type);
    ElementWriter writerFor(String type);
    Object newInstance(Schema schema);
    Iterable<? extends WritableElement> elementsFor(Iterator<Object> keysIterator);
}
