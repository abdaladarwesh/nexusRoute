package com.abdullah.nexus_route.model.CustomMinHeap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MinHeap<T> {

    private List<HeapNode<T>> heapArray = Collections.synchronizedList(new ArrayList<HeapNode<T>>());

    public void add(HeapNode<T> node) {
        if (node == null) {
            throw new NullPointerException("Node is null");
        }

        heapArray.add(node);

        int index = heapArray.size() - 1;

        while (index > 0 && heapArray.get(index).getValue() < heapArray.get(Math.floorDiv(index - 1, 2)).getValue()) {
            swap(index, Math.floorDiv(index - 1, 2));
            index = Math.floorDiv(index - 1, 2);
        }
    }

    public HeapNode<T> poll() {
        if (heapArray.isEmpty()) {
            return null;
        }

        HeapNode<T> root = heapArray.get(0);

        HeapNode<T> lastElement = heapArray.remove(heapArray.size() - 1);

        if (!heapArray.isEmpty()) {
            heapArray.set(0, lastElement);
            heapifyDown(0);
        }

        return root;
    }

    private void heapifyDown(int index) {
        int smallest = index;
        int leftChild = 2 * index + 1;
        int rightChild = 2 * index + 2;

        if (leftChild < heapArray.size() && heapArray.get(leftChild).getValue() < heapArray.get(smallest).getValue()) {
            smallest = leftChild;
        }

        if (rightChild < heapArray.size() && heapArray.get(rightChild).getValue() < heapArray.get(smallest).getValue()) {
            smallest = rightChild;
        }

        if (smallest != index) {
            HeapNode<T> temp = heapArray.get(index);
            heapArray.set(index, heapArray.get(smallest));
            heapArray.set(smallest, temp);

            heapifyDown(smallest);
        }
    }
    public boolean isEmpty(){
        return heapArray.isEmpty();
    }

    private void swap(int index1, int index2) {
        HeapNode<T> temp = heapArray.get(index1);
        heapArray.set(index1, heapArray.get(index2));
        heapArray.set(index2, temp);
    }
}
