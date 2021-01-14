package me.vukas.graphdiff.element;

public abstract class AbstractElementReader implements ElementReader {
    private final AbstractElementReader next;

    public AbstractElementReader(AbstractElementReader next) {
        this.next = next;
    }

    @Override
    public ElementReader readerFor(String type) {
        if(canReadFrom(type)){
            return this;
        }
        return next.readerFor(type);
    }
}
