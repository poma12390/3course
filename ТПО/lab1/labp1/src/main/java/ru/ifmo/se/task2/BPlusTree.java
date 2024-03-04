package ru.ifmo.se.task2;

import lombok.Getter;

@Getter
public class BPlusTree {
    private static final int MAX_DEGREE = 7;
    private Node root;

    public BPlusTree() {
        Node x = new Node();
        x.n = 0;
        x.leaf = true;
        root = x;
    }

    public void insert(int k) {
        Node r = root;
        if (r.n == MAX_DEGREE) {
            Node s = new Node();
            root = s;
            s.leaf = false;
            s.children[0] = r;
            s.splitChild(0);
            s.insertNonFull(k);
        } else {
            r.insertNonFull(k);
        }
    }
}
