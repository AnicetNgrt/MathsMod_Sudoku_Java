package sudoku;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    // Optimisation à l'aide d'HashSets
	public List<Integer[]> listeLiaisons() {
        Set<Integer[]> liaisons = new HashSet<Integer[]>();
        for(int i = 0; i < this.liaisons.size(); i++) {
            liaisons.add(new Integer[] {this.liaisons.get(i)[0], this.liaisons.get(i)[1]});
        }
        return new ArrayList<Integer[]>(liaisons);
    }

    // O(n)
    // Optimisation à l'aide d'HashSets
	public List<Integer> listeSommets() {
        HashSet<Integer> sommets = new HashSet<Integer>();
        for(int i = 0; i < liaisons.size(); i++) {
            int sommet1 = liaisons.get(i)[0];
            int sommet2 = liaisons.get(i)[1];
            sommets.add(sommet1);
            sommets.add(sommet2);
        }
        return new ArrayList<Integer>(sommets);
    }

    // O(n)
    public List<Integer> listeLiaisonsSommet(int sommet) {
        HashSet<Integer> liaisons = new HashSet<Integer>();
        for(int i = 0; i < this.liaisons.size(); i++) {
            if(this.liaisons.get(i)[1] == sommet) {
                int nouvLiaison = this.liaisons.get(i)[0];
                liaisons.add(nouvLiaison);
            } else if(this.liaisons.get(i)[0] == sommet) {
                int nouvLiaison = this.liaisons.get(i)[1];
                liaisons.add(nouvLiaison);
            }
        }
        return new ArrayList<Integer>(liaisons);
    }
}