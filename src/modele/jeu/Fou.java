package modele.jeu;

import modele.plateau.*;
import java.util.ArrayList;

public class Fou extends Piece {
    public Fou(Plateau plateau, Couleur couleur) {
        super(plateau, couleur);
    }

    @Override
    public ArrayList<Case> getDeplacementsPossibles() {
        DecorateurCasesAccessibles d = new DecorateurCasesEnDiagonale(null);
        d.setContexte(plateau, this);
        return d.getCasesPossibles();
    }
}
