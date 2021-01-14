package me.vukas.graphdiff.element;

public abstract class AbstractElementWriter implements ElementWriter {
    private final AbstractElementWriter next;

    public AbstractElementWriter(AbstractElementWriter next) {
        this.next = next;
    }

    @Override
    public ElementWriter writerFor(String type) {
        if(canWriteTo(type)){
            return this;
        }
        return next.writerFor(type);
    }
}
