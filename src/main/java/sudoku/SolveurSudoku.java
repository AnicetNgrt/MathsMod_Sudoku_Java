package sudoku;

import java.util.List;

public interface SolveurSudoku {
    public void modeVerbeux(boolean estActif);
    public void résoudre(Sudoku sudoku, int nb);
    public String nom();
}
