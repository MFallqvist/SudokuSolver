package classes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// The solution is built on reduction to binary matrix M built with doubly linked lists
public class PuzzleImp implements Puzzle {
    Vector<Vector<Integer>> sudoku = new Vector<Vector<Integer>>();
    int N = 9;
    int clues;

    // Returns true if puzzle is solved
    public boolean isSolved() {
        return true;
    }

    // Returns true if board is valid
    public boolean isValid() {
        // 17 clues need atleast for a unique solution
        if (clues < 17){
            // Also check constraints
            return false;
        }else{
            return true;
        }
    }

    // Generates a solution. Does not modify the current puzzle.
    public Puzzle solve() {
        Puzzle solution = null;
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

        // Store data in Vector, also count given amount of clues
            clues = 0;
        for (int i = 0; i < N; i++) {
            Vector<Integer> r = new Vector<>();
            for (int j = 0; j < N; j++) {
                if (dot.charAt(0) == input.get(i).charAt(j)){
                    r.add(0);
                }else{
                    clues++;
                    r.add(Character.getNumericValue(input.get(i).charAt(j)));
                }

            }
            sudoku.add(r);
        }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Now reduce Sudoku problem and translate to M

        showPuzzle();
    }

    // Saves a puzzle to a file
    public void save(String path) {

    }

    public void showPuzzle() {
        for (int i = 0; i < N; i++) {
            Vector<Integer> r = sudoku.get(i);
            for (int j = 0; j < N; j++) {
                System.out.print(r.get(j));
            }
            System.out.println();
        }
    }
}