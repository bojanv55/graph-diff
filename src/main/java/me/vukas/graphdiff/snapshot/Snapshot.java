package me.vukas.graphdiff.snapshot;

import me.vukas.graphdiff.diff.Diff;
import me.vukas.graphdiff.snapshot.util.Materializing;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static me.vukas.graphdiff.snapshot.util.Diffing.diff;
import static me.vukas.graphdiff.snapshot.util.Patching.patch;
import static me.vukas.graphdiff.snapshot.util.Snapshotting.snapshot;

public class Snapshot {
    private final Object data;
    private final int count;

    private Snapshot(Object data, int count) {
        this.data = data;
        this.count = count;
    }

    public static Snapshot of(Object graph) {
        AtomicInteger counter = new AtomicInteger();
        return new Snapshot(snapshot(graph, counter, new IdentityHashMap<>()), counter.get());
    }

    public Diff compareTo(Snapshot other) {
        AtomicInteger counter = new AtomicInteger(Math.max(count, other.count));
        return new Diff(diff(data, other.data, counter, new HashMap<>()));
    }

    public Snapshot patchWith(Diff diff) {
        AtomicInteger counter = new AtomicInteger();
        return new Snapshot(patch(data, diff.getData(), counter, new IdentityHashMap<>(), new IdentityHashMap<>()) , counter.get());
    }

    public Object materialize() {
        return Materializing.materialize(data, new IdentityHashMap<>());
    }
}
