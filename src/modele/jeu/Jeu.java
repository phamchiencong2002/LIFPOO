package modele.jeu;

import modele.plateau.*;
import javax.swing.JOptionPane;
import java.util.*;
import java.awt.Point;
import java.io.*;

public class Jeu {
    private Couleur joueurCourant;
    private Plateau plateau;
    private List<String> historiqueCoups = new ArrayList<>();
    private boolean debutHistoriqueAffiche = false;

    public Jeu() {
        plateau = new Plateau(this);
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
            System.out.println("Coup interdit.");
            return;
        }

        Piece ancienneCible = arrivee.getPiece();
        depart.quitterLaCase();
        arrivee.setEntite(piece);
        piece.setCase(arrivee);
        boolean enEchec = estEnEchec(joueurCourant);
        arrivee.setEntite(ancienneCible);
        depart.setEntite(piece);
        piece.setCase(depart);

        if (enEchec) {
            System.out.println("Coup refusé : roi en échec !");
            return;
        }

        plateau.deplacerPiece(depart, arrivee);

        String notation = genererNotationEchiquenne(piece, depart, arrivee, ancienneCible != null);
        historiqueCoups.add(notation);
        afficherHistoriqueConsole();
        changerTour();

        if (estEnEchec(joueurCourant)) {
            System.out.println("Roi " + joueurCourant + " est en échec !");
            if (estEchecEtMat(joueurCourant)) {
                System.out.println("Échec et mat ! Le joueur " + joueurCourant + " a perdu !");
                JOptionPane.showMessageDialog(null, "Échec et mat ! Le joueur " + joueurCourant + " a perdu !");
                sauvegarderPartiePGN();
            }
        }
    }

    private void changerTour() {
        joueurCourant = (joueurCourant == Couleur.BLANC) ? Couleur.NOIR : Couleur.BLANC;
    }

    private void afficherHistoriqueConsole() {
        if (!debutHistoriqueAffiche) {
            System.out.println("===== Historique des coups =====");
            debutHistoriqueAffiche = true;
        }

        int index = historiqueCoups.size() - 1;

        if (index % 2 == 0) {
            // Coup blanc → début d’une nouvelle ligne
            System.out.printf("%d. %-8s", (index / 2 + 1), historiqueCoups.get(index));
        } else {
            // Coup noir → complète la ligne
            System.out.printf("   %-8s\n", historiqueCoups.get(index));
        }
    }


    private String genererNotationEchiquenne(Piece piece, Case depart, Case arrivee, boolean prise) {
        String nomPiece = getLettrePiece(piece);
        String arriveeStr = caseVersNotation(arrivee);
        String sep = prise ? "x" : "";


        if (piece instanceof Pion && prise) {
            char col = (char) ('a' + plateau.getMap().get(depart).x);
            return col + "x" + arriveeStr;
        }

        return nomPiece + sep + arriveeStr;
    }

    private String getLettrePiece(Piece piece) {
        if (piece instanceof Pion) return "";
        if (piece instanceof Tour) return "R";
        if (piece instanceof Cheval) return "N";
        if (piece instanceof Fou) return "B";
        if (piece instanceof Dame) return "Q";
        if (piece instanceof Roi) return "K";
        return "?";
    }

    private String caseVersNotation(Case c) {
        Point p = plateau.getMap().get(c);
        char file = (char) ('a' + p.x);
        int rang = p.y + 1;
        return "" + file + rang;
    }

    public boolean estEnEchec(Couleur joueur) {
        Case caseRoi = trouverCaseRoi(joueur);
        if (caseRoi == null) return false;

        for (int x = 0; x < Plateau.SIZE_X; x++) {
            for (int y = 0; y < Plateau.SIZE_Y; y++) {
                Case c = plateau.getCases()[x][y];
                Piece p = c.getPiece();
                if (p != null && p.getCouleur() != joueur) {
                    if (p.getDeplacementsPossibles().contains(caseRoi)) return true;
                }
            }
        }
        return false;
    }

    public boolean estEchecEtMat(Couleur joueur) {
        if (!estEnEchec(joueur)) return false;

        for (int x = 0; x < Plateau.SIZE_X; x++) {
            for (int y = 0; y < Plateau.SIZE_Y; y++) {
                Case depart = plateau.getCases()[x][y];
                Piece piece = depart.getPiece();
                if (piece != null && piece.getCouleur() == joueur) {
                    for (Case arrivee : piece.getDeplacementsPossibles()) {
                        Piece cible = arrivee.getPiece();
                        depart.quitterLaCase();
                        arrivee.setEntite(piece);
                        piece.setCase(arrivee);

                        boolean encoreEnEchec = estEnEchec(joueur);

                        arrivee.setEntite(cible);
                        depart.setEntite(piece);
                        piece.setCase(depart);

                        if (!encoreEnEchec) return false;
                    }
                }
            }
        }
        return true;
    }

    private Case trouverCaseRoi(Couleur joueur) {
        for (int x = 0; x < Plateau.SIZE_X; x++) {
            for (int y = 0; y < Plateau.SIZE_Y; y++) {
                Case c = plateau.getCases()[x][y];
                Piece p = c.getPiece();
                if (p instanceof Roi && p.getCouleur() == joueur) {
                    return c;
                }
            }
        }
        return null;
    }

    private void sauvegarderPartiePGN() {
        try {
            File dossier = new File("parties_pgn");
            if (!dossier.exists()) dossier.mkdir();

            int i = 1;
            File fichier;
            do {
                fichier = new File(dossier, "partie" + i + ".pgn");
                i++;
            } while (fichier.exists());

            PrintWriter pw = new PrintWriter(fichier);
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < historiqueCoups.size(); j++) {
                if (j % 2 == 0) sb.append((j / 2 + 1) + ". ");
                sb.append(historiqueCoups.get(j)).append(" ");
            }
            pw.println(sb.toString().trim());
            pw.close();
            System.out.println("partie sauvegardée : " + fichier.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
