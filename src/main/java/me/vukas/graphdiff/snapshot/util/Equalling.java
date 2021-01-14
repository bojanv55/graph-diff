package me.vukas.graphdiff.snapshot.util;

import me.vukas.graphdiff.diff.DataDiff;
import me.vukas.graphdiff.snapshot.Data;
import me.vukas.graphdiff.snapshot.Schema;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static me.vukas.graphdiff.snapshot.util.Primitive.isNullOrPrimitive;
import static me.vukas.graphdiff.snapshot.util.Visited.*;

public class Equalling {
    public static boolean isEqual(Object left, Object right, Map<Integer, DataDiff> diffHistory, Map<Integer, Integer> leftToRightHistory) {
        if(isNullOrPrimitive(left) && isNullOrPrimitive(right)){
            return Objects.equals(left, right);
        }

        if(isNullOrPrimitive(left) || isNullOrPrimitive(right)){
            return false;
        }

        Data leftData = (Data) left;
        Data rightData = (Data) right;

        if(isAlreadyVisited(rightData.getId(), diffHistory)){
            return false;
        }

        if(isAlreadyVisited(leftData.getId(), leftToRightHistory)){
            return visitedFromHistory(leftData.getId(), leftToRightHistory).equals(rightData.getId());
        }

        markAsVisited(leftData.getId(), rightData.getId(), leftToRightHistory);

        Schema leftSchema = leftData.getSchema();
        Schema rightSchema = rightData.getSchema();

        if(!Objects.equals(leftSchema.getType(), rightSchema.getType())){
            return false;
        }

        List<Object> leftValues = leftData.getValues();
        List<Object> rightValues = rightData.getValues();
        List<Object> leftKeys = leftSchema.getKeys();
        List<Object> rightKeys = rightSchema.getKeys();

        if(leftValues.size() != rightValues.size()
                || leftKeys.size() != rightKeys.size()){
            return false;
        }

        return allEqual(leftKeys, rightKeys, diffHistory, leftToRightHistory)
                && allEqual(leftValues, rightValues, diffHistory, leftToRightHistory);
    }

    private static boolean allEqual(List<Object> leftValues, List<Object> rightValues, Map<Integer, DataDiff> diffHistory, Map<Integer, Integer> leftToRightHistory) {
        Iterator<Object> lvi = leftValues.iterator();
        Iterator<Object> rvi = rightValues.iterator();
        while (lvi.hasNext() && rvi.hasNext()) {
            if (!isEqual(lvi.next(), rvi.next(), diffHistory, leftToRightHistory)) {
                return false;
            }
        }
        return true;
    }
}
