package modele.plateau;

import modele.jeu.Piece;
import java.util.ArrayList;

public abstract class DecorateurCasesAccessibles {
    protected Plateau plateau;
    protected Piece piece;
    private DecorateurCasesAccessibles base;

    public DecorateurCasesAccessibles(DecorateurCasesAccessibles baseDecorateur) {
        this.base = baseDecorateur;
    }

    public void setContexte(Plateau plateau, Piece piece) {
        this.plateau = plateau;
        this.piece = piece;
    }

    public ArrayList<Case> getCasesPossibles() {
        ArrayList<Case> retour = new ArrayList<>();
        if (base != null) {
            base.setContexte(plateau, piece);
            retour.addAll(base.getCasesPossibles());
        }
        retour.addAll(getMesCasesAccessibles());
        return retour ;
    }

    protected abstract ArrayList<Case> getMesCasesAccessibles();
}
