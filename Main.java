/*
La classe Main est le point d'entrée du programme.
Le programme exécute en premier sa méthode (terme
désignant une fonction appartenant à une class) main.
*/
class Main {
	/*
	Lancement des tests unitaires pour s'assurer
	automatiquement à chaque lancement que les algos
	donnent des résultats corrects avec certains jeux
	de données.
	Voir ./tests/
	*/
  public static void main(String[] args) {
    TestRunner tr = new TestRunner();
    tr.main();
  }
}