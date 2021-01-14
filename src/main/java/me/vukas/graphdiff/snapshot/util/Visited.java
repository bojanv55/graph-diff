package me.vukas.graphdiff.snapshot.util;

import java.util.Map;

public class Visited {
    public static <K, V> boolean isAlreadyVisited(K key, Map<K, V> history) {
        return history.containsKey(key);
    }

    public static <K, V> V visitedFromHistory(K key, Map<K, V> history) {
        return history.get(key);
    }

    public static <K, V> void markAsVisited(K key, V value, Map<K, V> history) {
        history.put(key, value);
    }
}
