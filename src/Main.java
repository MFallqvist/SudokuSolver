import main.classes.Puzzle;
import main.classes.PuzzleImp;
// Created by Marcus F 20190114
// Sudoku solver based on KTH paper found at:
// https://www.kth.se/social/files/58861771f276547fe1dbf8d1/HLaestanderMHarrysson_dkand14.pdf
public class Main {

    public static void main(String[] args){
        Puzzle myPuzzle = new PuzzleImp();
        String path = "G:\\Arbete\\Git\\SudokuSolver\\Data\\puzzles.txt";
        myPuzzle.load(path);
        myPuzzle.solve();
    }

}
