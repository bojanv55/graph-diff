package me.vukas.graphdiff.snapshot.util;

import me.vukas.graphdiff.diff.DataDiff;
import me.vukas.graphdiff.diff.Status;
import me.vukas.graphdiff.snapshot.Data;
import me.vukas.graphdiff.snapshot.Schema;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static me.vukas.graphdiff.diff.Status.*;
import static me.vukas.graphdiff.snapshot.util.Equalling.isEqual;

public class StatusMapping {
    private final Map<Object, IndexedStatus> deletedKeyToIndexes;
    private final Map<Object, IndexedStatus> createdKeyToIndexes;
    private final Map<Object, IndexedStatus> equalKeyToIndexes;

    public StatusMapping(Map<Object, IndexedStatus> deletedKeyToIndexes,
                         Map<Object, IndexedStatus> createdKeyToIndexes,
                         Map<Object, IndexedStatus> equalKeyToIndexes) {
        this.deletedKeyToIndexes = deletedKeyToIndexes;
        this.createdKeyToIndexes = createdKeyToIndexes;
        this.equalKeyToIndexes = equalKeyToIndexes;
    }

    public static StatusMapping mapStatus(Data leftData, Data rightData, Map<Integer, DataDiff> diffHistory) {
        Schema leftSchema = leftData.getSchema();
        Schema rightSchema = rightData.getSchema();
        List<Object> leftSchemaKeys = leftSchema.getKeys();
        List<Object> rightSchemaKeys = rightSchema.getKeys();

        Map<Object, IndexedStatus> deletedKeyToIndexes = new IdentityHashMap<>();
        Map<Object, IndexedStatus> createdKeyToIndexes = IntStream.range(0, rightSchemaKeys.size())
                .boxed()
                .collect(Collectors.toMap(rightSchemaKeys::get, i -> new IndexedStatus(i, CREATED), (k, v)->{
                    throw new DuplicateKeyException(k);
                }, IdentityHashMap::new));
        Map<Object, IndexedStatus> equalKeyToIndexes = new IdentityHashMap<>();

        leftLoop:
        for(int i=0; i<leftSchemaKeys.size(); i++){
            Object leftKey = leftSchemaKeys.get(i);
            for(int j=0; j<rightSchemaKeys.size(); j++){
                Object rightKey = rightSchemaKeys.get(j);
                if (isEqual(leftKey, rightKey, diffHistory, new HashMap<>())) {
                    equalKeyToIndexes.put(rightKey, isEqual(leftData.getValueAt(i), rightData.getValueAt(j), diffHistory, new HashMap<>())
                            ? new IndexedStatus(i, EQUAL) : new IndexedStatus(i, UPDATED));
                    createdKeyToIndexes.remove(rightKey);
                    continue leftLoop;
                }
            }
            deletedKeyToIndexes.put(leftKey, new IndexedStatus(i, DELETED));
        }

        return new StatusMapping(deletedKeyToIndexes, createdKeyToIndexes, equalKeyToIndexes);
    }

    public IndexedStatus removeStatusOf(Object key) {
        if(equalKeyToIndexes.containsKey(key)){
            return equalKeyToIndexes.get(key);
        }
        else if(createdKeyToIndexes.containsKey(key)){
            return createdKeyToIndexes.get(key);
        }
        else {
            return deletedKeyToIndexes.get(key);
        }
    }

    public static class IndexedStatus{
        private final int index;
        private final Status status;

        public IndexedStatus(int index, Status status) {
            this.index = index;
            this.status = status;
        }

        public int getIndex() {
            return index;
        }

        public Status getStatus() {
            return status;
        }
    }
}
