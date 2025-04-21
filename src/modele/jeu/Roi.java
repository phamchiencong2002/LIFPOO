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
            System.out.println("👑 Roi bouge de " + from + " à " + to);

            if (Math.abs(from.x - to.x) == 2) {
                System.out.println("tentative de roque");

                // Petit roque
                if (to.x == 6) {
                    Case tourCase = plateau.getCases()[7][to.y];
                    if (tourCase.getPiece() instanceof Tour) {
                        Tour tour = (Tour) tourCase.getPiece();
                        if (!tour.aBouge()) {
                            System.out.println("déplacement tour petit roque");
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
                            System.out.println("déplacement tour grand roque");
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

        // Ajout du roque
        if (!aBouge) {
            System.out.println("✅ Roi n’a pas bougé, on vérifie les roques...");

            Point pos = plateau.getMap().get(getCase());
            if (pos != null) {
                int y = pos.y;

                // Petit roque (vers g1/g8)
                if (roquePossible(5, 6, 7, y)) {
                    System.out.println("✅ Petit roque possible");
                    resultat.add(plateau.getCases()[6][y]);
                }

                // Grand roque (vers c1/c8)
                if (roquePossible(1, 2, 3, 0, y)) {
                    System.out.println("✅ Grand roque possible");
                    resultat.add(plateau.getCases()[2][y]);
                }
            }
        }

        // ✅ Log des déplacements du roi
        System.out.println("📦 Déplacements possibles du roi :");
        for (Case c : resultat) {
            Point pos = plateau.getMap().get(c);
            System.out.println("→ " + pos);
        }

        return resultat;
    }

    private boolean roquePossible(int x1, int x2, int tourX, int y) {
        Case case1 = plateau.getCases()[x1][y];
        Case case2 = plateau.getCases()[x2][y];
        Case tourCase = plateau.getCases()[tourX][y];

        System.out.println("🔍 Vérif petit roque : cases " + x1 + " et " + x2 + " | tour à " + tourX);

        if (case1.aPiece() || case2.aPiece()) {
            System.out.println("❌ Pièces entre le roi et la tour !");
            return false;
        }

        if (tourCase.getPiece() instanceof Tour) {
            Tour tour = (Tour) tourCase.getPiece();
            if (tour.getCouleur() == this.getCouleur() && !tour.aBouge()) {
                System.out.println("✅ Tour prête pour roque !");
                return true;
            }
        }

        System.out.println("❌ Pas de tour ou mauvaise couleur");
        return false;
    }

    private boolean roquePossible(int x1, int x2, int x3, int tourX, int y) {
        Case case1 = plateau.getCases()[x1][y];
        Case case2 = plateau.getCases()[x2][y];
        Case case3 = plateau.getCases()[x3][y];
        Case tourCase = plateau.getCases()[tourX][y];

        System.out.println("🔍 Vérif grand roque : cases " + x1 + ", " + x2 + ", " + x3 + " | tour à " + tourX);

        if (case1.aPiece() || case2.aPiece() || case3.aPiece()) {
            System.out.println("❌ Pièces entre le roi et la tour !");
            return false;
        }

        if (tourCase.getPiece() instanceof Tour) {
            Tour tour = (Tour) tourCase.getPiece();
            if (tour.getCouleur() == this.getCouleur() && !tour.aBouge()) {
                System.out.println("✅ Tour prête pour roque !");
                return true;
            }
        }

        System.out.println("❌ Pas de tour ou mauvaise couleur");
        return false;
    }
}
