package sudoku;

/*
Interface représentant les méthodes (=fonctions)
propre à un Graphe et que toutes les implémentations
devront implémenter.

Chaque implémentation pourra ensuite adopter la
structuration interne qui lui convient pour implémenter
ces fonctionnalités.

Voir cela comme un cahier des charges
*/

public interface Graph {
	//Savoir si deux sommets sont connectés
	public boolean sontConnectés(int sommmet1, int sommet2);
	public void connecterBinaire(int sommet1, int sommet2);
	public void connecterMultiple(int sommet, int[] sommets);
	//Donner une liste des liaisons entre sommets
	public int[][] listeLiaisons();
	//Donner une liste des sommets
	public int[] listeSommets();
}