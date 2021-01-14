package me.vukas.graphdiff.snapshot.util;

import me.vukas.graphdiff.diff.DataDiff;
import me.vukas.graphdiff.diff.SchemaDiff;
import me.vukas.graphdiff.diff.Status;
import me.vukas.graphdiff.snapshot.Data;
import me.vukas.graphdiff.snapshot.Schema;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static me.vukas.graphdiff.snapshot.util.Primitive.isNullOrPrimitive;
import static me.vukas.graphdiff.snapshot.util.Visited.*;

public class Patching {
    public static Object patch(Object snapshot, Object diff, AtomicInteger counter, Map<DataDiff, Data> diffDataHistory, Map<Integer, Data> snapshotDataHistory) {
        if(isNullOrPrimitive(diff)){
            return diff;
        }

        DataDiff dataDiff = (DataDiff) diff;

        if(isAlreadyVisited(dataDiff, diffDataHistory)){
            return visitedFromHistory(dataDiff, diffDataHistory);
        }

        SchemaDiff schemaDiff = dataDiff.getSchemaDiff();

        //TODO: mozda malo bolje da handleuje kada je lijeva strana primitive element?
        Data data = isNullOrPrimitive(snapshot) ? new Data(0, null) : (Data) snapshot;
        Schema schema = data.getSchema();

        String type = schemaDiff.getType() == null ? schema.getType() : schemaDiff.getType();
        Schema resultSchema = new Schema(type);
        Data resultData = new Data(counter.getAndIncrement(), resultSchema);

        markAsVisited(dataDiff, resultData, diffDataHistory);

        int equalsCount = 0;
        int index = 0;
        for(Map.Entry<Object, Status> keyToStatus : schemaDiff.getKeyToStatus().entrySet()){
            Object key = keyToStatus.getKey();
            Status status = keyToStatus.getValue();

            switch (status){
                case EQUAL -> {
                    resultData.addValue(patchLeft(data.getValueAt(index), counter, snapshotDataHistory));
                    resultSchema.addKey(patchLeft(key, counter, snapshotDataHistory));
                    equalsCount++;
                }
                case CREATED -> {
                    resultData.addValue(patch(null, dataDiff.getValueAt(index - equalsCount), counter, diffDataHistory, snapshotDataHistory));
                    resultSchema.addKey(patch(null, key, counter, diffDataHistory, snapshotDataHistory));
                }
                case UPDATED -> {
                    resultData.addValue(patch(data.getValueAt(index), dataDiff.getValueAt(index - equalsCount), counter, diffDataHistory, snapshotDataHistory));
                    resultSchema.addKey(patch(schema.getKeyAt(index), key, counter, diffDataHistory, snapshotDataHistory));
                }
            }
            index++;
        }

        return resultData;
    }

    private static Object patchLeft(Object left, AtomicInteger counter, Map<Integer, Data> snapshotDataHistory) {
        if(isNullOrPrimitive(left)){
            return left;
        }

        Data leftData = (Data) left;
        Schema leftSchema = leftData.getSchema();

        if(isAlreadyVisited(leftData.getId(), snapshotDataHistory)){
            return visitedFromHistory(leftData.getId(), snapshotDataHistory);
        }

        Schema resultSchema = new Schema(leftSchema.getType());
        leftSchema.getKeys()
                .forEach(k -> resultSchema.addKey(patchLeft(k, counter, snapshotDataHistory)));

        Data resultData = new Data(counter.getAndIncrement(), resultSchema);

        markAsVisited(leftData.getId(), resultData, snapshotDataHistory);

        leftData.getValues()
                .forEach(v -> resultData.addValue(patchLeft(v, counter, snapshotDataHistory)));

        return resultData;
    }
}
