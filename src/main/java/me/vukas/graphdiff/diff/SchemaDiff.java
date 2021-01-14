package me.vukas.graphdiff.diff;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class SchemaDiff {
    private final String type;
    private final Map<Object, Status> keyToStatus = new LinkedHashMap<>();

    public SchemaDiff(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Map<Object, Status> getKeyToStatus() {
        return Collections.unmodifiableMap(keyToStatus);
    }

    public void addKey(Status status, Object key) {
        this.keyToStatus.put(key, status);
    }
}
