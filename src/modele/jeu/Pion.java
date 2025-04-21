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
        int sens = (getCouleur() == Couleur.BLANC) ? 1 : -1; //on avance vers y+1

        Case current = getCase();
        if (current == null) return resultat;

        // Case devant
        Case front = plateau.getCaseRelative(current, 0, sens);
        if (front != null && !front.aPiece()) {
            resultat.add(front);

            Point coord = plateau.getMap().get(current);
            int ligneDepart = (getCouleur() == Couleur.BLANC) ? 1 : 6;
            if (coord != null && coord.y == ligneDepart) {
                Case doubleFront = plateau.getCaseRelative(current, 0, 2 * sens);
                if (doubleFront != null && !doubleFront.aPiece()) {
                    resultat.add(doubleFront);
                }
            }
        }

        //on fait la prise diadonale
        int[][] diagonales = { {1, sens}, {-1, sens} };
        for (int[] d : diagonales) {
            Case cible = plateau.getCaseRelative(current, d[0], d[1]);
            if (cible != null && cible.aPiece() &&
                    cible.getPiece().getCouleur() != this.getCouleur()) {
                resultat.add(cible);
            }
        }

        return resultat;
    }
}
