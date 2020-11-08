package sudoku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GenerateurSudoku {

    HashMap<Integer, List<List<Integer>>> groupesDeContraintesParTaille = new HashMap<>();

    private void générerGroupesDeContraintes(int taille) {
        List<List<Integer>> groupesDeContraintes = new ArrayList<>();
        for(int i = 0; i < taille*taille*3; i++) {
            groupesDeContraintes.add(new ArrayList<Integer>());
        }

		for(int row = 0; row < taille*taille; row++) {
			for(int col = 0; col < taille*taille; col++) {
                // On encode les coordonnées de la case dans un entier base 10
                int numCase = row * 1000 + col;
                int numGroupe = taille*(row/taille) + (col/taille);
				groupesDeContraintes.get(row).add(numCase);
				groupesDeContraintes.get(taille*taille+col).add(numCase);
				groupesDeContraintes.get(2*taille*taille+numGroupe).add(numCase);
			}
        }

        groupesDeContraintesParTaille.put(taille, groupesDeContraintes);
    }

    public void générerContraintesSudoku(GraphColore sudoku, int taille) {
        // On crée des groupes de contraintes pour chaque ligne/colonne/zone
        // Sauf si on les a déjà crées par le passé (optimisation)
        List<List<Integer>> groupesDeContraintes = groupesDeContraintesParTaille.get(taille);
        if(groupesDeContraintes == null) {
            générerGroupesDeContraintes(taille);
            groupesDeContraintes = groupesDeContraintesParTaille.get(taille);
        }
        
        // On relie toutes les cases entre elles dans le graphe coloré en fonction
        // de leurs différents groupes de contraintes
		for(List<Integer> groupeDeContraintes: groupesDeContraintes) {
			for(int i = 0; i < groupeDeContraintes.size(); i++) {
				for(int j = i+1; j < groupeDeContraintes.size(); j++) {
					sudoku.connecterBinaire(groupeDeContraintes.get(i), groupeDeContraintes.get(j));
				}
			}
		}
    }
}