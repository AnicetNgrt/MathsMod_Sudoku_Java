package sudoku;

import java.util.ArrayList;

import javax.swing.plaf.synth.SynthStyleFactory;

// Code traduit de l'anglais et du C++ et adapté par l'équipe
// Code original: http://mathieuturcotte.ca/textes/sudoku-dancing-links/
public class SolveurDancingLinks implements SolveurSudoku {

    private boolean modeVerbeux = false;

    public class Branche {
        Branche haut;
        Branche bas;
        Branche gauche;
        Branche droite;
        Branche entete;
        int numBranche;
        int couleur;
        int tailleCol;
        int indexCol;
    }

    public void résoudre(Sudoku sudoku, int nb) {
        Branche racine = construireMatriceCouverture(sudoku);
        résoudreCouverture(sudoku, racine, nb);
    }

    private Branche construireMatriceCouverture(Sudoku s) {
        int taille2 = s.taille*s.taille;
        int nbBranches = taille2*taille2;
        int nbColonnes = nbBranches*4;
        ArrayList<Branche> entetesColonnes = new ArrayList<Branche>();

        Branche racine = new Branche();
        racine.bas = racine;
        racine.haut = racine;
        racine.gauche = racine;
        racine.droite = racine;
        Branche prédécesseur = racine;

        for(int i = 0; i < nbColonnes; i++) {
            Branche entete = new Branche();
            entete.haut = entete.bas = entete;
            entete.gauche = prédécesseur;
            entete.droite = racine;
            entete.indexCol = i;
            entete.tailleCol = 0;

            racine.gauche = entete;
            prédécesseur.droite = entete;

            entetesColonnes.add(entete);
            prédécesseur = entete;
        }

        for(int row = 0; row < taille2; row++) {
            for(int col = 0; col < taille2; col++) {
                int numBranche = GenerateurSudoku.encoderCoordonnées(row, col);
                int début = 0;
                int fin = taille2;

                if(s.graph.estSommetColoré(numBranche)) {
                    début = s.graph.couleurSommet(numBranche);
                    fin = début+1;
                }

                for(int couleur = début; couleur < fin; couleur++) {
                    int[] posCol = new int[4];
                    posCol[0] = row * taille2 + col;
                    posCol[1] = nbBranches + row * taille2 + couleur;
                    posCol[2] = (nbBranches * 2) + row * taille2 + couleur;
                    int region = (row/s.taille + col/s.taille * s.taille);
                    posCol[3] = (nbBranches * 3) + region * taille2 + couleur;

                    Branche entete = entetesColonnes.get(posCol[0]);
                    Branche premier = new Branche();

                    premier.numBranche = numBranche;
                    premier.couleur = couleur;
                    premier.entete = entete;
                    premier.haut = entete.haut;
                    premier.bas = entete;
                    premier.gauche = premier;
                    premier.droite = premier;

                    premier.haut.bas = premier;
                    entete.haut = premier;
                    entete.tailleCol++;

                    Branche précédente = premier;
                    for(int i = 1; i < 4; i++) {
                        entete = entetesColonnes.get(posCol[i]);
                        Branche actuel = new Branche();

                        actuel.numBranche = numBranche;
                        actuel.couleur = couleur;
                        actuel.entete = entete;
                        actuel.haut = entete.haut;
                        actuel.bas = entete;
                        actuel.gauche = précédente;
                        actuel.droite = premier;

                        actuel.haut.bas = actuel;
                        actuel.gauche.droite = actuel;
                        entete.haut = actuel;
                        premier.gauche = actuel;
                        entete.tailleCol++;

                        précédente = actuel;
                    }
                }
            }
        }
        return racine;
    }

    private boolean résoudreCouverture(Sudoku s, Branche racine, int nb) {
        boolean résolu = false;
        Branche entete = choisirProchaineColonne(racine);

        if(entete == racine){
            return true;
        }

        couvrirColonne(entete);

        Branche elementCol = entete.bas;
        while(elementCol != entete) {

            Branche elementRow = elementCol.droite;
            while(elementRow != elementCol) {
                couvrirColonne(elementRow.entete);
                elementRow = elementRow.droite;
            }

            résolu = résoudreCouverture(s, racine, nb);

            elementRow = elementCol.gauche;
            while(elementRow != elementCol) {
                découvrirColonne(elementRow.entete);
                elementRow = elementRow.gauche;
            }

            if(résolu) {
                int couleur = elementCol.couleur;
                if(modeVerbeux) {
                    if(!s.graph.estCouleurCorrecte(elementCol.numBranche, couleur)) {
                        System.out.println("Valeur pour "+elementCol.numBranche+" éronnée!");
                    }
                }
                s.graph.colorerSommet(elementCol.numBranche, couleur);
                break;
            }

            elementCol = elementCol.bas;
        }

        découvrirColonne(entete);
        return résolu;
    }

    private Branche choisirProchaineColonne(Branche racine) {
        int plusPetitNbBranche = Integer.MAX_VALUE;

        Branche enteteActuelle = racine.droite;
        Branche enteteSuivante = enteteActuelle;

        while(enteteActuelle != racine) {
            int nbBranche = enteteActuelle.tailleCol;
            if(nbBranche < plusPetitNbBranche) {
                plusPetitNbBranche = nbBranche;
                enteteSuivante = enteteActuelle;
            }

            enteteActuelle = enteteActuelle.droite;
        }
        return enteteSuivante;
    }

    private void couvrirColonne(Branche entete) {
        /*System.out.println("---");
        System.out.println(entete);
        System.out.println(entete.gauche);
        System.out.println(entete.droite);
        System.out.println(entete.bas);
        System.out.println(entete.haut);
        System.out.println(entete.entete);*/
        entete.gauche.droite = entete.droite;
        entete.droite.gauche = entete.gauche;

        Branche elementCol = entete.bas;
        while(elementCol != entete) {
            Branche elementRow = elementCol.droite;
            while(elementRow != elementCol) {
                elementRow.haut.bas = elementRow.bas;
                elementRow.bas.haut = elementRow.haut;
                elementRow.entete.tailleCol--;
                elementRow = elementRow.droite;
            }
            elementCol = elementCol.bas;
        }
    }

    private void découvrirColonne(Branche entete) {
        Branche elementCol = entete.haut;
        while(elementCol != entete) {
            Branche elementRow = elementCol.gauche;
            while(elementRow != elementCol) {
                elementRow.haut.bas = elementRow;
                elementRow.bas.haut = elementRow;
                elementRow.entete.tailleCol++;
                elementRow = elementRow.gauche;
            }
            elementCol = elementCol.haut;
        }
        entete.gauche.droite = entete;
        entete.droite.gauche = entete;
    }

    public void modeVerbeux(boolean estActif) {
        modeVerbeux = estActif;
    }

    public String nom() {
        return "Dancing Links";
    }
}
