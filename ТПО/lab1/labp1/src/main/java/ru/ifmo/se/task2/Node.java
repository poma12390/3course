package ru.ifmo.se.task2;

import lombok.Getter;

@Getter
public class Node {
    int n;
    int[] keys = new int[7];
    Node[] children = new Node[8];
    boolean leaf = true;

    int search(int k) {
        for (int i = 0; i < this.n; i++) {
            if (this.keys[i] == k) {
                return i;
            }
        }
        return -1;
    }

    void insertNonFull(int k) {
        int i = n;
        if (leaf) {
            while (i >= 1 && keys[i - 1] > k) {
                keys[i] = keys[i - 1];
                i--;
            }
            keys[i] = k;
            n++;
        } else {
            while (i >= 1 && keys[i - 1] > k) {
                i--;
            }
            if (children[i].n == 7) {
                splitChild(i);
                if (k > keys[i]) {
                    i++;
                }
            }
            children[i].insertNonFull(k);
        }
    }

    void splitChild(int i) {
        Node t = children[i];
        Node z = new Node();
        z.leaf = t.leaf;
        t.n = 4;
        z.n = 3;
        for (int j = 0; j < 3; j++) {
            z.keys[j] = t.keys[j + 4];
            t.keys[j + 4] = 0;
        }
        if (!t.leaf) {
            System.arraycopy(t.children, 4, z.children, 0, 4);
        }
        for (int j = n; j >= i + 1; j--) {
            children[j + 1] = children[j];
        }
        children[i + 1] = z;
        for (int j = n - 1; j >= i; j--) {
            keys[j + 1] = keys[j];
        }
        keys[i] = t.keys[3];
        n = n + 1;
        // The following line is for clearing the split key from t after assigning it to keys[i];
        t.keys[3] = 0;
        t.n = 3;
    }

}
