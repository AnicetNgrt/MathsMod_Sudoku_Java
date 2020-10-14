/*
Interface représentant les méthodes (=fonctions)
propre à un Sudoku et que toutes les implémentations
devront implémenter.
*/

/*
Etend l'interface GraphColore. Une implémentation du Sudoku
devra aussi implémenter le GraphColore.
*/
public interface GraphSudoku extends GraphColore {
	//Résoudre le sudoku en changeant son état interne
	public void résoudre();

	//Réinitialiser et regénérer aléatoirement l'état interne du sudoku
	public void générerAléatoirement(int graine, int nbCasesRévelés, int nbChiffresRévélés);
}