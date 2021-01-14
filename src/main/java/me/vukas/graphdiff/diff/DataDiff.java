package me.vukas.graphdiff.diff;

import java.util.ArrayList;
import java.util.List;

public class DataDiff {
    private final int id;
    private final SchemaDiff schemaDiff;
    private final List<Object> values = new ArrayList<>();

    public DataDiff(int id, SchemaDiff schemaDiff) {
        this.id = id;
        this.schemaDiff = schemaDiff;
    }

    public int getId() {
        return id;
    }

    public SchemaDiff getSchemaDiff() {
        return schemaDiff;
    }

    public Object getValueAt(int index) {
        return this.values.get(index);
    }

    public void addValue(Object value) {
        this.values.add(value);
    }
}
