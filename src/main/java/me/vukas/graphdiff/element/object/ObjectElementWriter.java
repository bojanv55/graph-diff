package me.vukas.graphdiff.element.object;

import me.vukas.graphdiff.element.AbstractElementWriter;
import me.vukas.graphdiff.element.WritableElement;
import me.vukas.graphdiff.snapshot.Schema;
import me.vukas.graphdiff.snapshot.util.MaterializationException;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import java.util.Iterator;

public class ObjectElementWriter extends AbstractElementWriter {
    public ObjectElementWriter() {
        super(null);
    }

    @Override
    public boolean canWriteTo(String type) {
        return true;
    }

    @Override
    public Object newInstance(Schema schema) {
        try {
            Objenesis objenesis = new ObjenesisStd();
            ObjectInstantiator instantiator = objenesis.getInstantiatorOf(Class.forName(schema.getType()));
            return instantiator.newInstance();
        } catch (ClassNotFoundException e) {
            throw new MaterializationException(e);
        }
    }

    @Override
    public Iterable<? extends WritableElement> elementsFor(Iterator<Object> keysIterator) {
        return (Iterable<ObjectWritableElement>) () -> new Iterator<>() {
            @Override
            public boolean hasNext() {
                return keysIterator.hasNext();
            }

            @Override
            public ObjectWritableElement next() {
                return new ObjectWritableElement((String)keysIterator.next());
            }
        };
    }
}
