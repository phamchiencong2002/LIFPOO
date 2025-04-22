package modele.jeu;

import modele.plateau.Case;
import modele.plateau.Plateau;
import modele.plateau.DecorateurCasesAccessibles;

import java.awt.Point;
import java.util.ArrayList;

public abstract class Piece {
    protected Plateau plateau;
    protected Couleur couleur;
    protected Case maCase;
    protected DecorateurCasesAccessibles casesAccessibles;

    public Piece(Plateau plateau, Couleur couleur) {
        this.plateau = plateau;
        this.couleur = couleur;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public Case getCase() {
        return maCase;
    }

    public void setCase(Case c) {
        this.maCase = c;
    }

    public void allerSurCase(Case nouvelleCase) {
        if (maCase != null) maCase.quitterLaCase();
        nouvelleCase.setEntite(this);
        maCase = nouvelleCase;

        // Promotion
        if (this instanceof Pion) {
            Point coord = plateau.getMap().get(maCase);
            if (coord != null) {
                int ligneFinale = (getCouleur() == Couleur.BLANC) ? 7 : 0;
                if (coord.y == ligneFinale) {
                    System.out.println("promotion d’un pion en dame !");
                    Piece dame = new Dame(plateau, getCouleur());
                    dame.allerSurCase(maCase);
                }
            }
        }
    }

    public abstract ArrayList<Case> getDeplacementsPossibles();
}
