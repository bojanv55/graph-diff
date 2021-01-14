package me.vukas.old;

public class CyclicObject1 implements EmptyInterface {
    EmptyInterface cycle;

    public CyclicObject1() {
        this.cycle = this;
    }

    public void setCycle(EmptyInterface cycle) {
        this.cycle = cycle;
    }
}
