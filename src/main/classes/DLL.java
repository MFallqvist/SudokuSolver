package main.classes;

import java.util.ArrayList;
import java.util.List;

// Class for Doubly Linked List
public class DLL {
    private ColumnNode root; // root of list
    private List<Node> solution = new ArrayList<>();
    private int solutions;

    DLL(int[][] sudoku){
        root = DLL_grid(sudoku);
        runSolver();
    }
    /* Doubly Linked list Node*/
    public class Node {
        int data=1, row;
        Node up, down, left, right;
        ColumnNode col;

        // Constructor to create a new node
        // next and prev is by default initialized as null
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
        }
    }
    // Based on Algorithm-X
    private void search(int k){
        System.out.print("Function call search, depth: "+k+"\n");
        Node c, r, j;
        if(root.right==root) {
            print_solution();
            return;
        }else{
            c = choose_column_object();
            r = c.down;
            while (r!=c){
                solution.add(r);
                System.out.print("solution size: "+solution.size()+"\n");
                j = r.right;
                while (j!=r){
                    cover(j.col);
                    j = j.right;
                }
                search(k+1);
                // Pop data object from s
                r = solution.remove(solution.size()-1);
                c = r.col;
                j = r.left;
                while (j!=r){
                    uncover(j.col);
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
        //System.out.print("Function call uncover, c.col.size: " +c.col.size+" \n");
        Node i, j;
        i = c.up;
        while(i!=c){
            j = i.left;
            while (j!=i){
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
        //System.out.print("Function call cover, c.col.size: " +c.col.size+" \n");
        Node i, j;
        c.right.left = c.left;
        c.left.right = c.right;
        i = c.down;
        while (i!=c){
            j = i.right;
            while (j!=i){
                j.down.up = j.up;
                j.up.down = j.down;
                j.col.size -= 1; // S[C[j]] ← S[C[j]] − 1
                j = j.right;
            }
            i = i.down;
        }
    }

    ColumnNode choose_column_object(){
        //System.out.print("Function call choose_column_object\n");
        ColumnNode chosen;
        Node candidate;
        int min = Integer.MAX_VALUE;
        candidate = root.right;
        chosen = null;
        while (candidate!=root){
            if (candidate.col.size < min){
                //System.out.print("chosen a candidate with col.size: "+candidate.col.size +" and name: " + candidate.col.name+ "\n");
                chosen = candidate.col;
                min = chosen.col.size;
            }
            else{
                candidate=candidate.right;
                continue;
            }

        }
        return chosen;
    }
    void print_solution(){
        System.out.print("Function call print_solution, size: " +solution.size()+"\n");
        Node i;
        try {
            i = solution.remove(solution.size() - 1);
            while (i != i.right) {
                System.out.print(i.col.name); // print (N[C[i]])
                i = i.right;
            }
        }catch(Exception e){
            new Error("No solution found: "+e);
        }
    }
    // Returns the root column header node used in DLL solve process
    public ColumnNode DLL_grid(int[][] grid){
        //System.out.print("Function call DLL_grid \n");
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
        search(0);
        print_solution();
    }

}
