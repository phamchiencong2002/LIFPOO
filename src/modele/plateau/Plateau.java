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

public class Plateau extends Observable {

    public static final int SIZE_X = 8;
    public static final int SIZE_Y = 8;

    private HashMap<Case, Point> map = new HashMap<>(); // permet de récupérer la position d'une case à partir de sa référence
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
        System.out.println("🔧 Placement des pièces :");

        //pieces blanches
        new Tour(this, Couleur.BLANC).allerSurCase(grilleCases[0][0]);
        new Cheval(this, Couleur.BLANC).allerSurCase(grilleCases[1][0]);
        new Fou(this, Couleur.BLANC).allerSurCase(grilleCases[2][0]);
        new Roi(this, Couleur.BLANC).allerSurCase(grilleCases[3][0]);
        new Dame(this, Couleur.BLANC).allerSurCase(grilleCases[4][0]);
        new Fou(this, Couleur.BLANC).allerSurCase(grilleCases[5][0]);
        new Cheval(this, Couleur.BLANC).allerSurCase(grilleCases[6][0]);
        new Tour(this, Couleur.BLANC).allerSurCase(grilleCases[7][0]);

        //pions blancs
        for (int x = 0; x < 8; x++) {
            new Pion(this, Couleur.BLANC).allerSurCase(grilleCases[x][1]);
        }

        //pions noirs
        for (int x = 0; x < 8; x++) {
            new Pion(this, Couleur.NOIR).allerSurCase(grilleCases[x][6]);
        }

        //pieces en noirs
        new Tour(this, Couleur.NOIR).allerSurCase(grilleCases[0][7]);
        new Cheval(this, Couleur.NOIR).allerSurCase(grilleCases[1][7]);
        new Fou(this, Couleur.NOIR).allerSurCase(grilleCases[2][7]);
        new Roi(this, Couleur.NOIR).allerSurCase(grilleCases[3][7]);
        new Dame(this, Couleur.NOIR).allerSurCase(grilleCases[4][7]);
        new Fou(this, Couleur.NOIR).allerSurCase(grilleCases[5][7]);
        new Cheval(this, Couleur.NOIR).allerSurCase(grilleCases[6][7]);
        new Tour(this, Couleur.NOIR).allerSurCase(grilleCases[7][7]);

        setChanged();
        notifyObservers();

        System.out.println("📦 Grille des pièces :");
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                Case c = grilleCases[x][y];
                Piece p = c.getPiece();
                if (p != null) {
                    System.out.println("🔹 " + p.getClass().getSimpleName() + " (" + p.getCouleur() + ") en [" + x + "," + y + "]");
                }
            }
        }
    }



    public void arriverCase(Case c, Piece p) {
        c.setEntite(p);
        setChanged();
        notifyObservers();
    }

    public void deplacerPiece(Case c1, Case c2) {
        Piece piece = c1.getPiece();
        if (piece != null) {
            piece.allerSurCase(c2);
        }
        setChanged();
        notifyObservers();
    }

    public HashMap<Case, Point> getMap() {
        return map;
    }

    public Case getCaseRelative(Case c, int dx, int dy) {
        Point pos = map.get(c);
        if (pos == null) return null;

        int newX = pos.x + dx;
        int newY = pos.y + dy;

        if (newX >= 0 && newX < SIZE_X && newY >= 0 && newY < SIZE_Y) {
            return grilleCases[newX][newY];
        }
        return null;
    }

    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }

    private Case caseALaPosition(Point p) {
        if (contenuDansGrille(p)) {
            return grilleCases[p.x][p.y];
        }
        return null;
    }
}
