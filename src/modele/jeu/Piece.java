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
        if (maCase != null) {
            maCase.quitterLaCase(); //on quitte la case
        }

        nouvelleCase.setEntite(this); //on atterit sur une nouvelle case
        maCase = nouvelleCase;

        //promotion
        if (this instanceof Pion) {
            Point coord = plateau.getMap().get(maCase);
            if (coord != null) {
                int ligneFinale = (getCouleur() == Couleur.BLANC) ? 7 : 0;
                if (coord.y == ligneFinale) {
                    // 💫 Promotion en Dame
                    System.out.println("promotion d’un pion en dame !");
                    Piece dame = new Dame(plateau, getCouleur());
                    dame.allerSurCase(maCase); //remplacer par une dame
                }
            }
        }
    }


    // ⚠️ obligatoire pour que @Override fonctionne dans les classes filles
    public abstract ArrayList<Case> getDeplacementsPossibles();
}
