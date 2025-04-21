package modele.plateau;

import java.util.ArrayList;

public class DecorateurCasesEnLigne extends DecorateurCasesAccessibles {
    public DecorateurCasesEnLigne(DecorateurCasesAccessibles base) {
        super(base);
    }

    @Override
    protected ArrayList<Case> getMesCasesAccessibles() {
        ArrayList<Case> resultat = new ArrayList<>();
        int[][] directions = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };

        for (int[] dir : directions) {
            int dx = dir[0], dy = dir[1];
            Case cible = plateau.getCaseRelative(piece.getCase(), dx, dy);
            while (cible != null) {
                if (!cible.aPiece()) {
                    resultat.add(cible);
                } else {
                    if (cible.getPiece().getCouleur() != piece.getCouleur()) {
                        resultat.add(cible);
                    }
                    break;
                }
                dx += dir[0];
                dy += dir[1];
                cible = plateau.getCaseRelative(piece.getCase(), dx, dy);
            }
        }

        return resultat;
    }
}
