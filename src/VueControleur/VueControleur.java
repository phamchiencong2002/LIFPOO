package VueControleur;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

import modele.jeu.Coup;
import modele.jeu.Jeu;
import modele.plateau.Case;
import modele.jeu.Piece;
import modele.jeu.Roi;
import modele.jeu.Pion;
import modele.jeu.Dame;
import modele.jeu.Fou;
import modele.jeu.Tour;
import modele.jeu.Cheval;
import modele.plateau.Plateau;

public class VueControleur extends JFrame implements Observer {
    private Plateau plateau;
    private Jeu jeu;
    private final int sizeX;
    private final int sizeY;
    private static final int pxCase = 50; // pixels par case

    private ImageIcon icoRoiB, icoRoiN;
    private ImageIcon icoPionB, icoPionN;
    private ImageIcon icoDameB, icoDameN;
    private ImageIcon icoFouB, icoFouN;
    private ImageIcon icoTourB, icoTourN;
    private ImageIcon icoChevalB, icoChevalN;

    private Case caseClic1;
    private Case caseClic2;

    private JLabel[][] tabJLabel;

    public VueControleur(Jeu _jeu) {
        jeu = _jeu;
        plateau = jeu.getPlateau();
        sizeX = plateau.SIZE_X;
        sizeY = plateau.SIZE_Y;

        chargerLesIcones();
        placerLesComposantsGraphiques();

        plateau.addObserver(this);

        mettreAJourAffichage();
    }

    private void chargerLesIcones() {
        icoRoiB = chargerIcone("Images/wK.png");
        icoRoiN = chargerIcone("Images/bK.png");
        icoPionB = chargerIcone("Images/wP.png");
        icoPionN = chargerIcone("Images/bP.png");
        icoDameB = chargerIcone("Images/wQ.png");
        icoDameN = chargerIcone("Images/bQ.png");
        icoFouB = chargerIcone("Images/wB.png");
        icoFouN = chargerIcone("Images/bB.png");
        icoTourB = chargerIcone("Images/wR.png");
        icoTourN = chargerIcone("Images/bR.png");
        icoChevalB = chargerIcone("Images/wN.png");
        icoChevalN = chargerIcone("Images/bN.png");
    }

    private ImageIcon chargerIcone(String urlIcone) {
        ImageIcon icon = new ImageIcon(urlIcone);
        Image img = icon.getImage().getScaledInstance(pxCase, pxCase, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void placerLesComposantsGraphiques() {
        setTitle("Jeu d'Échecs");
        setResizable(false);
        setSize(sizeX * pxCase, sizeY * pxCase);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComponent grilleJLabels = new JPanel(new GridLayout(sizeY, sizeX));
        tabJLabel = new JLabel[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();
                tabJLabel[x][y] = jlab;

                final int xx = x;
                final int yy = y;

                jlab.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Case clic = plateau.getCases()[xx][yy];

                        if (caseClic1 == null) {
                            if (clic.getPiece() != null) {
                                caseClic1 = clic;
                            }
                        } else {
                            caseClic2 = clic;
                            jeu.envoyerCoup(new Coup(caseClic1, caseClic2));
                            caseClic1 = null;
                            caseClic2 = null;
                        }
                    }
                });

                jlab.setOpaque(true);

                if ((x + y) % 2 == 0) {
                    jlab.setBackground(new Color(238, 238, 238)); // clair
                } else {
                    jlab.setBackground(new Color(125, 135, 150)); // foncé
                }

                grilleJLabels.add(jlab);
            }
        }
        add(grilleJLabels);
    }

    private void mettreAJourAffichage() {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                Case c = plateau.getCases()[x][y];

                if (c != null) {
                    Piece e = c.getPiece();

                    if (e != null) {
                        if (e instanceof Roi) {
                            if (y == 7) {
                                tabJLabel[x][y].setIcon(icoRoiB);
                            } else {
                                tabJLabel[x][y].setIcon(icoRoiN);
                            }
                        } else if (e instanceof Pion) {
                            if (y == 6) {
                                tabJLabel[x][y].setIcon(icoPionB);
                            } else if (y == 1) {
                                tabJLabel[x][y].setIcon(icoPionN);
                            }
                        } else if (e instanceof Dame) {
                            if (y == 7) {
                                tabJLabel[x][y].setIcon(icoDameB);
                            } else {
                                tabJLabel[x][y].setIcon(icoDameN);
                            }
                        } else if (e instanceof Fou) {
                            if (y == 7) {
                                tabJLabel[x][y].setIcon(icoFouB);
                            } else {
                                tabJLabel[x][y].setIcon(icoFouN);
                            }
                        } else if (e instanceof Tour) {
                            if (y == 7) {
                                tabJLabel[x][y].setIcon(icoTourB);
                            } else {
                                tabJLabel[x][y].setIcon(icoTourN);
                            }
                        } else if (e instanceof Cheval) {
                            if (y == 7) {
                                tabJLabel[x][y].setIcon(icoChevalB);
                            } else {
                                tabJLabel[x][y].setIcon(icoChevalN);
                            }
                        }
                    } else {
                        tabJLabel[x][y].setIcon(null);
                    }
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        mettreAJourAffichage();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mettreAJourAffichage();
            }
        });
    }
}
