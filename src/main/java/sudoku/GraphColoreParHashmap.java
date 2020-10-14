package sudoku;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import java.util.Iterator;

public class GraphColoreParHashmap extends GraphParArray implements GraphColore {
    private HashMap<Integer, Integer> couleurs = new HashMap<Integer, Integer>();
    
    @Override
    public void connecterBinaire(int sommet1, int sommet2) {
        super.connecterBinaire(sommet1, sommet2);
        if (couleurs.get(sommet1) == null)
            couleurs.put(sommet1, -1);
        if (couleurs.get(sommet2) == null)
            couleurs.put(sommet2, -1);
    }

    public int[][] listeSommetsColorés() {
        int[][] sommetsColorés = new int[couleurs.size()][2];
        Iterator<Entry<Integer, Integer>> it = couleurs.entrySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> pair = it.next();
            sommetsColorés[i][0] = pair.getKey();
            sommetsColorés[i][1] = pair.getValue();
            it.remove();
            i++;
        }
        return sommetsColorés;
    }

	public void colorerSommet(int sommet, int couleur) {
        couleurs.put(sommet, couleur);
    }

	public void décolorerSommet(int sommet, int couleur) {
        couleurs.put(sommet, -1);
    }

	public boolean estCouleurCorrecte(int sommet, int couleur) {
        if(couleur == -1) return true;
        Set<Integer> liaisons = listeLiaisonsSommet(sommet);
        for(int l:liaisons) {
            int c = couleurs.get(l);
            if(c != -1 && c == couleur) return false;
        }
        return true;
    }

	public boolean estColorationCorrecte() {
        Set<Integer> sommets = listeSommets();
        for(int s:sommets)
            if(!estCouleurCorrecte(s, couleurs.get(s))) return false;
        return true;
    }

	public int nbColorationsCorrectesPossibles() {
        return -1;
    }

	public int nbCouleurs() {
        HashSet<Integer> set = new HashSet<Integer>();
        Iterator<Entry<Integer, Integer>> it = couleurs.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> pair = it.next();
            if(pair.getValue() != -1) set.add(pair.getValue());
            it.remove();
        }
        return set.size();
    }
}
