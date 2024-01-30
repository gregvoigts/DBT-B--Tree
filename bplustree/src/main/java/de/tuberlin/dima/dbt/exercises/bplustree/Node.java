package de.tuberlin.dima.dbt.exercises.bplustree;

import java.util.Arrays;

public abstract class Node implements Comparable<Node> {

    protected Integer[] keys;

    protected int capacity;

    public Node(Integer[] keys, int capacity) {
        this.capacity = capacity;
        assert keys.length <= capacity;
        this.keys = Arrays.copyOf(keys, keys.length);
    }

    public Integer[] getKeys() {
        return keys;
    }

    public void setKeys(Integer[] keys) {
        assert keys.length <= this.capacity;
        this.keys = Arrays.copyOf(keys, keys.length);
        ;
    }

    public abstract Object[] getPayload();

    public abstract void setPayload(Object[] payload);

    @Override
    public int compareTo(Node o) {
        return this.keys[0].compareTo(o.keys[0]);
    }

}
