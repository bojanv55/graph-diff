package me.vukas.graphdiff.snapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Schema {
    private final String type;
    private final List<Object> keys = new ArrayList<>();

    public Schema(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public List<Object> getKeys() {
        return Collections.unmodifiableList(keys);
    }

    public Object getKeyAt(int index){
        return keys.get(index);
    }

    public void addKey(Object key){
        this.keys.add(key);
    }

    public int keyCount(){
        return this.keys.size();
    }
}
