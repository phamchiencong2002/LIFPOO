package modele.jeu;

import modele.plateau.*;
import java.util.ArrayList;

public class Dame extends Piece {
    public Dame(Plateau plateau, Couleur couleur) {
        super(plateau, couleur);
    }

    @Override
    public ArrayList<Case> getDeplacementsPossibles() {
        DecorateurCasesAccessibles d = new DecorateurCasesEnDiagonale(
                new DecorateurCasesEnLigne(null)
        );
        d.setContexte(plateau, this);
        return d.getCasesPossibles();
    }
}
