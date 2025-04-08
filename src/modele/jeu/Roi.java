package modele.jeu;

import modele.plateau.*;

public class Roi extends Piece {
    public Roi(Plateau _plateau, Couleur couleur) {
        super(_plateau, couleur);
        casesAccessibles = new DecorateurCasesEnLigne(new DecorateurCasesEnDiagonale(null));
    }

    @Override
    public String toString() {
        return couleur == Couleur.BLANC ? "Roi blanc" : "Roi noir";
    }
}
