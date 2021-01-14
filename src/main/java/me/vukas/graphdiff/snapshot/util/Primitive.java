package me.vukas.graphdiff.snapshot.util;

import java.util.Set;

public class Primitive {
    private static final Set<Class<?>> PRIMITIVES = Set.of(
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            Boolean.class,
            Character.class,
            String.class
    );

    public static boolean isNullOrPrimitive(Object object) {
        return object == null || PRIMITIVES.contains(object.getClass());
    }
}
