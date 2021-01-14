package me.vukas.graphdiff.element.object;

import me.vukas.graphdiff.element.AbstractElementReader;
import me.vukas.graphdiff.element.ReadableElement;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Stream;

public class ObjectElementReader extends AbstractElementReader {
    public ObjectElementReader() {
        super(null);
    }

    @Override
    public boolean canReadFrom(String type) {
        return true;
    }

    @Override
    public Iterable<? extends ReadableElement> elementsFor(Object object) {
        return (Iterable<ObjectReadableElement>) () ->
                objectElements(object.getClass(), 0)
                        .sorted()
                        .iterator();
    }

    private Stream<ObjectReadableElement> objectElements(Class<?> type, int depth) {
        if (type == Object.class) {
            return Stream.empty();
        }
        return Stream.concat(
                Arrays.stream(type.getDeclaredFields())
                        .filter(f -> {
                            int m = f.getModifiers();
                            return !(Modifier.isStatic(m) && Modifier.isFinal(m));
                        })
                        .map(f -> new ObjectReadableElement(f, depth)),
                objectElements(type.getSuperclass(), depth + 1)
        );
    }
}
