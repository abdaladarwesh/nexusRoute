package com.abdullah.nexus_route.model.Graph;

import java.util.Objects;

public class Node<T> {
    public T data;

    public Node(T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return Objects.equals(data, node.data);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(data);
    }
}

