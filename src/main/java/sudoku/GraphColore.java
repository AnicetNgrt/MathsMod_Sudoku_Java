package sudoku;

import java.util.List;
import java.util.Set;

/*
Interface représentant les méthodes (=fonctions)
propre à un Graphe Coloré et que toutes les implémentations
devront implémenter.
*/

/*
Etend l'interface Graph. Une implémentation du GraphColore
devra aussi implémenter le Graph.
*/
public interface GraphColore extends Graph {
	public int[][] listeSommetsColorés();
	public void colorerSommet(int sommet, int couleur);
	public void décolorerSommet(int sommet);
	public boolean estSommetColoré(int sommet);
	public boolean estCouleurCorrecte(int sommet, int couleur);
	public Set<Integer> couleursCorrectes(int sommet);
	public boolean estColorationCorrecte();
	public int nbColorationsCorrectesPossibles();
	public int nbCouleurs();
	public void réinitialiser();
	public GraphColore dupliquer();
	public Set<Integer> couleursPossibles();
	public void ajouterCouleurPossible(int couleur);
}