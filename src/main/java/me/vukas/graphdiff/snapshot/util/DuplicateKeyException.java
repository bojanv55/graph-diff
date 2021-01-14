package me.vukas.graphdiff.snapshot.util;

public class DuplicateKeyException extends RuntimeException{
    public DuplicateKeyException(Object key) {
        super(String.format("Duplicate key %s. Only one key with same reference is allowed.", key));
    }
}
