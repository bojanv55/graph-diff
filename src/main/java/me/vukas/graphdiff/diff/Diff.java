package me.vukas.graphdiff.diff;

public class Diff {
    private final Object data;

    public Diff(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
