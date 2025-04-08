package modele.jeu;

import modele.plateau.*;

public abstract class Piece {
    protected Plateau plateau;
    protected Couleur couleur;
    protected Case maCase;
    protected DecorateurCasesAccessibles casesAccessibles;

    public Piece(Plateau p, Couleur couleur) {
        plateau = p;
        this.couleur = couleur;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public Case getCase() {
        return maCase;
    }

    public void allerSurCase(Case nouvelleCase) {
        if (maCase != null) {
            maCase.quitterLaCase();     // <-- Important : quitter l'ancienne case proprement
        }
        nouvelleCase.setEntite(this);    // <-- Correct : setEntite (pas setPiece)
        maCase = nouvelleCase;
    }
}
