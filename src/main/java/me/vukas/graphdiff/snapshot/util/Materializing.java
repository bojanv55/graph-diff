package me.vukas.graphdiff.snapshot.util;

import me.vukas.graphdiff.element.ElementWriter;
import me.vukas.graphdiff.element.WritableElement;
import me.vukas.graphdiff.element.array.ArrayElementWriter;
import me.vukas.graphdiff.element.object.ObjectElementWriter;
import me.vukas.graphdiff.snapshot.Data;
import me.vukas.graphdiff.snapshot.Schema;

import java.util.Iterator;
import java.util.Map;

import static me.vukas.graphdiff.snapshot.util.Primitive.isNullOrPrimitive;
import static me.vukas.graphdiff.snapshot.util.Visited.*;

public class Materializing {
    private static final ElementWriter WRITER_CHAIN = new ArrayElementWriter(new ObjectElementWriter());

    public static Object materialize(Object object, Map<Data, Object> history){
        if(isNullOrPrimitive(object)){
            return object;
        }

        Data data = (Data) object;

        if(isAlreadyVisited(data, history)){
            return visitedFromHistory(data, history);
        }

        Schema schema = data.getSchema();
        String type = schema.getType();
        ElementWriter writer = WRITER_CHAIN.writerFor(type);

        Object instance = writer.newInstance(schema);

        markAsVisited(data, instance, history);

        Iterator<Object> keysIterator = schema.getKeys().stream()
                .map(k -> Materializing.materialize(k, history)).iterator();

        int index = 0;
        for(WritableElement element : writer.elementsFor(keysIterator)){
            element.writeTo(instance, materialize(data.getValueAt(index++), history));
        }

        return instance;
    }
}
