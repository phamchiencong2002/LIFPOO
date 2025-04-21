package modele.jeu;

import modele.plateau.*;

import java.awt.Point;
import java.util.ArrayList;

public class Tour extends Piece {
    private boolean aBouge = false;

    public Tour(Plateau plateau, Couleur couleur) {
        super(plateau, couleur);
    }

    public boolean aBouge() {
        return aBouge;
    }

    @Override
    public void allerSurCase(Case nouvelleCase) {
        super.allerSurCase(nouvelleCase);
        aBouge = true;
    }

    @Override
    public ArrayList<Case> getDeplacementsPossibles() {
        ArrayList<Case> resultat = new ArrayList<>();
        Point pos = plateau.getMap().get(getCase());

        if (pos == null) return resultat;

        // directions : haut, bas, gauche, droite
        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}
        };

        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];
            int x = pos.x + dx;
            int y = pos.y + dy;

            while (x >= 0 && x < Plateau.SIZE_X && y >= 0 && y < Plateau.SIZE_Y) {
                Case cible = plateau.getCases()[x][y];

                if (!cible.aPiece()) {
                    resultat.add(cible);
                } else {
                    if (cible.getPiece().getCouleur() != this.getCouleur()) {
                        resultat.add(cible); // capture possible
                    }
                    break; // stop après collision
                }

                x += dx;
                y += dy;
            }
        }

        return resultat;
    }
}
