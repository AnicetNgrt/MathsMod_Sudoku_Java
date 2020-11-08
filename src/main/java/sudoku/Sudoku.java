package sudoku;

import java.lang.StringBuilder;

public class Sudoku {
    public int taille = 3;
    public GraphColore graph = null;
    public boolean sontContraintesGénérées = false;


    public String[][] toBuffer() {
        final int height = taille*taille*2 + 1;
        final int cellLength = String.valueOf(taille*taille).length();
        final int width = taille*taille*3 + 1;

        String[][] buffer = new String[height][width];

        for(int row = 0; row < height; row++) {
            for(int col = 0; col < width; col++) {
                buffer[row][col] = " ";
            }
        }
        
        for(int[] sommetColoré:graph.listeSommetsColorés()) {
            int numCase = sommetColoré[0];
            int couleur = sommetColoré[1];
            int[] crds = GenerateurSudoku.décoderCoordonnées(numCase);
            int x = crds[1];
            int y = crds[0];
            int debutCol = x*(cellLength+1);
            int debutLigne = y*2;
            if(debutCol+2 < width && debutLigne+2 < height) {
                for(int i = 0; i < 3; i++) {
                    for(int j = 0; j < 3; j++) {
                        boolean estBordGauche = i == 0;
                        boolean estBordDroit = i == 2;
                        boolean estBordHaut = j == 0;
                        boolean estBordBas = j == 2;
                        boolean estCentre = j == 1 && i == 1;
                        boolean estCoin = (i == 0 || i == 2) && (j == 0 || j == 2);

                        if(estCoin) {
                            buffer[debutLigne+j][debutCol+i] = "+";
                        } else if(estBordHaut || (estBordBas && y == taille*taille-1)) {
                            buffer[debutLigne+j][debutCol+i] = "--";
                            for(int k = 0; k < cellLength; k++)
                                buffer[debutLigne+j][debutCol+i] += "-";
                        }
                        else if(estBordGauche || (estBordDroit && x == taille*taille-1))
                            buffer[debutLigne+j][debutCol+i] = "|";
                        else if(estCentre) {
                            buffer[debutLigne+j][debutCol+i] = String.format(" %"+Integer.toString(cellLength)+"s ", couleur == -1 ? "?" : couleur);
                            if(couleur != -1)
                                buffer[debutLigne+j][debutCol+i] = ANSI.YELLOW+buffer[debutLigne+j][debutCol+1]+ANSI.BLUE;
                        }
    
                        if(x % taille == 0 && estBordGauche && x != 0) {
                            buffer[debutLigne+j][debutCol+i] = ANSI.GREEN+buffer[debutLigne+j][debutCol+i]+ANSI.BLUE;
                        }
                        if(y % taille == taille-1 && estBordBas && y != taille*taille-1) {
                            buffer[debutLigne+j][debutCol+i] = ANSI.GREEN+buffer[debutLigne+j][debutCol+i]+ANSI.BLUE;
                        }
                        if(y % taille == 0 && estBordHaut && y != 0) {
                            buffer[debutLigne+j][debutCol+i] = ANSI.GREEN+buffer[debutLigne+j][debutCol+i]+ANSI.BLUE;
                        }
                    }
                }
            }  
        }

        buffer[0][0] = ANSI.BLUE + buffer[0][0];
        buffer[height-1][width-1] += ANSI.RESET;
        return buffer;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        String[][] buffer = toBuffer();

        for(String[] line:buffer) {
            sb.append("      ");
            for(String cell:line) {
                sb.append(cell);
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
