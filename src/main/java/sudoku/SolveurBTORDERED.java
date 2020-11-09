package sudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SolveurBTORDERED implements SolveurSudoku {

    private boolean modeVerbeux = false;
    private List<Integer> listeCases = null;

    public void résoudre(Sudoku sudoku, int nb) {
        listeCases = sudoku.casesNonRésolues();
        if(listeCases.size() == 0 || 0 >= nb) {
            System.out.println("Déjà fini");
            return;
        }
        System.out.println("Veuillez patienter...");
        resoudreRec(sudoku, listeCases, 0, 0, nb);
    }

    /*public boolean trouverCheminSuivant(
        Sudoku sudoku,
        int nbObjectif,
        int nbRésolues,
        List<Integer> cases
    ) {
        int indiceCaseSuivante = entierAléatoireIntervale(0, cases.size()-1);
        boolean succès = false;
        int essais = 0;
        while(!succès && essais < cases.size()) {
            indiceCaseSuivante = (indiceCaseSuivante + 1) % cases.size();
            succès = resoudreRec(sudoku, cases.get(indiceCaseSuivante), nbRésolues, nbObjectif);
            essais++;
        }
        return succès;
    }*/

    public boolean resoudreRec(
        Sudoku sudoku,
        List<Integer> listeCases,
        int indiceCase,
        int nbRésolues,
        int nbObjectif
    ) {
        if(indiceCase >= listeCases.size() || nbRésolues >= nbObjectif) {
            
            return true;
        }

        int numCase = listeCases.get(indiceCase);
        ArrayList<Integer> couleursCorrectes = new ArrayList<>(sudoku.graph.couleursCorrectes(numCase));
        
        if(couleursCorrectes.size() == 0) {
            
            return false;
        }

        int couleur = 0;
        int i = entierAléatoireIntervale(0, couleursCorrectes.size()-1);
        int max = couleursCorrectes.size();
        int essais = 0;
        boolean succès;

        do {
            succès = false;
            i = (i + 1) % max;
            couleur = couleursCorrectes.get(i);
            if(sudoku.graph.estCouleurCorrecte(numCase, couleur)) {
                sudoku.graph.colorerSommet(numCase, couleur);
                succès = resoudreRec(sudoku, listeCases, indiceCase+1, nbRésolues+1, nbObjectif);
            } else {
                System.out.println("oh");
            }
            essais++;
            if(!succès) {
                sudoku.graph.décolorerSommet(numCase);
            }
        } while(!succès && essais <= max);
        if(succès) {
            nbRésolues++;
        }
        
        if(modeVerbeux) System.out.println("\033[1A"+"Valeurs trouvées sur la branche explorée: "+nbRésolues+"/"+nbObjectif);
        return succès;
    }

    //Bornes inclusives
    public static int entierAléatoireIntervale(int min, int max) {
        return (int)(Math.random() * ((max - min) + 1)) + min;
    }

    public void modeVerbeux(boolean estActif) {
        modeVerbeux = estActif;
    }

    public String nom() {
        return "Backtracking choix des cases ordonné";
    }
}
