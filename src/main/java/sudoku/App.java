package sudoku;

import java.io.IOException;
import java.util.Scanner;

public class App {
    // Initialisation du module de génération
    private static GenerateurSudoku générateur = new GenerateurSudoku();
    private static Sudoku sudoku = null;
    // Initialisation du module de saisie utilisateur
    private static Scanner scan = new Scanner(System.in);
    private static String choix = "acceuil";

    // Fonction principale / point d'entrée du programme
    public static void main(String[] args) {
        while (choix != "sortie") {
            effacerEcran();
            if(sudoku != null && sudoku.graph != null)
                System.out.println(sudoku.graph.listeSommets().size());
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

        générateur.générerValeursDépartSudoku(sudoku, nb);
        System.out.println(sudoku);

        attendreTouchePressée();
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
                style.couleurPoliceQuestion = ANSI.BLUE;
                style.couleurPolicePrompt = ANSI.YELLOW;
                break;
            case "acceuil":
                style.couleurFondH1 = ANSI.BLUE_BACKGROUND;
                style.couleurPoliceQuestion = ANSI.BLUE;
                style.couleurPolicePrompt = ANSI.YELLOW;
                break;
            case "apropos":
                style.couleurFondH1 = ANSI.PURPLE_BACKGROUND;
                style.couleurPoliceH2 = ANSI.RED;
                style.couleurPoliceQuestion = ANSI.BLUE;
                style.couleurPolicePrompt = ANSI.YELLOW;
                break;
        }

        ANSI.chargerStyle(style);
    }
}
