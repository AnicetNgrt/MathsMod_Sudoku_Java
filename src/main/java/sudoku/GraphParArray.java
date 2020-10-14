package sudoku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

// On implémente les graphes en utilisant des
// arraylist car leur complexité en accès et en
// ajout est constante
public class GraphParArray implements Graph {

    private ArrayList<Integer[]> liaisons = new ArrayList<Integer[]>();

    // Pire cas O(n), meilleur cas O(1), cas moyen O(n)
	public boolean sontConnectés(int sommet1, int sommet2) {
        for(Integer[] liaison: liaisons) {
            if(liaison[0] == sommet1 && liaison[1] == sommet2) return true;
            if(liaison[1] == sommet1 && liaison[0] == sommet2) return true;
        }
        return false;
    }

    // O(1)
	public void connecterBinaire(int sommet1, int sommet2) {
        liaisons.add(new Integer[] {sommet1, sommet2});
    }

    // O(1) pour chaque sommet
	public void connecterMultiple(int sommet, Set<Integer> sommets) {
        for(Integer s:sommets) {
            connecterBinaire(sommet, s);
        }
    }

	// O(n)
	public Set<Integer[]> listeLiaisons() {
        Set<Integer[]> liaisons = new HashSet<Integer[]>();
        for(int i = 0; i < this.liaisons.size(); i++) {
            liaisons.add(new Integer[] {this.liaisons.get(i)[0], this.liaisons.get(i)[1]});
        }
        return liaisons;
    }

    // Meilleur cas O(n), cas moyen O(n), pire cas O(n²)
	public Set<Integer> listeSommets() {
        HashSet<Integer> sommets = new HashSet();
        for(int i = 0; i < liaisons.size(); i++) {
            int sommet1 = liaisons.get(i)[0];
            int sommet2 = liaisons.get(i)[1];
            sommets.add(sommet1);
            sommets.add(sommet2);
        }
        return sommets;
    }

    // Meilleur cas O(n), cas moyen O(n), pire cas O(n²)
    public Set<Integer> listeLiaisonsSommet(int sommet) {
        HashSet<Integer> liaisons = new HashSet<Integer>();
        for(int i = 0; i < this.liaisons.size(); i++) {
            int nouvLiaisonPotentielle = this.liaisons.get(i)[1];
            if(this.liaisons.get(i)[1] == sommet) {
                nouvLiaisonPotentielle = this.liaisons.get(i)[0];
            }
            liaisons.add(nouvLiaisonPotentielle);
        }
        return liaisons;
    }
}