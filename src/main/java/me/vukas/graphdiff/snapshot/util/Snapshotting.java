package me.vukas.graphdiff.snapshot.util;

import me.vukas.graphdiff.element.ElementReader;
import me.vukas.graphdiff.element.ReadableElement;
import me.vukas.graphdiff.element.array.ArrayElementReader;
import me.vukas.graphdiff.element.object.ObjectElementReader;
import me.vukas.graphdiff.snapshot.Data;
import me.vukas.graphdiff.snapshot.Schema;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static me.vukas.graphdiff.snapshot.util.Visited.*;
import static me.vukas.graphdiff.snapshot.util.Primitive.isNullOrPrimitive;

public class Snapshotting {
    private static final ElementReader READER_CHAIN = new ArrayElementReader(new ObjectElementReader());

    public static Object snapshot(Object object, AtomicInteger counter, Map<Object, Data> history) {
        if (isNullOrPrimitive(object)) {
            return object;
        }

        if (isAlreadyVisited(object, history)) {
            return visitedFromHistory(object, history);
        }

        String type = object.getClass().getName();
        ElementReader reader = READER_CHAIN.readerFor(type);

        Schema schema = new Schema(type);
        Data data = new Data(counter.getAndIncrement(), schema);

        markAsVisited(object, data, history);

        for (ReadableElement element : reader.elementsFor(object)) {
            schema.addKey(snapshot(element.key(), counter, history));
            data.addValue(snapshot(element.value(object), counter, history));
        }

        return data;
    }
}
