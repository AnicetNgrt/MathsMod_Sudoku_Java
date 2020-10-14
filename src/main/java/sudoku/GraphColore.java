package sudoku;

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
	public void décolorerSommet(int sommet, int couleur);
	public boolean estCouleurCorrecte(int sommet, int couleur);
	public boolean estColorationCorrecte();
	public boolean nbColorationsCorrectesPossibles();
	public int nbCouleurs();
}