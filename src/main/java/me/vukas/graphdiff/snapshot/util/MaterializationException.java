package me.vukas.graphdiff.snapshot.util;

public class MaterializationException extends RuntimeException {
    public MaterializationException(Throwable cause) {
        super("Error during snapshot materialization!", cause);
    }
}
