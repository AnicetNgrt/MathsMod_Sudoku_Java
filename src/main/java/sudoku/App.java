package sudoku;

import java.io.IOException;
import java.util.Scanner;

public class App {
    private static Sudoku sudoku = null;
    // Initialisation du module de génération
    private static GenerateurSudoku générateur = new GenerateurSudoku();
    // Initialisation du module de résolution
    private static SolveurSudoku[] solveurs = new SolveurSudoku[] {
        new SolveurBTENTR(),
        new SolveurBTORDERED(),
        new SolveurDancingLinks()
    };
    // Initialisation du module de saisie utilisateur
    private static Scanner scan = new Scanner(System.in);
    private static String choix = "acceuil";

    // Fonction principale / point d'entrée du programme
    public static void main(String[] args) {
        while (choix != "sortie") {
            effacerEcran();
            switch (choix) {
                case "générer":
                    génération();
                    break;
                case "acceuil":
                    acceuil();
                    break;
                case "apropos":
                    apropos();
                    break;
                case "générerContraintes":
                    générerContraintes();
                    break;
                case "revueContraintes":
                    revueContraintes();
                    break;
                case "générerChiffresDépart":
                    générerChiffresDépart();
                    break;
                case "résoudre":
                    résoudre();
                    break;
            }
        }
        effacerEcran();
    }

    public static void génération() {
        utiliserStyle("generer");

        ANSI.afficherH1("Générer un sudoku");

        int taille = -1;
        while(taille < 2) {
            ANSI.afficherQuestion("Quelle taille pour le sudoku ? (2 min)");
            ANSI.afficherPrompt();
            taille = scan.nextInt();
        }

        sudoku = new Sudoku();
        sudoku.graph = new GraphColoreParHashmap();
        sudoku.taille = taille;

        choix = "générerContraintes";
    }

    public static void générerContraintes() {
        utiliserStyle("generer");
        ANSI.afficherH1("Générer un sudoku");
        ANSI.afficherH2("Génération des contraintes");
        long debut = System.nanoTime();
        générateur.générerContraintesSudoku(sudoku);
        long temps = (System.nanoTime() - debut)/1000000l;
        System.out.println(sudoku);
        ANSI.afficherTexte("Contraintes générées en "+temps+"ms");

        ANSI.afficherQuestion("Que voulez-vous faire ?");
        ANSI.afficherChoix("1","Passer en revue les contraintes");
        ANSI.afficherChoix("2","Continuer");
        ANSI.afficherPrompt();
        switch (scan.next()) {
            case "1":
                choix = "revueContraintes";
                break;
            case "2":
                choix = "générerChiffresDépart";
                break;
        }
    }

    public static int indiceGroupe = 0;
    public static void revueContraintes() {
        utiliserStyle("generer");
        ANSI.afficherH1("Générer un sudoku");
        ANSI.afficherH2("Passage en revue des contraintes - groupe : "+(indiceGroupe+1)+"/"+générateur.groupesDeContraintes.size());
        System.out.println();
        for(int row = 0; row < sudoku.taille*sudoku.taille; row++) {
            System.out.print("      ");
            for(int col = 0; col < sudoku.taille*sudoku.taille; col++) {
                int crdEncodées = GenerateurSudoku.encoderCoordonnées(row, col);
                if(générateur.groupesDeContraintes.get(indiceGroupe).contains(crdEncodées)) {
                    ANSI.afficherEnCouleurEnLigne("[]", ANSI.YELLOW_BACKGROUND, ANSI.PASS);
                } else {
                    ANSI.afficherEnCouleurEnLigne("  ", ANSI.BLUE_BACKGROUND, ANSI.PASS);
                }
            }
            System.out.println();
        }
        indiceGroupe++;
        indiceGroupe %= générateur.groupesDeContraintes.size();

        ANSI.afficherQuestion("Que voulez-vous faire ?");
        ANSI.afficherChoix("1","Passer en revue le groupe suivant");
        ANSI.afficherChoix("2","Revenir à la génération");
        ANSI.afficherPrompt();
        switch (scan.next()) {
            case "1":
                choix = "revueContraintes";
                break;
            case "2":
                choix = "générerChiffresDépart";
                break;
        }
    }

    public static void générerChiffresDépart() {
        do {
            utiliserStyle("generer");
            ANSI.afficherH1("Générer un sudoku");
            ANSI.afficherH2("Génération des chiffres de départ");

            int nbMax = sudoku.taille*sudoku.taille*sudoku.taille*sudoku.taille;
            int nb = -1;
            while(nb < 0 || nb > nbMax) {
                ANSI.afficherQuestion("Combien de chiffres de départ faut-il générer ? ("+nbMax+" max)");
                ANSI.afficherPrompt();
                nb = scan.nextInt();
            }

            do {
                solveurs[0].modeVerbeux(false);
                générateur.générerValeursDépartSudoku(solveurs[0], sudoku, nb);
                System.out.println();
                System.out.println(sudoku);

                ANSI.afficherQuestion("Que voulez-vous faire ?");
                ANSI.afficherChoix("1", "Résoudre en entier");
                ANSI.afficherChoix("2", "Regénérer un sudoku de même taille");
                ANSI.afficherChoix("3", "Régénérer ce sudoku");
                ANSI.afficherChoix("4", "Revenir à l'acceuil");
                ANSI.afficherPrompt();
                switch (scan.next()) {
                    case "1":
                        choix = "résoudre";
                        break;
                    case "2":
                        choix = "generer";
                        break;
                    case "3":
                        choix = "regenerer";
                        break;
                    case "4":
                        choix = "acceuil";
                        break;
                }
            } while(choix == "regenerer");
        } while(choix == "generer");
    }

    public static void résoudre() {
        do {
            Sudoku sudokuAvantRésolution = sudoku.dupliquer();
            utiliserStyle("resoudre");
            ANSI.afficherH1("Résoudre un sudoku");
            ANSI.afficherH2("Sudoku de départ");
            System.out.println();
            System.out.println(sudoku);

            ANSI.afficherQuestion("Quel algorithme utiliser ?");
            ANSI.afficherChoix("1", "Résoudre à nouveau");
            ANSI.afficherChoix("2", "Revenir à l'acceuil");
            ANSI.afficherPrompt();
            
            int i = -1;
            while(i < 0 || i >= solveurs.length*2) {
                ANSI.afficherQuestion("Quelle algorithme utiliser ?");
                for(int j = 0; j < solveurs.length*2; j++) {
                    ANSI.afficherChoix(Integer.toString(j), solveurs[j/2].nom()+" "+(j%2 == 1 ? "en mode verbeux" : ""));
                }
                ANSI.afficherPrompt();
                i = scan.nextInt();
            }
            SolveurSudoku solveur = solveurs[i/2];
            if(i % 2 == 0)
                solveur.modeVerbeux(false);

            ANSI.afficherH2("Résolution: "+solveur.nom());
            long debut = System.nanoTime();
            solveur.résoudre(sudoku, sudoku.taille*sudoku.taille*sudoku.taille*sudoku.taille);
            long temps = (System.nanoTime() - debut)/1000000l;
            System.out.println(sudoku);
            boolean correct = sudoku.graph.estColorationCorrecte();
            if(correct) {
                ANSI.afficherEnCouleur("Le sudoku est correct", ANSI.GREEN_BACKGROUND, ANSI.BLACK);
            } else {
                ANSI.afficherEnCouleur("Le sudoku est incorrect", ANSI.RED_BACKGROUND, ANSI.BLACK);
            }
            System.out.println();
            ANSI.afficherTexte("Sudoku résolu en "+temps+"ms");

            ANSI.afficherQuestion("Que voulez-vous faire ?");
            ANSI.afficherChoix("1", "Résoudre à nouveau");
            ANSI.afficherChoix("2", "Revenir à l'acceuil");
            ANSI.afficherPrompt();
            switch (scan.next()) {
                case "1":
                    choix = "résoudre";
                    sudoku = sudokuAvantRésolution;
                    break;
                case "2":
                    choix = "acceuil";
                    break;
            }
        } while(choix == "résoudre");
    }

    public static void acceuil() {
        utiliserStyle("acceuil");
        ANSI.afficherH1("Bienvenue sur le générateur/solveur de Sudokus.");
        ANSI.afficherQuestion("Que voulez-vous faire ?");
        ANSI.afficherChoix("1", "Générer un sudoku");
        ANSI.afficherChoix("2", "Lire la section à-propos");
        ANSI.afficherChoix("3", "Quitter");
        ANSI.afficherPrompt();
        switch (scan.next()) {
            case "1":
                choix = "générer";
                break;
            case "2":
                choix = "apropos";
                break;
            case "3":
                choix = "sortie";
                break;
        }
    }

    public static void apropos() {
        utiliserStyle("apropos");

        ANSI.afficherH1("A propos");
        ANSI.afficherTexte("Programme de génération et de résolution de Sudokus basé sur la théorie des graphes");
        ANSI.afficherTexte("et sur le problème de la coloration complète des graphes.");
        ANSI.afficherH1("L'équipe");
        ANSI.afficherH2("IUT de Paris - 2020");
        ANSI.afficherTexte("Clémence - Anicet - Hubert - Janik");
        revenirAcceuilSiTouche();
    }

    public static void effacerEcran() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();
    }

    public static void attendreTouchePressée() {
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void revenirAcceuilSiTouche() {
        ANSI.afficherQuestion("Appuyez sur n'importe quelle touche pour revenir à l'acceuil");
        attendreTouchePressée();
        choix = "acceuil";
    }

    public static void utiliserStyle(String nom) {
        ANSI.StyleSheet style = new ANSI.StyleSheet();
        
        switch(nom) {
            case "generer":
                style.couleurFondH1 = ANSI.YELLOW_BACKGROUND;
                style.couleurPoliceH1 = ANSI.BLACK;
                style.couleurFondH2 = ANSI.GREEN_BACKGROUND;
                style.couleurPoliceH2 = ANSI.BLACK;
                style.couleurPoliceQuestion = ANSI.BLUE;
                style.couleurPolicePrompt = ANSI.YELLOW;
                break;
            case "resoudre":
                style.couleurFondH1 = ANSI.CYAN_BACKGROUND;
                style.couleurPoliceH1 = ANSI.BLACK;
                style.couleurFondH2 = ANSI.WHITE_BACKGROUND;
                style.couleurPoliceH2 = ANSI.BLACK;
                style.couleurPoliceQuestion = ANSI.BLUE;
                style.couleurPolicePrompt = ANSI.YELLOW;
                break;
            case "acceuil":
                style.couleurFondH1 = ANSI.BLUE_BACKGROUND;
                style.couleurPoliceH1 = ANSI.BLACK;
                style.couleurPoliceQuestion = ANSI.BLUE;
                style.couleurPolicePrompt = ANSI.YELLOW;
                break;
            case "apropos":
                style.couleurFondH1 = ANSI.PURPLE_BACKGROUND;
                style.couleurPoliceH1 = ANSI.BLACK;
                style.couleurPoliceH2 = ANSI.RED;
                style.couleurPoliceQuestion = ANSI.BLUE;
                style.couleurPolicePrompt = ANSI.YELLOW;
                break;
        }

        ANSI.chargerStyle(style);
    }
}
