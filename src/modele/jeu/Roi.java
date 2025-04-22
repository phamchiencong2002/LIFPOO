package modele.jeu;

import modele.plateau.*;
import java.awt.Point;
import java.util.ArrayList;

public class Roi extends Piece {
    private boolean aBouge = false;

    public Roi(Plateau plateau, Couleur couleur) {
        super(plateau, couleur);
    }

    public boolean aBouge() {
        return aBouge;
    }

    @Override
    public void allerSurCase(Case nouvelleCase) {
        Point from = plateau.getMap().get(getCase());
        Point to = plateau.getMap().get(nouvelleCase);

        super.allerSurCase(nouvelleCase);
        aBouge = true;

        if (from != null && to != null) {
            if (Math.abs(from.x - to.x) == 2) {
                // Petit roque
                if (to.x == 6) {
                    Case tourCase = plateau.getCases()[7][to.y];
                    if (tourCase.getPiece() instanceof Tour) {
                        Tour tour = (Tour) tourCase.getPiece();
                        if (!tour.aBouge()) {
                            Case nouvellePositionTour = plateau.getCases()[5][to.y];
                            tour.allerSurCase(nouvellePositionTour);
                        }
                    }
                }

                // Grand roque
                else if (to.x == 2) {
                    Case tourCase = plateau.getCases()[0][to.y];
                    if (tourCase.getPiece() instanceof Tour) {
                        Tour tour = (Tour) tourCase.getPiece();
                        if (!tour.aBouge()) {
                            Case nouvellePositionTour = plateau.getCases()[3][to.y];
                            tour.allerSurCase(nouvellePositionTour);
                        }
                    }
                }
            }
        }
    }

    @Override
    public ArrayList<Case> getDeplacementsPossibles() {
        ArrayList<Case> resultat = new ArrayList<>();

        int[] dx = {-1, 0, 1};
        int[] dy = {-1, 0, 1};

        for (int x : dx) {
            for (int y : dy) {
                if (x == 0 && y == 0) continue;
                Case c = plateau.getCaseRelative(getCase(), x, y);
                if (c != null && (!c.aPiece() || c.getPiece().getCouleur() != this.getCouleur())) {
                    resultat.add(c);
                }
            }
        }

        // Ajout du roque si possible
        if (!aBouge) {
            Point pos = plateau.getMap().get(getCase());
            if (pos != null) {
                int y = pos.y;

                if (roquePossible(5, 6, 7, y)) {
                    resultat.add(plateau.getCases()[6][y]);
                }

                if (roquePossible(1, 2, 3, 0, y)) {
                    resultat.add(plateau.getCases()[2][y]);
                }
            }
        }

        return resultat;
    }

    private boolean roquePossible(int x1, int x2, int tourX, int y) {
        Case case1 = plateau.getCases()[x1][y];
        Case case2 = plateau.getCases()[x2][y];
        Case tourCase = plateau.getCases()[tourX][y];

        if (case1.aPiece() || case2.aPiece()) {
            return false;
        }

        if (tourCase.getPiece() instanceof Tour) {
            Tour tour = (Tour) tourCase.getPiece();
            return tour.getCouleur() == this.getCouleur() && !tour.aBouge();
        }

        return false;
    }

    private boolean roquePossible(int x1, int x2, int x3, int tourX, int y) {
        Case case1 = plateau.getCases()[x1][y];
        Case case2 = plateau.getCases()[x2][y];
        Case case3 = plateau.getCases()[x3][y];
        Case tourCase = plateau.getCases()[tourX][y];

        if (case1.aPiece() || case2.aPiece() || case3.aPiece()) {
            return false;
        }

        if (tourCase.getPiece() instanceof Tour) {
            Tour tour = (Tour) tourCase.getPiece();
            return tour.getCouleur() == this.getCouleur() && !tour.aBouge();
        }

        return false;
    }
}
