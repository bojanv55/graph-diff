package me.vukas.old.model;

public class EntityNoDefConstructor {
    private int id;
    private GrandChildEntity child;

    public EntityNoDefConstructor(int id, GrandChildEntity child){
        this.id = id;
        this.child = child;
    }
}
