    package VueControleur;

    import java.awt.*;
    import java.awt.event.MouseAdapter;
    import java.awt.event.MouseEvent;
    import java.util.ArrayList;
    import javax.swing.*;
    import java.util.*; // ou bien
    import modele.plateau.*;
    import VueControleur.VueControleur;



    import modele.jeu.*;
    import modele.plateau.*;

    public class VueControleur extends JFrame implements Observer {
        private static VueControleur instance;

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
        private ArrayList<Case> casesSurbrillance = new ArrayList<>();
        private Color couleurSurbrillance = new Color(236, 198, 151  ); // bleu clair transparent
        private Color couleurClair = new Color(238, 238, 238);
        private Color couleurFonce = new Color(125, 135, 150);


        public VueControleur(Jeu _jeu) {
            instance = this;
            jeu = _jeu;
            plateau = jeu.getPlateau();
            sizeX = Plateau.SIZE_X;
            sizeY = Plateau.SIZE_Y;

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
                                if (clic.getPiece() != null && clic.getPiece().getCouleur() == jeu.getJoueurCourant()) {
                                    caseClic1 = clic;
                                    afficherCasesAccessibles(clic);
                                }
                            } else {
                                caseClic2 = clic;
                                jeu.envoyerCoup(new Coup(caseClic1, caseClic2));
                                caseClic1 = null;
                                caseClic2 = null;
                                casesSurbrillance.clear();
                                remettreCouleursPlateau();
                            }

                        }
                    });

                    jlab.setOpaque(true);
                    jlab.setHorizontalAlignment(SwingConstants.CENTER);
                    jlab.setVerticalAlignment(SwingConstants.CENTER);

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
                    Piece e = c.getPiece();

                    if (e != null) {
                        Couleur couleur = e.getCouleur();

                        if (e instanceof Roi) {
                            tabJLabel[x][y].setIcon(couleur == Couleur.BLANC ? icoRoiB : icoRoiN);
                        } else if (e instanceof Pion) {
                            tabJLabel[x][y].setIcon(couleur == Couleur.BLANC ? icoPionB : icoPionN);
                        } else if (e instanceof Dame) {
                            tabJLabel[x][y].setIcon(couleur == Couleur.BLANC ? icoDameB : icoDameN);
                        } else if (e instanceof Fou) {
                            tabJLabel[x][y].setIcon(couleur == Couleur.BLANC ? icoFouB : icoFouN);
                        } else if (e instanceof Tour) {
                            tabJLabel[x][y].setIcon(couleur == Couleur.BLANC ? icoTourB : icoTourN);
                        } else if (e instanceof Cheval) {
                            tabJLabel[x][y].setIcon(couleur == Couleur.BLANC ? icoChevalB : icoChevalN);
                        }
                    } else {
                        tabJLabel[x][y].setIcon(null);
                    }
                }
            }
        }

        @Override
        public void update(Observable o, Object arg) {
            SwingUtilities.invokeLater(this::mettreAJourAffichage);
        }
        private void afficherCasesAccessibles(Case caseDepart) {
            casesSurbrillance.clear();
            remettreCouleursPlateau();

            Piece p = caseDepart.getPiece();
            if (p != null) {
                ArrayList<Case> deplacements = p.getDeplacementsPossibles();
                casesSurbrillance.addAll(deplacements);

                for (Case c : deplacements) {
                    Point pos = plateau.getMap().get(c);
                    if (pos != null) {
                        tabJLabel[pos.x][pos.y].setBackground(couleurSurbrillance);
                    }
                }
            }
        }
        private void remettreCouleursPlateau() {
            for (int x = 0; x < sizeX; x++) {
                for (int y = 0; y < sizeY; y++) {
                    if ((x + y) % 2 == 0) {
                        tabJLabel[x][y].setBackground(couleurClair);
                    } else {
                        tabJLabel[x][y].setBackground(couleurFonce);
                    }
                }
            }
        }
        public static VueControleur getInstance() {
            return instance;
        }
    }
