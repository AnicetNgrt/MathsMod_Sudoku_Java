package sudoku;

import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Sudoku {

    public static String couleurVide = ANSI.PURPLE;
    public static String couleurRempli = ANSI.CYAN;
    public static String couleurZone = ANSI.GREEN;

    public int taille = 3;
    public GraphColore graph = null;

    public int[] listeCouleurs() {
        int[] couleurs = new int[taille*taille];
        for(int i = 0; i < taille*taille; couleurs[i] = i++);
        return couleurs;
    }

    public List<Integer> casesEntropieMinNonRésolues() {
        int entropieMin = taille*taille;
        ArrayList<Integer> cases = new ArrayList<Integer>();
        for (int i = 0; i < taille*taille; i++) {
            for (int j = 0; j < taille*taille; j++) {
                int numCase = GenerateurSudoku.encoderCoordonnées(i, j);
                if(graph.estSommetColoré(numCase)) 
                    continue;
                Set<Integer> correctes = graph.couleursCorrectes(numCase);
                
                if(correctes.size() == entropieMin) {
                    cases.add(numCase);
                } else if(correctes.size() < entropieMin) {
                    cases = new ArrayList<Integer>();
                    entropieMin = correctes.size();
                    cases.add(numCase);
                }
            }
        }
        return cases;
    }

    public List<Integer> casesNonRésolues() {
        ArrayList<Integer> cases = new ArrayList<Integer>();
        for (int i = 0; i < taille*taille; i++) {
            for (int j = 0; j < taille*taille; j++) {
                int numCase = GenerateurSudoku.encoderCoordonnées(i, j);
                if(graph.estSommetColoré(numCase)) 
                    continue;
                cases.add(numCase);
            }
        }
        return cases;
    }

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
                                buffer[debutLigne+j][debutCol+i] = couleurRempli+buffer[debutLigne+j][debutCol+1]+couleurVide;
                        }
    
                        if(x % taille == 0 && estBordGauche && x != 0) {
                            buffer[debutLigne+j][debutCol+i] = couleurZone+buffer[debutLigne+j][debutCol+i]+couleurVide;
                        }
                        if(y % taille == taille-1 && estBordBas && y != taille*taille-1) {
                            buffer[debutLigne+j][debutCol+i] = couleurZone+buffer[debutLigne+j][debutCol+i]+couleurVide;
                        }
                        if(y % taille == 0 && estBordHaut && y != 0) {
                            buffer[debutLigne+j][debutCol+i] = couleurZone+buffer[debutLigne+j][debutCol+i]+couleurVide;
                        }
                    }
                }
            }  
        }

        buffer[0][0] = couleurVide + buffer[0][0];
        buffer[height-1][width-1] += ANSI.RESET;
        return buffer;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder("\n");
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

    public Sudoku dupliquer() {
        Sudoku dup = new Sudoku();
        dup.taille = taille;
        dup.graph = graph.dupliquer();
        return dup;
    }
}
