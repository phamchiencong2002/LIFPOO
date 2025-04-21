package modele.jeu;

import modele.plateau.*;
import java.util.ArrayList;

public class Cheval extends Piece {
    public Cheval(Plateau plateau, Couleur couleur) {
        super(plateau, couleur);
    }

    @Override
    public ArrayList<Case> getDeplacementsPossibles() {
        ArrayList<Case> resultat = new ArrayList<>();
        int[][] mouvements = {
                {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
                {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };

        for (int[] mvt : mouvements) {
            Case cible = plateau.getCaseRelative(getCase(), mvt[0], mvt[1]);
            if (cible != null && (!cible.aPiece() || cible.getPiece().getCouleur() != this.getCouleur())) {
                resultat.add(cible);
            }
        }

        return resultat;
    }
}
