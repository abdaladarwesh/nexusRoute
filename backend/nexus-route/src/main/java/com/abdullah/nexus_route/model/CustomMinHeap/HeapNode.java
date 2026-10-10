package CustomMinHeap;

import java.util.Objects;

public class HeapNode<T> {
    private T node;
    private int value;

    public T getNode() {
        return node;
    }

    public void setNode(T node) {
        this.node = node;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public HeapNode(T node, int value) {
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
