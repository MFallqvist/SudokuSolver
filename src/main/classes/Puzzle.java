package main.classes;
public interface Puzzle {
    // Returns true if puzzle is solved
    boolean isSolved();
    // Returns true if board is valid
    boolean isValid();
    // Generates a solution. Does not modify the current puzzle.
    Puzzle solve();
    // Loads a puzzle from a file
    void load(String path);
    // Saves a puzzle to a file
    void save(String path);
}