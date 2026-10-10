package CustomMinHeap;

import java.util.ArrayList;
import java.util.Collections;

public class MinHeap<T> {

    private ArrayList<HeapNode<T>> heapArray = new ArrayList<>();

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
        HeapNode<T> first = heapArray.get(0);
        heapArray.remove(0);
        return first;
    }

    public boolean isEmpty(){
        return heapArray.isEmpty();
    }

    private void swap(int index1, int index2) {
        HeapNode<T> temp = heapArray.get(index1);
        heapArray.set(index1, heapArray.get(index2));
        heapArray.set(index2, heapArray.get(index1));
    }
}
