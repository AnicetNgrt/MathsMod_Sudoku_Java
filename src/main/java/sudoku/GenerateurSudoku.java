package sudoku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class GenerateurSudoku {

    //Variable de cache pour ne pas recalculer les contraintes quand ça a déjà été fait
    private HashMap<Integer, List<List<Integer>>> groupesDeContraintesParTaille = new HashMap<>();

    //Derniers groupes de contraintes calculés
    public List<List<Integer>> groupesDeContraintes = null;

    //Générer toutes les groupes de contraintes d'un sudoku d'une certaine taille
    //et les stocker dans le cache
    private void générerGroupesDeContraintes(int taille) {
        List<List<Integer>> groupesDeContraintes = new ArrayList<>();
        for(int i = 0; i < taille*taille*3; i++) {
            groupesDeContraintes.add(new ArrayList<Integer>());
        }

		for(int row = 0; row < taille*taille; row++) {
			for(int col = 0; col < taille*taille; col++) {
                // On encode les coordonnées de la case dans un entier base 10
                int numCase = encoderCoordonnées(row, col);
                int numGroupe = taille*(row/taille) + (col/taille);
				groupesDeContraintes.get(row).add(numCase);
				groupesDeContraintes.get(taille*taille+col).add(numCase);
				groupesDeContraintes.get(2*taille*taille+numGroupe).add(numCase);
			}
        }

        groupesDeContraintesParTaille.put(taille, groupesDeContraintes);
    }

    //Générer toutes les contraintes d'un sudoku à base de graphe
    //en liant les cases d'un même groupe entre elles.
    public void générerContraintesSudoku(Sudoku sudoku) {
        int taille = sudoku.taille;
        GraphColore graph = sudoku.graph;
        // On crée des groupes de contraintes pour chaque ligne/colonne/zone
        // Sauf si on les a déjà crées par le passé (optimisation)
        groupesDeContraintes = groupesDeContraintesParTaille.get(taille);
        if(groupesDeContraintes == null) {
            générerGroupesDeContraintes(taille);
            groupesDeContraintes = groupesDeContraintesParTaille.get(taille);
        }
        
        // On relie toutes les cases entre elles dans le graphe coloré en fonction
        // de leurs différents groupes de contraintes
		for(List<Integer> groupeDeContraintes: groupesDeContraintes) {
			for(int i = 0; i < groupeDeContraintes.size(); i++) {
				for(int j = i+1; j < groupeDeContraintes.size(); j++) {
					graph.connecterBinaire(groupeDeContraintes.get(i), groupeDeContraintes.get(j));
				}
			}
		}
    }

    public void générerValeursDépartSudoku(Sudoku sudoku, int nb) {
        List<Integer> listeSommets = sudoku.graph.listeSommets();
        for(int i = 0; i < nb;) {
            int numCase = listeSommets.get(entierAléatoireIntervale(0, listeSommets.size()-1));
            if(!sudoku.graph.estSommetColoré(numCase)) {
                int couleur = entierAléatoireIntervale(0, sudoku.taille*sudoku.taille-1);
                int essais = 0;
                while(!sudoku.graph.estCouleurCorrecte(numCase, couleur)) {
                    couleur = entierAléatoireIntervale(0, sudoku.taille*sudoku.taille-1);
                    if(essais++ > sudoku.taille*sudoku.taille*1000) break;
                }
                sudoku.graph.colorerSommet(numCase, couleur);
                i++;
            }
        }
    }

    public static int encoderCoordonnées(int row, int col) {
        return row * 1000 + col;
    }

    public static int[] décoderCoordonnées(int crd) {
        return new int[]{crd/1000, crd%1000};
    }

    //Bornes inclusives
    public static int entierAléatoireIntervale(int min, int max) {
        return (int)(Math.random() * ((max - min) + 1)) + min;
    }
}