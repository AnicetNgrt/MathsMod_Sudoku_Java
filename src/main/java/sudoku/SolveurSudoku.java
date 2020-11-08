package sudoku;

import java.util.List;
import java.util.Set;

public class SolveurSudoku {

    public float seuilUtilisationMéthodeEntropique = 0.7f;

    public void résoudre(Sudoku sudoku) {
        int nbCases = sudoku.taille*sudoku.taille*sudoku.taille*sudoku.taille;

        List<Integer> cases = sudoku.casesEntropieMinNonRésolues();
        if(cases.size() == 0 || 0 >= nbCases) {
            System.out.println("Déjà fini");
            return;
        }
        System.out.println("Veuillez patienter...");
        trouverCheminSuivant(sudoku, nbCases, 0, cases);
    }

    public boolean trouverCheminSuivant(
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
    }

    public boolean resoudreRec(
        Sudoku sudoku,
        int numCase,
        int nbRésolues,
        int nbObjectif
    ) {
        Set<Integer> couleursCorrectes = sudoku.graph.couleursCorrectes(numCase);
        if(couleursCorrectes.size() == 0) {
            // Case impossible
            return false;
        }
        int couleur = 0;
        int randi = entierAléatoireIntervale(0, couleursCorrectes.size()-1);
        int i = 0;
        for(int c:couleursCorrectes) {
            if(i++ == randi) {
                couleur = c;
                if(!sudoku.graph.estCouleurCorrecte(numCase, couleur)) System.out.println("oh");
            }
        }
        sudoku.graph.colorerSommet(numCase, couleur);
        nbRésolues++;
        //System.out.println("\033[1A"+"Valeurs trouvées sur la branche explorée: "+nbRésolues+"/"+nbObjectif);

        List<Integer> cases = nbRésolues > seuilUtilisationMéthodeEntropique*nbObjectif ? sudoku.casesEntropieMinNonRésolues() : sudoku.casesEntropieSuffNonRésolues();
        if(cases.size() == 0 || nbRésolues >= nbObjectif) {
            // Plus rien à résoudre
            return true;
        }
        
        if(!trouverCheminSuivant(sudoku, nbObjectif, nbRésolues, cases)) {
            sudoku.graph.décolorerSommet(numCase);
            return false;
        } else {
            return true;
        }
    }

    //Bornes inclusives
    public static int entierAléatoireIntervale(int min, int max) {
        return (int)(Math.random() * ((max - min) + 1)) + min;
    }
}
