package com.lemoncode._interviews.algorithms;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;

// Recursive Java program to reverse
// a linked list
class LinkedListReverse {
    static Node HEAD_NODE; // head of list

    @Getter
    @Setter
    private static class Node {
        private final int data;
        private Node next;

        Node(int d) {
            data = d;
            next = null;
        }
    }

    static Node reverse(Node current) {
        if (current == null || current.next == null)   //return the last item
            return current;

        Node rest = reverse(current.next);  //always returns the last item from original list
        current.getNext().setNext(current);
        current.setNext(null);

        return rest;
    }


    static void push(int data) {
        Node newNode = new Node(data);
        newNode.next = HEAD_NODE;   //the newNode will become the head whose next is the previous HEAD node
        HEAD_NODE = newNode;
    }


    /* Driver program to test above function*/
    public static void main(String args[]) {


        //create linked nodes always replacing head with last pushed item
        push(4);
        push(3);
        push(2);
        push(1);

        Assertions.assertEquals("4321", stringify(reverse(HEAD_NODE)));
        System.out.println("END");
    }

    private static String stringify(Node reverse) {
        StringBuilder sb = new StringBuilder();
        sb.append(reverse.getData());
        while (reverse.getNext() != null){
            sb.append(reverse.getNext().getData());
            reverse = reverse.getNext();
        }

        return sb.toString();
    }
}
