package me.vukas.graphdiff.element.array;

import me.vukas.graphdiff.element.ReadableElement;

import java.lang.reflect.Array;

public class ArrayReadableElement implements ReadableElement {
    private final int index;

    public ArrayReadableElement(int index) {
        this.index = index;
    }

    @Override
    public Integer key() {
        return index;
    }

    @Override
    public Object value(Object object) {
        return Array.get(object, index);
    }
}
