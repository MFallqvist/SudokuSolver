package classes;

import java.util.List;

// Class for Doubly Linked List
public class DLL {
    Node root; // root of list

    /* Doubly Linked list Node*/
    class Node {
        int data, row;
        int col_size = 0;
        String name = " ";
        Node up, down, left, right, center;


        // Constructor to create a new node
        // next and prev is by default initialized as null
        Node(int d, int r) {
            data = d;
            row = r;
        }
    }

    void search(Node h, int k, List<Node> s){
        // h = root column object, k = current search depth, s = solution
        Node c, r, j;
        if(root.right.equals(root)) {
            print_solution(s);
            return;
        }else{
            c = choose_column_object(h);
            r = c.down;
            while (!r.equals(c)){
                s.add(r);
                j = r.right;
                while (!j.equals(r)){
                    cover(j.center);
                    j = j.right;
                }
                search(h,k+1, s);
            }
        }
    }
    public void cover(Node c){
        // To be implemented, change pointers etc
        Node i, j;
        c.right.left = c.left;
        c.left.right = c.right;
        i = c.down;
        while (!i.equals(c)){
            j = i.right;
            while (!j.equals(i)){
                j.down.up = j.up;
                j.up.down = j.down;
                j.col_size -= 1; // S[C[j]] ← S[C[j]] − 1
                j = j.right;
            }
            i = i.down;
        }
    }

    // Push a node to front
    public void push(int data, int row)
    {
        /* 1. allocate node
         * 2. put in the data */
        Node new_Node = new Node(data, row);

        /* 3 Make right of new node as root and previous as NULL */
        new_Node.right = root;
        new_Node.left = null;
        /* 4 Link up and down of new node */
        new_Node.down=root.down;
        new_Node.up=root.up;


        /* 5. change prev of head node to new node */
        if (root != null)
            root.left = new_Node;

        /* 6. move the head to point to the new node */
        root = new_Node;
    }

    Node choose_column_object(Node obj){
        Node chosen = obj;
        // To be implemented, use column with fewest data points
        return chosen;
    }
    void print_solution(List<Node> obj){
        // To be implemented
    }
}
