/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import java.awt.Point;
import java.util.HashMap;
import java.util.Observable;


import modele.jeu.Couleur;
import modele.jeu.Piece;
import modele.jeu.Roi;
import modele.jeu.Pion;
import modele.jeu.Tour;
import modele.jeu.Fou;
import modele.jeu.Dame;
import modele.jeu.Cheval;


import java.awt.Point;
import java.util.HashMap;
import java.util.Observable;


public class Plateau extends Observable {

    public static final int SIZE_X = 8;
    public static final int SIZE_Y = 8;


    private HashMap<Case, Point> map = new  HashMap<Case, Point>(); // permet de récupérer la position d'une case à partir de sa référence
    private Case[][] grilleCases = new Case[SIZE_X][SIZE_Y]; // permet de récupérer une case à partir de ses coordonnées

    public Plateau() {
        initPlateauVide();
    }

    public Case[][] getCases() {
        return grilleCases;
    }

    private void initPlateauVide() {

        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                grilleCases[x][y] = new Case(this);
                map.put(grilleCases[x][y], new Point(x, y));
            }

        }

    }

    public void placerPieces() {
        // Rois
        Roi roiB = new Roi(this, Couleur.BLANC);
        roiB.allerSurCase(grilleCases[4][7]);

        Roi roiN = new Roi(this, Couleur.NOIR);
        roiN.allerSurCase(grilleCases[4][0]);

        // Pions blancs
        for (int x = 0; x < 8; x++) {
            Pion pionB = new Pion(this, Couleur.BLANC);
            pionB.allerSurCase(grilleCases[x][6]);
        }

        // Pions noirs
        for (int x = 0; x < 8; x++) {
            Pion pionN = new Pion(this, Couleur.NOIR);
            pionN.allerSurCase(grilleCases[x][1]);
        }

        // Dames
        Dame dameB = new Dame(this, Couleur.BLANC);
        dameB.allerSurCase(grilleCases[3][7]);
        Dame dameN = new Dame(this, Couleur.NOIR);
        dameN.allerSurCase(grilleCases[3][0]);

        // Fous
        Fou fouB1 = new Fou(this, Couleur.BLANC);
        fouB1.allerSurCase(grilleCases[2][7]);
        Fou fouB2 = new Fou(this, Couleur.BLANC);
        fouB2.allerSurCase(grilleCases[5][7]);

        Fou fouN1 = new Fou(this, Couleur.NOIR);
        fouN1.allerSurCase(grilleCases[2][0]);
        Fou fouN2 = new Fou(this, Couleur.NOIR);
        fouN2.allerSurCase(grilleCases[5][0]);

        // Chevaux
        Cheval chevalB1 = new Cheval(this, Couleur.BLANC);
        chevalB1.allerSurCase(grilleCases[1][7]);
        Cheval chevalB2 = new Cheval(this, Couleur.BLANC);
        chevalB2.allerSurCase(grilleCases[6][7]);

        Cheval chevalN1 = new Cheval(this, Couleur.NOIR);
        chevalN1.allerSurCase(grilleCases[1][0]);
        Cheval chevalN2 = new Cheval(this, Couleur.NOIR);
        chevalN2.allerSurCase(grilleCases[6][0]);

        // Tours
        Tour tourB1 = new Tour(this, Couleur.BLANC);
        tourB1.allerSurCase(grilleCases[0][7]);
        Tour tourB2 = new Tour(this, Couleur.BLANC);
        tourB2.allerSurCase(grilleCases[7][7]);

        Tour tourN1 = new Tour(this, Couleur.NOIR);
        tourN1.allerSurCase(grilleCases[0][0]);
        Tour tourN2 = new Tour(this, Couleur.NOIR);
        tourN2.allerSurCase(grilleCases[7][0]);

        // Rafraîchir l'affichage
        setChanged();
        notifyObservers();
    }



    public void arriverCase(Case c, Piece p) {
        c.setEntite(p);
        setChanged();           // ✅ maintenant c'est ici qu'on notifie la vue
        notifyObservers();      // ✅ la vue va se mettre à jour
    }

    public void deplacerPiece(Case c1, Case c2) {
        if (c1.p != null) {
            c1.p.allerSurCase(c2);

        }
        setChanged();
        notifyObservers();

    }


    /** Indique si p est contenu dans la grille
     */
    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }
    
    private Case caseALaPosition(Point p) {
        Case retour = null;
        
        if (contenuDansGrille(p)) {
            retour = grilleCases[p.x][p.y];
        }
        return retour;
    }


}
