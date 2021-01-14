package me.vukas.graphdiff.snapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Data {
    private final int id;
    private final Schema schema;
    private final List<Object> values = new ArrayList<>();

    public Data(int id, Schema schema) {
        this.id = id;
        this.schema = schema;
    }

    public int getId() {
        return id;
    }

    public Schema getSchema() {
        return schema;
    }

    public List<Object> getValues() {
        return Collections.unmodifiableList(values);
    }

    public Object getValueAt(int index){
        return values.get(index);
    }

    public void addValue(Object value){
        this.values.add(value);
    }
}
