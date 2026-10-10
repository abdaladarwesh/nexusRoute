package com.abdullah.nexus_route.model.CustomMinHeap;

import java.util.Objects;

public class HeapNode<T> {
    private T node;
    private Double value;

    public T getNode() {
        return node;
    }

    public void setNode(T node) {
        this.node = node;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public HeapNode(T node, Double value) {
        this.node = node;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HeapNode<?> heapNode = (HeapNode<?>) o;
        return value == heapNode.value && Objects.equals(node, heapNode.node);
    }

    @Override
    public int hashCode() {
        return Objects.hash(node, value);
    }
}
