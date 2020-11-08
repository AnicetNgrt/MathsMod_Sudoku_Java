package sudoku;

/**
 * Classe servant à l'affichage dans le terminal avec des couleurs
 */
public class ANSI {
    // code récupéré: https://stackoverflow.com/a/5762502
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BLACK_BACKGROUND = "\u001B[40m";
    public static final String RED_BACKGROUND = "\u001B[41m";
    public static final String GREEN_BACKGROUND = "\u001B[42m";
    public static final String YELLOW_BACKGROUND = "\u001B[43m";
    public static final String BLUE_BACKGROUND = "\u001B[44m";
    public static final String PURPLE_BACKGROUND = "\u001B[45m";
    public static final String CYAN_BACKGROUND = "\u001B[46m";
    public static final String WHITE_BACKGROUND = "\u001B[47m";
    public static final String RESET = "\u001B[0m";
    // fin code récupéré

    public static final String PASS = "";
    
    public static StyleSheet styleParDefaut = new StyleSheet();
    public static StyleSheet styleCourant = styleParDefaut;

    public static void chargerStyle(StyleSheet style) {
        styleCourant = style;
    }

    public static void afficherEnCouleur(Object o, String couleurFond, String couleurPolice) {
        System.out.println(RESET+couleurPolice+couleurFond+o.toString()+RESET);
    }

    public static void afficherEnCouleurEnLigne(Object o, String couleurFond, String couleurPolice) {
        System.out.print(RESET+couleurPolice+couleurFond+o.toString()+RESET);
    }

    public static void afficherH1(String s) {
        System.out.println();
        afficherEnCouleur("  "+s, styleCourant.couleurFondH1, styleCourant.couleurPoliceH1);
    }

    public static void afficherH2(String s) {
        System.out.println();
        afficherEnCouleur("    "+s, styleCourant.couleurFondH2, styleCourant.couleurPoliceH2);
    }

    public static void afficherTexte(String s) {
        afficherEnCouleur("      "+s, styleCourant.couleurFondTexte, styleCourant.couleurPoliceTexte);
    }

    public static void afficherQuestion(String s) {
        System.out.println();
        afficherEnCouleur("    "+s, styleCourant.couleurFondQuestion, styleCourant.couleurPoliceQuestion);
    }

    public static void afficherChoix(String indication, String label) {
        afficherEnCouleur("      "+styleCourant.texteIndicGauche+indication+styleCourant.texteIndicDroit+label, styleCourant.couleurFondChoix, styleCourant.couleurPoliceChoix);
    }

    public static void afficherPrompt() {
        System.out.println();
        afficherEnCouleurEnLigne("    "+styleCourant.textePrompt, styleCourant.couleurFondPrompt, styleCourant.couleurPolicePrompt);
    }

    public static class StyleSheet {
        public String couleurPoliceH1 = WHITE;
        public String couleurFondH1 = PASS;
        public String couleurPoliceH2 = WHITE;
        public String couleurFondH2 = PASS;
        public String couleurPoliceTexte = WHITE;
        public String couleurFondTexte = PASS;
        public String couleurPoliceQuestion = WHITE;
        public String couleurFondQuestion = PASS;
        public String couleurPoliceChoix = WHITE;
        public String couleurFondChoix = PASS;
        public String couleurPolicePrompt = WHITE;
        public String couleurFondPrompt = PASS;
        public String textePrompt = ">>> ";
        public String texteIndicGauche = "[";
        public String texteIndicDroit = "] ";
    }
}
