package me.vukas.old;

public class CyclicObject2 implements EmptyInterface {
    private EmptyInterface cycle;

    public CyclicObject2(EmptyInterface cycle) {
        this.cycle = cycle;
    }
}
