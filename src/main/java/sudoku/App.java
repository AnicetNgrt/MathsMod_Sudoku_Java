package sudoku;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        GenerateurSudoku générateur = new GenerateurSudoku();
        int taille = 3;
        GraphColore sudoku = new GraphColoreParHashmap();
        générateur.générerContraintesSudoku(sudoku, taille);
        System.out.println(sudoku);
        afficherSudoku(sudoku);
    }

    public static void afficherSudoku(GraphColore sudoku) {
        final int width = 50;
        final int height = 50;
        String[][] buffer = new String[height][width];
        for(int row = 0; row < height; row++) {
            for(int col = 0; col < width; col++) {
                buffer[row][col] = " ";
            }
        }
        
        for(int[] sommetColoré:sudoku.listeSommetsColorés()) {
            int numCase = sommetColoré[0];
            int couleur = sommetColoré[1];
            int x = (numCase % 1000)*2;
            int y = (numCase / 1000)*2;
            if(x < width && y < height) {
                for(int i = 0; i < 3; i++) {
                    for(int j = 0; j < 3; j++) {
                        boolean estBordHoriz = i == 0 || i == 2;
                        boolean estBordVert = j == 0 || j == 2;
                        boolean estCentre = j == 1 && i == 1;
                        if(estBordHoriz && estBordVert) buffer[x+i][y+j] = "+";
                        else if(estBordHoriz) buffer[x+i][y+j] = "----";
                        else if(estBordVert) buffer[x+i][y+j] = "|";
                        else if(estCentre) buffer[x+i][y+j] = couleur == -1 ? "  ? " : "  "+(65+couleur)+" ";
                    }
                }
            }
        }

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                System.out.print(buffer[y][x]);
            }
            System.out.print("\n");
        }
    }
}
