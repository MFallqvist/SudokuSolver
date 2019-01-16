package main.classes;

import java.util.List;

public class SolutionParser {
    private List<DLL.Node> solution;
    public PuzzleImp grid = new PuzzleImp();

    SolutionParser(List<DLL.Node> s){
        //System.out.print("Function call SolutionParser with size: "+s.size()+"\n");
        solution = s;
        parse();
    }
    private void parse(){
        byte[][] result = new byte[grid.N][grid.N];
            for(DLL.Node n : solution){
                DLL.Node rcNode = n;
                int min = Integer.parseInt(rcNode.col.name);
                for(DLL.Node tmp = n.right; tmp != n; tmp = tmp.right){
                    int val = Integer.parseInt(tmp.col.name);
                    if (val < min){
                        min = val;
                        rcNode = tmp;
                    }
                }
                int ans1 = Integer.parseInt(rcNode.col.name);
                int ans2 = Integer.parseInt(rcNode.right.col.name);
                int r = ans1 / grid.N;
                int c = ans1 % grid.N;
                byte num = (byte)((ans2 % grid.N) + 1);
                result[r][c] = num;
            }
        grid.sudoku = result;
    }
}
