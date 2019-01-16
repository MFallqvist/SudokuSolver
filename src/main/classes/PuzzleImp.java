package main.classes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// The solution is built on reduction to binary matrix M built with doubly linked lists
public class PuzzleImp implements Puzzle {
    private static int N = 9;
    int clues;
    byte[][] sudoku = new byte[N][N];

    // Returns true if puzzle is solved
    public boolean isSolved() {
        if (clues == N*N){
            return true;
        }
        else{
            return false;
        }
    }

    // Returns true if board is valid
    public boolean isValid() {
        // 17 clues need atleast for a unique solution
        if (clues < 17){
            return false;
        }else{
            return true;
        }
    }

    // Generates a solution. Does not modify the current puzzle.
    public Puzzle solve() {
        Puzzle solution = null;
        int[][] binary_grid = createBinaryGrid();
        DLL solver = new DLL(binary_grid);

        return solution;
    }

    // Loads a puzzle from a file
    public void load(String path) {
        // stream into list then put inside matrix (represented by Vector)
        List<String> input;
        String dot = ".";
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            input = stream
                    .collect(Collectors.toList());

        // Store data in matrix, also count given amount of clues
            clues = 0;
        for (int i = 0; i < N; i++) {
            //Vector<Integer> r = new Vector<>();
            byte[] r = new byte[N];
            for (int j = 0; j < N; j++) {
                if (dot.charAt(0) == input.get(i).charAt(j)){
                    //r.add(0);
                    r[j] = 0;
                }else{
                    clues++;
                    r[j] = (byte)Character.getNumericValue(input.get(i).charAt(j));
                    //r.add(Character.getNumericValue(input.get(i).charAt(j)));
                }
            }
            sudoku[i] = r;
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        showPuzzle();
    }

    // Saves a puzzle to a file
    public void save(String path) {

    }
    // Prints the puzzle
    public void showPuzzle() {
        for (int i = 0; i < N; i++) {
            //Vector<Integer> r = sudoku.get(i);
            for (int j = 0; j < N; j++) {
                System.out.print(sudoku[i][j]+" ");
            }
            System.out.println();
        }
    }

    // Code added and modified from github rafailo 20190115
    // sudoku has numbers 1-9. A 0 indicates an empty cell that we will need to
    // fill in.
    private int[][] createBinaryGrid(){
        int[][] R = sudokuExactCover();
        for(int i = 1; i <= N; i++){
            for(int j = 1; j <= N; j++){
                int n = sudoku[i - 1][j - 1];
                if (n != 0){ // zero out in the constraint board
                    for(int num = 1; num <= N; num++){
                        if (num != n){
                            Arrays.fill(R[getIdx(i, j, num)], 0);
                        }
                    }
                }
            }
        }
        return R;
    }

    // Returns the base exact cover grid for a SUDOKU puzzle
    private int[][] sudokuExactCover(){
        int[][] R = new int[N*N*N][4*N*N];

        int hBase = 0;

        // row-column constraints
        for(int r = 1; r <= N; r++){
            for(int c = 1; c <= N; c++, hBase++){
                for(int n = 1; n <= N; n++){
                    R[getIdx(r, c, n)][hBase] = 1;
                }
            }
        }

        // row-number constraints
        for(int r = 1; r <= N; r++){
            for(int n = 1; n <= N; n++, hBase++){
                for(int c1 = 1; c1 <= N; c1++){
                    R[getIdx(r, c1, n)][hBase] = 1;
                }
            }
        }

        // column-number constraints

        for(int c = 1; c <= N; c++){
            for(int n = 1; n <= N; n++, hBase++){
                for(int r1 = 1; r1 <= N; r1++){
                    R[getIdx(r1, c, n)][hBase] = 1;
                }
            }
        }

        // box-number constraints

        for(int br = 1; br <= N; br += N/3){
            for(int bc = 1; bc <= N; bc += N/3){
                for(int n = 1; n <= N; n++, hBase++){
                    for(int rDelta = 0; rDelta < N/3; rDelta++){
                        for(int cDelta = 0; cDelta < N/3; cDelta++){
                            R[getIdx(br + rDelta, bc + cDelta, n)][hBase] = 1;
                        }
                    }
                }
            }
        }

        return R;
    }
    // row [1,S], col [1,S], num [1,S]
    private int getIdx(int row, int col, int num){
        return (row - 1) * N * N + (col - 1) * N + (num - 1);
    }


}