package modele.jeu;

import modele.plateau.*;
import javax.swing.JOptionPane;

import java.util.ArrayList;

public class Jeu {
    private Couleur joueurCourant;
    private Plateau plateau;

    public Jeu() {
        plateau = new Plateau();
        joueurCourant = Couleur.BLANC;
        plateau.placerPieces();
    }

    public Couleur getJoueurCourant() {
        return joueurCourant;
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public void envoyerCoup(Coup coup) {
        Case depart = coup.getCaseDepart();
        Case arrivee = coup.getCaseArrivee();

        if (depart == null || arrivee == null) return;

        Piece piece = depart.getPiece();
        if (piece == null || piece.getCouleur() != joueurCourant) return;

        ArrayList<Case> deplacements = piece.getDeplacementsPossibles();
        if (!deplacements.contains(arrivee)) {
            System.out.println("Coup illégal !");
            return;
        }


        //on commence la simulation
        Piece ancienneCible = arrivee.getPiece();
        depart.quitterLaCase();
        arrivee.setEntite(piece);
        piece.setCase(arrivee);

        boolean enEchec = estEnEchec(joueurCourant);

        //on annule simulation
        arrivee.setEntite(ancienneCible);
        depart.setEntite(piece);
        piece.setCase(depart);

        if (enEchec) {
            System.out.println("\u26d4 Coup refusé : roi en échec !");
            return;
        }

        //appliquer
        plateau.deplacerPiece(depart, arrivee);
        changerTour();

        if (estEnEchec(joueurCourant)) {
            System.out.println("\u26a0 Roi " + joueurCourant + " est en échec !");
        }


        if (estEnEchec(joueurCourant)) {
            System.out.println("⚠️ Roi " + joueurCourant + " est en échec !");
            if (estEchecEtMat(joueurCourant)) {
                System.out.println("♚ ÉCHEC ET MAT ! Le joueur " + joueurCourant + " a perdu !");
                JOptionPane.showMessageDialog(null, "♚ Échec et mat ! Le joueur " + joueurCourant + " a perdu !");
                // 👉 Option : System.exit(0); pour quitter le jeu
            }
        }

    }

    private void changerTour() {
        joueurCourant = (joueurCourant == Couleur.BLANC) ? Couleur.NOIR : Couleur.BLANC;
    }

    public boolean estEnEchec(Couleur joueur) {
        Case caseRoi = trouverCaseRoi(joueur);
        if (caseRoi == null) return false;

        for (int x = 0; x < plateau.SIZE_X; x++) {
            for (int y = 0; y < plateau.SIZE_Y; y++) {
                Case c = plateau.getCases()[x][y];
                Piece p = c.getPiece();

                if (p != null && p.getCouleur() != joueur) {
                    ArrayList<Case> deplacements = p.getDeplacementsPossibles();
                    if (deplacements.contains(caseRoi)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    public boolean estEchecEtMat(Couleur joueur) {
        if (!estEnEchec(joueur)) return false;

        // Parcourir toutes les pièces du joueur
        for (int x = 0; x < plateau.SIZE_X; x++) {
            for (int y = 0; y < plateau.SIZE_Y; y++) {
                Case depart = plateau.getCases()[x][y];
                Piece piece = depart.getPiece();

                if (piece != null && piece.getCouleur() == joueur) {
                    ArrayList<Case> deplacements = piece.getDeplacementsPossibles();

                    for (Case arrivee : deplacements) {
                        // Simuler le coup
                        Piece cible = arrivee.getPiece();
                        depart.quitterLaCase();
                        arrivee.setEntite(piece);
                        piece.setCase(arrivee);

                        boolean encoreEnEchec = estEnEchec(joueur);

                        // Annuler le coup
                        arrivee.setEntite(cible);
                        depart.setEntite(piece);
                        piece.setCase(depart);

                        if (!encoreEnEchec) {
                            return false; //le joueur peut encore s’en sortir
                        }
                    }
                }
            }
        }

        return true; //aucun coup ne sauve le roi,
    }


    private Case trouverCaseRoi(Couleur joueur) {
        for (int x = 0; x < plateau.SIZE_X; x++) {
            for (int y = 0; y < plateau.SIZE_Y; y++) {
                Case c = plateau.getCases()[x][y];
                Piece p = c.getPiece();
                if (p instanceof Roi && p.getCouleur() == joueur) {
                    return c;
                }
            }
        }
        return null;
    }
}
