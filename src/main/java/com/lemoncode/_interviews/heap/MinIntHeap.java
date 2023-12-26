package com.lemoncode._interviews.heap;

import java.util.Arrays;

public class MinIntHeap {
    private int capacity = 10;
    private int size = 0;

    int[] items = new int[capacity];

    private int getLeftChildIndex(int parentIndex) {
        return 2 * parentIndex + 1;
    }

    private int getRightChildIndex(int parentIndex) {
        return 2 * parentIndex + 2;
    }

    private int getParentIndex(int childIndex) {
        return (childIndex - 1) / 2;
    }

    private boolean hasLeftChild(int index) {
        return getLeftChildIndex(index) < size;
    }

    private boolean hasRightChild(int index) {
        return getRightChildIndex(index) < size;
    }

    private boolean hasParent(int index) {
        return getParentIndex(index) > 0;
    }

    private int leftChild(int index) {
        return items[getLeftChildIndex(index)];
    }

    private int rightChild(int index) {
        return items[getRightChildIndex(index)];
    }

    private int parent(int index) {
        return items[getParentIndex(index)];
    }

    private void swap(int indexOne, int indexTwo) {
        int temp = items[indexOne];
        items[indexOne] = items[indexTwo];
        items[indexTwo] = temp;
    }

    private void ensureExtraCapacity() {
        if (size == capacity) {
            items = Arrays.copyOf(items, capacity * 2);
            capacity *= 2;
        }
    }

    public int peek() { //return first element
        if (size == 0) throw new IllegalStateException();
        return items[0];
    }

    public int poll() { //extract minimum element
        if (size == 0) throw new IllegalStateException();
        //get and remove first item and move last item to first index
        int item = items[0];
        items[0] = items[size - 1];
        size--; //shirnk array because we took out one
        heapifyDown();  // move top element down if needed
        return item;
    }

    public void add(int item) {
        ensureExtraCapacity();
        items[size] = item; //add item to last item
        size++;
        heapifyUp();
    }

    public void heapifyUp() {
        int index = size - 1; //start with last index
        while (hasParent(index) && parent(index) > items[index]) {
            //keep going up while there is a parent bigger than me
            swap(getParentIndex(index), index); //swap values with parent
            index = getParentIndex(index);
        }
    }

    public void heapifyDown() {
        int index = 0; //start with first
        while (hasLeftChild(index)) {
            int smallerChildIndex = getLeftChildIndex(index);
            if (hasRightChild(index) && rightChild(index) < leftChild(index)) {
                smallerChildIndex = getRightChildIndex(index);
            }

            if (items[index] < items[smallerChildIndex]) {
                break;// it is already in the right place
            } else {
                swap(index, smallerChildIndex);
                index = smallerChildIndex;
            }
        }
    }


}
