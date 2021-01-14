package me.vukas.graphdiff.snapshot.util;

import me.vukas.graphdiff.diff.DataDiff;
import me.vukas.graphdiff.diff.SchemaDiff;
import me.vukas.graphdiff.snapshot.Data;
import me.vukas.graphdiff.snapshot.Schema;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static me.vukas.graphdiff.diff.Status.*;
import static me.vukas.graphdiff.snapshot.util.Primitive.isNullOrPrimitive;
import static me.vukas.graphdiff.snapshot.util.StatusMapping.IndexedStatus;
import static me.vukas.graphdiff.snapshot.util.StatusMapping.mapStatus;
import static me.vukas.graphdiff.snapshot.util.Visited.*;

public class Diffing {
    public static Object diff(Object left, Object right, AtomicInteger counter, Map<Integer, DataDiff> diffHistory) {
        if (isNullOrPrimitive(right)) {
            return right;
        }

        if (isNullOrPrimitive(left)) {
            return diffRight(right, counter, diffHistory);
        }

        Data rightData = (Data) right;

        if (isAlreadyVisited(rightData.getId(), diffHistory)) {
            return visitedFromHistory(rightData.getId(), diffHistory);
        }

        Data leftData = (Data) left;
        Schema leftSchema = leftData.getSchema();
        Schema rightSchema = rightData.getSchema();

        String type = diffType(leftSchema, rightSchema);
        SchemaDiff schemaDiff = new SchemaDiff(type);
        DataDiff dataDiff = new DataDiff(rightData.getId(), schemaDiff);

        markAsVisited(rightData.getId(), dataDiff, diffHistory);

        StatusMapping statusMapping = mapStatus(leftData, rightData, diffHistory);

        List<Object> rightKeys = rightSchema.getKeys();
        List<Object> leftKeys = leftSchema.getKeys();
        for(int i=0; i<rightKeys.size(); i++){
            Object rightKey = rightKeys.get(i);
            IndexedStatus status = statusMapping.removeStatusOf(rightKey);

            Object rightValue = rightData.getValueAt(i);

            switch (status.getStatus()){
                case EQUAL -> {
                    schemaDiff.addKey(EQUAL, diff(leftSchema.getKeyAt(status.getIndex()), rightKey, counter, diffHistory));
                }
                case UPDATED -> {
                    Object leftValue = leftData.getValueAt(status.getIndex());
                    schemaDiff.addKey(UPDATED, diff(leftSchema.getKeyAt(status.getIndex()), rightKey, counter, diffHistory));
                    dataDiff.addValue(diff(leftValue, rightValue, counter, diffHistory));
                }
                case CREATED -> {
                    schemaDiff.addKey(CREATED, diffRight(rightKey, counter, diffHistory));
                    dataDiff.addValue(diffRight(rightValue, counter, diffHistory));
                }
            }
        }

        return dataDiff;
    }

    private static String diffType(Schema leftSchema, Schema rightSchema) {
        return Objects.equals(leftSchema.getType(), rightSchema.getType()) ? null : rightSchema.getType();
    }

    private static Object diffRight(Object right, AtomicInteger counter, Map<Integer, DataDiff> diffHistory) {
        if(isNullOrPrimitive(right)){
            return right;
        }

        Data rightData = (Data) right;

        if(isAlreadyVisited(rightData.getId(), diffHistory)){
            return visitedFromHistory(rightData.getId(), diffHistory);
        }

        Schema rightSchema = rightData.getSchema();

        SchemaDiff schemaDiff = new SchemaDiff(rightSchema.getType());
        rightSchema.getKeys()
                .forEach(k -> schemaDiff.addKey(CREATED, diffRight(k, counter, diffHistory)));

        DataDiff dataDiff = new DataDiff(counter.getAndIncrement(), schemaDiff);

        markAsVisited(rightData.getId(), dataDiff, diffHistory);

        rightData.getValues()
                .forEach(v -> dataDiff.addValue(diffRight(v, counter, diffHistory)));

        return dataDiff;
    }
}
