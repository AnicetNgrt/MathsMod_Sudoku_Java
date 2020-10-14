import java.util.ArrayList;

// On implémente les graphes en utilisant des
// arraylist car leur complexité en accès et en
// ajout est constante
public class GraphParArray implements Graph {

    public ArrayList<Integer[]> liaisons = new ArrayList();

    // Pire cas O(n), meilleur cas O(1), cas moyen O(n)
		public sontConnectés(int sommet1, int sommet2) {
        for(int[] liaison: liaisons) {
            if(liaison[0] == sommet1 && liaison[1] == sommet2) return true;
            if(liaison[1] == sommet1 && liaison[0] == sommet2) return true;
        }
        return false;
    }

    // O(1)
		public connecterBinaire(int sommet1, int sommet2) {
        liaisons.add(new Integer[] {sommet1, sommet2});
    }

    // O(1) pour chaque sommet
		public connecterMultiple(int sommet, int[] sommets) {
        for(int s:sommets) {
            connecterBinaire(sommet, s);
        }
    }

		// O(n)
		public int[][] listeLiaisons() {
        int[][] liaisons = new int[this.liaisons.size()][2];
        for(int i = 0; i < this.liaisons.size(); i++) {
            liaisons[i][0] = this.liaisons.get(i)[0];
            liaisons[i][1] = this.liaisons.get(i)[1];
        }
        return liaisons;
    }

		public int[] listeSommets() {
        int[] sommets;
        return sommets;
    }
}