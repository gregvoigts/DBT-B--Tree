package de.tuberlin.dima.dbt.exercises.bplustree;

import java.util.Arrays;

public class LeafNode extends Node {

    private String[] values;

    public LeafNode(int capacity) {
        this(new Integer[] {}, new String[] {}, capacity);
    }

    public LeafNode(Integer[] keys, String[] values, int capacity) {
        super(keys, capacity);
        assert keys.length == values.length;
        this.values = Arrays.copyOf(values, values.length);
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        assert this.keys.length == values.length;
        this.values = Arrays.copyOf(values, values.length);
    }

    @Override
    public Object[] getPayload() {
        return getValues();
    }

    @Override
    public void setPayload(Object[] payload) {
        setValues((String[]) payload);
    }

    public String toString() {
        return new BPlusTreePrinter(this).toString();
    }

}
