package modele.jeu;

import modele.plateau.*;
import java.util.ArrayList;
import java.awt.Point;

public class Pion extends Piece {

    public Pion(Plateau plateau, Couleur couleur) {
        super(plateau, couleur);
    }

    @Override
    public ArrayList<Case> getDeplacementsPossibles() {
        ArrayList<Case> resultat = new ArrayList<>();
        int sens = (getCouleur() == Couleur.BLANC) ? 1 : -1;
        Case current = getCase();
        if (current == null) return resultat;

        Point pos = plateau.getMap().get(current);
        if (pos == null) return resultat;

        // Avance normale
        Case front = plateau.getCaseRelative(current, 0, sens);
        if (front != null && !front.aPiece()) {
            resultat.add(front);

            // Double depuis ligne de départ
            int ligneDepart = (getCouleur() == Couleur.BLANC) ? 1 : 6;
            if (pos.y == ligneDepart) {
                Case doubleFront = plateau.getCaseRelative(current, 0, 2 * sens);
                if (doubleFront != null && !doubleFront.aPiece()) {
                    resultat.add(doubleFront);
                }
            }
        }

        // Prise diagonale
        for (int dx : new int[]{-1, 1}) {
            Case diag = plateau.getCaseRelative(current, dx, sens);
            if (diag != null && diag.aPiece() && diag.getPiece().getCouleur() != this.getCouleur()) {
                resultat.add(diag);
            }
        }

        return resultat;
    }
}
