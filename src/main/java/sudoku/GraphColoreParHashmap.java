package sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.List;

public class GraphColoreParHashmap extends GraphImpl implements GraphColore {
    private Set<Integer> couleursPossibles = new HashSet<Integer>();
    private HashMap<Integer, Integer> couleurs = new HashMap<Integer, Integer>();
    private HashMap<Integer, Set<Integer>> couleursCorrectes = new HashMap<Integer, Set<Integer>>();
    private HashMap<Integer, Integer> poidsCouleurs = new HashMap<Integer, Integer>();
    
    public void connecterBinaire(int sommet1, int sommet2) {
        super.connecterBinaire(sommet1, sommet2);
        if (couleurs.get(sommet1) == null) {
            couleurs.put(sommet1, -1);
            couleursCorrectes.put(sommet1, new HashSet<Integer>(couleursPossibles));
        }
        if (couleurs.get(sommet2) == null) {
            couleurs.put(sommet2, -1);
            couleursCorrectes.put(sommet2, new HashSet<Integer>(couleursPossibles));
        }
    }

    public int[][] listeSommetsColorés() {
        int[][] sommetsColorés = new int[couleurs.size()][2];
        int i = 0;
        for(Map.Entry<Integer, Integer> pair: couleurs.entrySet()) {
            sommetsColorés[i][0] = pair.getKey();
            sommetsColorés[i][1] = pair.getValue();
            i++;
        }
        return sommetsColorés;
    }

	public void colorerSommet(int sommet, int couleur) {
        couleurs.put(sommet, couleur);
        if(couleur != -1) {
            for(int l:listeLiaisonsSommet(sommet)) {
                if(poidsCouleurs.get(l*1000 + couleur) == null){
                    poidsCouleurs.put(l*1000 + couleur, -1);
                } else {
                    poidsCouleurs.put(l*1000 + couleur, poidsCouleurs.get(l*1000 + couleur)-1);
                }
                couleursCorrectes.get(l).remove(couleur);
            }
        }
    }

	public void décolorerSommet(int sommet) {
        int couleur = couleurs.get(sommet);
        if(couleur != -1) {
            for(int l:listeLiaisonsSommet(sommet)) {
                if(poidsCouleurs.get(l*1000 + couleur) == null){
                    poidsCouleurs.put(l*1000 + couleur, 1);
                } else {
                    poidsCouleurs.put(l*1000 + couleur, poidsCouleurs.get(l*1000 + couleur)+1);
                }
                if(poidsCouleurs.get(l*1000 + couleur) >= 0){
                    couleursCorrectes.get(l).add(couleur);
                }
            }
        }
        couleurs.put(sommet, -1);
    }

    //O(1)
	public boolean estCouleurCorrecte(int sommet, int couleur) {
        return couleursCorrectes.get(sommet).contains(couleur);
    }

	public boolean estColorationCorrecte() {
        List<Integer> sommets = listeSommets();
        for(int s:sommets)
            if(!estCouleurCorrecte(s, couleurs.get(s))) return false;
        return true;
    }

	public int nbColorationsCorrectesPossibles() {
        return -1;
    }

    public String toString() {
        String s = "";
        for(int[] colorations:listeSommetsColorés()) {
            s += "som: "+numCaseToString(colorations[0])+" col: "+colorations[1]+" lia: [";
            Object[] liaisons = listeLiaisonsSommet(colorations[0]).toArray();
            Arrays.sort(liaisons);
            for(Object sommet: liaisons){
                s += numCaseToString(((int) sommet))+", ";
            }
            s += "]\n";
        }
        return s;
    }

    private static String numCaseToString(int numCase) {
        String s = numCase / 1000 + "|" + numCase % 1000;
        return s;
    }

    public boolean estSommetColoré(int sommet) {
        if(couleurs.get(sommet) == null) return false;
        if(couleurs.get(sommet) == -1) return false;
        return true;
    }

    //O(1)
    public Set<Integer> couleursCorrectes(int sommet) {
        return couleursCorrectes.get(sommet);
    }

    public void réinitialiser() {
        for(int numCase:listeSommets()) {
            décolorerSommet(numCase);
        }
    }

    public GraphColore dupliquer() {
        GraphColoreParHashmap dup = new GraphColoreParHashmap();
        dup.couleurs = new HashMap<Integer, Integer>(couleurs);
        dup.liaisons = new ArrayList<Integer[]>(liaisons);
        dup.liaisonsDirectes = new HashMap<>(liaisonsDirectes);
        dup.couleursCorrectes = new HashMap<>();
        for(Entry<Integer, Set<Integer>> entry:couleursCorrectes.entrySet()) {
            dup.couleursCorrectes.put(entry.getKey(), new HashSet<Integer>(entry.getValue()));
        }
        dup.couleursPossibles = new HashSet<>(couleursPossibles);
        dup.poidsCouleurs = new HashMap<>(poidsCouleurs);
        return dup;
    }

    public Set<Integer> couleursPossibles() {
        return couleursPossibles;
    }

    public void ajouterCouleurPossible(int couleur) {
        couleursPossibles.add(couleur);
    }

    public int couleurSommet(int sommet) {
        return couleurs.get(sommet);
    }
}
