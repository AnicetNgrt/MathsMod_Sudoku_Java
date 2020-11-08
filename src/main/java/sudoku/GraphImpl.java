package sudoku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class GraphImpl implements Graph {

    //Utilisation de 2 structures de données
    //2 fois plus de mémoire utilisée mais plus de versatilité qui
    //ont mené à une perte de 4 exposants dans la complexité.
    protected HashMap<Integer, ArrayList<Integer>> liaisonsDirectes = new HashMap<Integer, ArrayList<Integer>>();
    protected ArrayList<Integer[]> liaisons = new ArrayList<Integer[]>();

    // Pire cas O(n), meilleur cas O(1), cas moyen O(n)
	public boolean sontConnectés(int sommet1, int sommet2) {
        ArrayList<Integer> l1 = liaisonsDirectes.get(sommet1);
        ArrayList<Integer> l2 = liaisonsDirectes.get(sommet2);
        boolean connectés = false;
        if(l1 == null && l2 == null) {
            return false;
        }
        if(l2 != null) {
            connectés |= l2.contains(sommet1);
        }
        if(l1 != null) {
            connectés |= l1.contains(sommet2);
        }
        return false;
    }

    // O(1)
	public void connecterBinaire(int sommet1, int sommet2) {
        liaisons.add(new Integer[] {sommet1, sommet2});
        if(liaisonsDirectes.get(sommet1) == null)
            liaisonsDirectes.put(sommet1, new ArrayList<Integer>());
        liaisonsDirectes.get(sommet1).add(sommet2);
        if(liaisonsDirectes.get(sommet2) == null)
            liaisonsDirectes.put(sommet2, new ArrayList<Integer>());
        liaisonsDirectes.get(sommet2).add(sommet1);
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

    // O(1)
    public List<Integer> listeLiaisonsSommet(int sommet) {
        HashSet<Integer> liaisons = new HashSet<Integer>();
        if(liaisonsDirectes.get(sommet) == null) {
            return new ArrayList<Integer>();
        }
        return liaisonsDirectes.get(sommet);
    }
}