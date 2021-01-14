package me.vukas.graphdiff.element.array;

import me.vukas.graphdiff.element.AbstractElementReader;
import me.vukas.graphdiff.element.ReadableElement;

import java.lang.reflect.Array;
import java.util.stream.IntStream;

public class ArrayElementReader extends AbstractElementReader {
    public ArrayElementReader(AbstractElementReader next) {
        super(next);
    }

    @Override
    public boolean canReadFrom(String type) {
        return type.startsWith("[");
    }

    @Override
    public Iterable<? extends ReadableElement> elementsFor(Object object) {
        int length = Array.getLength(object);
        return (Iterable<ArrayReadableElement>) () ->
                IntStream.range(0, length)
                        .mapToObj(ArrayReadableElement::new)
                        .iterator();
    }
}
