package me.vukas.graphdiff.element.object;

import me.vukas.graphdiff.element.WritableElement;
import me.vukas.graphdiff.snapshot.util.MaterializationException;

import java.lang.reflect.Field;

public class ObjectWritableElement implements WritableElement {
    private final String key;

    public ObjectWritableElement(String key){
        this.key = key;
    }

    @Override
    public void writeTo(Object instance, Object value) {
        Field field = getField(instance);
        field.setAccessible(true);
        try {
            if(field.getType().isPrimitive()){
                switch (field.getType().getName()){
                    case "byte" -> field.setByte(instance, (byte) value);
                    case "short" -> field.setShort(instance, (short) value);
                    case "int" -> field.setInt(instance, (int) value);
                    case "long" -> field.setLong(instance, (long) value);
                    case "float" -> field.setFloat(instance, (float) value);
                    case "double" -> field.setDouble(instance, (double) value);
                    case "boolean" -> field.setBoolean(instance, (boolean) value);
                    case "char" -> field.setChar(instance, (char) value);
                }
            }
            else{
                field.set(instance, value);
            }
        }
        catch (Exception e){
            throw new MaterializationException(e);
        }
    }

    private int getDepth(){
        String prefix = key.replaceAll("(\\d+).+", "$1");
        return prefix.equals(key) ? 0 : Integer.parseInt(prefix);
    }

    private String getFieldName(){
        String prefix = key.replaceAll("(\\d+).+", "$1");
        return prefix.equals(key) ? key : key.substring(prefix.length());
    }

    private Field getField(Object instance) {
        int depth = getDepth();
        String fieldName = getFieldName();
        Class<?> type = instance.getClass();
        for (int i=0; i<depth; i++){
            type = type.getSuperclass();
        }
        try {
            return type.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new MaterializationException(e);
        }
    }
}
