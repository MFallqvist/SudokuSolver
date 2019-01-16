package main.classes;

import java.util.ArrayList;
import java.util.List;

// Class for Doubly Linked List
public class DLL {
    private ColumnNode root; // root of list
    public List<Node> solution = new ArrayList<>();
    //private int solutions;

    // Constructor to be fed binary matrix, out comes DLL matrix
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
    // ColumnNode to present top row with size info and name
    public class ColumnNode extends Node{
        int size;
        String name;
        private ColumnNode(String n){
            super();
            name = n;
            size = 0;
            col = this;
        }
        // Used to backtrack to covered Node
        private void uncover(){
            //System.out.print("Function call uncover, c.col.size: " +c.col.size+" \n");
            Node i, j;
            i = col.up;
            while(i!=col){
                j = i.left;
                while (j!=i){
                    j.col.size += 1; //S[C[j]] ← S[C[j]] − 1
                    j.down.up = j;
                    j.up.down = j;
                    j = j.left;
                }
                i = i.up;
            }
            col.right.left = col;
            col.left.right = col;
        }
        // Used to cover Node and all connected data points in that same column
        private void cover(){
            //System.out.print("Function call cover, c.col.size: " +c.col.size+" \n");
            Node i, j;
            col.right.left = col.left;
            col.left.right = col.right;
            i = col.down;
            while (i!=col){
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
    }
    // Search is based on Algorithm-X to solve exact cover problem
    private void search(){
        search(0, solution);
    }
    // Search is based on Algorithm-X to solve exact cover problem
    private void search(int k, List<Node> sol){
        ColumnNode c;
        List<Node> tmp = new ArrayList<>();
        if(root.right==root) {
            solution.addAll(sol);
            return;
        }else{
            c = choose_column_object();
            c.cover();

            for(Node r = c.down; r != c; r = r.down){
                sol.add(r);

                for(Node j = r.right; j != r; j = j.right){
                    j.col.cover();
                }
                tmp.addAll(sol);
                search(k + 1, tmp);
                r = sol.remove(sol.size() - 1);
                c = r.col;

                for(Node j = r.left; j != r; j = j.left){
                    j.col.uncover();
                }
            }
            c.uncover();
        }
        solution.addAll(sol);
        return;
    }

    // Choose the ColumnNode with fewest data points
    private ColumnNode choose_column_object(){
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
            }

        }
        return chosen;
    }
    // To print the current solution list
    void print_solution(){
        //System.out.print("Function call print_solution with solution.size(): " +solution.size()+"\n");
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
    private ColumnNode DLL_grid(int[][] grid){
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
    // Helper to run and print
    private void runSolver(){
        search();
    }

}
