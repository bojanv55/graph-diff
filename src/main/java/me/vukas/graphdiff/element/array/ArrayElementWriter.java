package me.vukas.graphdiff.element.array;

import me.vukas.graphdiff.element.AbstractElementWriter;
import me.vukas.graphdiff.element.WritableElement;
import me.vukas.graphdiff.snapshot.Schema;
import me.vukas.graphdiff.snapshot.util.MaterializationException;

import java.lang.reflect.Array;
import java.util.Iterator;

public class ArrayElementWriter extends AbstractElementWriter {
    public ArrayElementWriter(AbstractElementWriter next) {
        super(next);
    }

    @Override
    public boolean canWriteTo(String type) {
        return type.startsWith("[");
    }

    @Override
    public Object newInstance(Schema schema) {
        Object instance;
        try {
            String element = schema.getType().substring(1);
            if ("B".equals(element)) {
                instance = new byte[schema.keyCount()];
            } else if ("S".equals(element)) {
                instance = new short[schema.keyCount()];
            } else if ("I".equals(element)) {
                instance = new int[schema.keyCount()];
            } else if ("J".equals(element)) {
                instance = new long[schema.keyCount()];
            } else if ("F".equals(element)) {
                instance = new float[schema.keyCount()];
            } else if ("D".equals(element)) {
                instance = new double[schema.keyCount()];
            } else if ("Z".equals(element)) {
                instance = new boolean[schema.keyCount()];
            } else if ("C".equals(element)) {
                instance = new char[schema.keyCount()];
            } else if (element.startsWith("L")) {
                element = element.substring(1, element.length() - 1);
                instance = Array.newInstance(Class.forName(element), schema.keyCount());
            } else {
                instance = Array.newInstance(Class.forName(element), schema.keyCount());
            }
        }
        catch (Exception e){
            throw new MaterializationException(e);
        }
        return instance;
    }

    @Override
    public Iterable<? extends WritableElement> elementsFor(Iterator<Object> keysIterator) {
        return (Iterable<ArrayWritableElement>) () -> new Iterator<>() {
            @Override
            public boolean hasNext() {
                return keysIterator.hasNext();
            }

            @Override
            public ArrayWritableElement next() {
                return new ArrayWritableElement((int)keysIterator.next());
            }
        };
    }
}
