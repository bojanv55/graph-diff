package me.vukas.graphdiff.element.array;

import me.vukas.graphdiff.element.WritableElement;
import me.vukas.graphdiff.snapshot.util.MaterializationException;

import java.lang.reflect.Array;

public class ArrayWritableElement implements WritableElement {
    private final int index;

    public ArrayWritableElement(int index) {
        this.index = index;
    }

    @Override
    public void writeTo(Object object, Object value) {
        try {
            Array.set(object, index, value);
        }
        catch (IllegalArgumentException e){
            throw new MaterializationException(e);
        }
    }
}
