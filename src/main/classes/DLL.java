package main.classes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;
import main.helper.*;

// Class for Doubly Linked List
public class DLL {
    ColumnNode root; // root of list
    LinkedList<Node> solution;

    DLL(int[][] sudoku){
        root = DLL_grid(sudoku);
        runSolver();
    }
    /* Doubly Linked list Node*/
    public class Node {
        int data, row;
        Node up, down, left, right, center;
        ColumnNode col;

        // Constructor to create a new node
        // next and prev is by default initialized as null
        Node(int d, int r) {
            data = d;
            row = r;
        }
        Node() {
            data = 0;
            row = 0;
            up = down = left = right = this;
        }
        Node(ColumnNode c){
            this();
            col = c;
        }
        // hooke a node n1 to the right of `this` node
        Node hookRight(Node n1){
            n1.right = this.right;
            n1.right.left = n1;
            n1.left = this;
            this.right = n1;
            return n1;
        }
        // hooks node n1 `below` current node
        Node hookDown(Node n1){
            assert (this.col == n1.col);
            n1.down = this.down;
            n1.down.up = n1;
            n1.up = this;
            this.down = n1;
            return n1;
        }

    }
    public class ColumnNode extends Node{
        int size = 0;
        String name;
        public ColumnNode(String n){
            super();
            name = n;
            col = this;
            //size = 0;
        }
    }

    void search(Node h, int k, LinkedList<Node> s){
        // h = root column object, k = current search depth, s = solution
        Node c, r, j;
        if(root.right.equals(root)) {
            print_solution(s);
            return;
        }else{
            c = choose_column_object(h);
            r = c.down;
            while (!r.equals(c)){
                s.push(r);
                j = r.right;
                while (!j.equals(r)){
                    cover(j.center);
                    j = j.right;
                }
                search(h,k+1, s);
                // Pop data object from s
                r = s.pop();
                c = r.center;
                j = r.left;
                while (!j.equals(r)){
                    uncover(j.center);
                    j = j.left;
                }
                r = r.down;
            }
            uncover(c);
            return;
        }
    }
    // Used to backtrack to covered Node
    public void uncover(Node c){
        Node i, j;
        i = c.up;
        while(!i.equals(c)){
            j = i.left;
            while (!j.equals(i)){
                j.col.size -= 1; //S[C[j]] ← S[C[j]] − 1
                j.down.up = j;
                j.up.down = j;
                j = j.left;
            }
            i = i.up;
        }
        c.right.left = c;
        c.left.right = c;
    }
    // Used to cover Node and all connected data points in that same column
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
                j.col.size -= 1; // S[C[j]] ← S[C[j]] − 1
                j = j.right;
            }
            i = i.down;
        }
    }

    // Push a node to front
    public void push(int data, int row)
    {
        /* 1. allocate node
           2. put in the data */
        ColumnNode new_Node = new ColumnNode("root"); //Node(data, row);

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
        Node chosen, candidate;
        int min;
        candidate = obj.right;
        min = candidate.col.size;
        chosen = candidate;
        while (!candidate.equals(null)){
            if (candidate.col.size < min){
                chosen = candidate;
                min = chosen.col.size;
            }
            else{
                continue;
            }
        }
        return chosen;
    }
    void print_solution(LinkedList<Node> s){
        Node i;
        i = s.pop().right;
        while(!i.equals(s)){ // equal must be null?
            System.out.print(i.center.col.name); // print (N[C[i]])
            i = i.right;
        }
    }
    // Returns the root column header node used in DLL solve process
    public ColumnNode DLL_grid(int[][] grid){
        final int COLS = grid[0].length;
        final int ROWS = grid.length;

        ColumnNode headerNode = new ColumnNode("header");
        ArrayList<ColumnNode> columnNodes = new ArrayList<ColumnNode>();

        for(int i = 0; i < COLS; i++){
            ColumnNode n = new ColumnNode(Integer.toString(i));
            columnNodes.add(n);
            headerNode = (ColumnNode) headerNode.hookRight(n);
        }
        headerNode = headerNode.right.col;

        for(int i = 0; i < ROWS; i++){
            Node prev = null;
            for(int j = 0; j < COLS; j++){
                if (grid[i][j] == 1){
                    ColumnNode c = columnNodes.get(j);
                    Node newNode = new Node(c);
                    if (prev == null)
                        prev = newNode;
                    c.up.hookDown(newNode);
                    prev = prev.hookRight(newNode);
                    c.size++;
                }
            }
        }
        headerNode.size = COLS;

        return headerNode;
    };

    public void runSolver(){
        solution = new LinkedList<Node>();
        search(root,0,solution);
        //if(verbose) showInfo();
    }

}
