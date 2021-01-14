package me.vukas.graphdiff.element.object;

import me.vukas.graphdiff.element.ElementReadException;
import me.vukas.graphdiff.element.ReadableElement;

import java.lang.reflect.Field;

public class ObjectReadableElement implements ReadableElement, Comparable<ObjectReadableElement> {
    private final Field field;
    private final int depth;

    public ObjectReadableElement(Field field, int depth) {
        this.field = field;
        this.depth = depth;
    }

    @Override
    public String key() {
        return keyPrefix() + keySuffix();
    }

    private String keyPrefix() {
        return depth == 0 ? "" : String.valueOf(depth);
    }

    private String keySuffix() {
        return field.getName();
    }

    @Override
    public Object value(Object object) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new ElementReadException(e);
        }
    }

    @Override
    public int compareTo(ObjectReadableElement o) {
        return key().compareTo(o.key());
    }
}
