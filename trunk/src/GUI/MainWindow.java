/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*s
 * MainWindow.java
 *
 * Created on 30.7.2009, 16:50:20
 */
package GUI;

import javax.swing.*;
import classes.*;
import api.*;
import hibernate.DatabaseManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Vladimír Řiha
 */
public class MainWindow extends javax.swing.JFrame {

    /** Creates new form MainWindow */
    public MainWindow() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        initComponents();
    }

    /**
     *
     * @param id
     * @param name
     * @param lastname
     * @param email
     */
    public MainWindow(int id, String name, String lastname, String email) {
        this.userID = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.main = this;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        initComponents();
        setNumberOfContacts();
        setFilterMenu();
    }

    /**
     * Generates list of all labels to customize displayed contacts
     */
    public void setFilterMenu() {
        JCheckBox box = new JCheckBox("Contacts without labels");
        box.setSelected(true);
        box.setName("organizer");
        box.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterChangedAction(evt);
            }
        });
        jMenu4.add(box);
        try {
            jMenu4.removeAll();
            String labels = DatabaseManager.getUserLabels(this.userID);

            if (labels != null && labels.length() > 0) {
                String[] label = labels.split(",");
                for (int i = 0; i < label.length; i++) {
                    if (label[i].length() > 0 && !label[i].equals("organizer")) {
                        box = new JCheckBox(label[i]);
                        box.setSelected(true);
                        box.setName(label[i]);
                        jMenu4.add(box);
                        box.addActionListener(new java.awt.event.ActionListener() {

                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                filterChangedAction(evt);
                            }
                        });
                    }
                }
            }
        } catch (Exception ex) {
            ErrorDialog ed = new ErrorDialog((this), true, "Error with labels", ex.getMessage());
            ed.setVisible(true);
        }
    }

    /**
     * Adds or removes labels from visible list
     * @param evt
     */
    private void filterChangedAction(ActionEvent evt) {
        JCheckBox box = (JCheckBox) evt.getSource();
        if (box.isSelected()) {
            hiddenLabels.remove(box.getName());
        } else {
            hiddenLabels.add(box.getName());
        }
        if (this.pismeno != null && !this.pismeno.equals("")) {
            createBussinessCard(getHlavniPanel(), this.pismeno, false);
        }
    }

    /**
     * Creates jbuttons for each contact and places them in jpanel
     * @param p jPanel where all jButtons will be placed
     * @param letter letter of contacts to be generated
     * @param resetMenu if true, list of labels will be recreated
     */
    public void createBussinessCard(JPanel p, String letter, boolean resetMenu) {
        if (resetMenu) {
            setFilterMenu();
        }
        this.pismeno = letter;
        p.removeAll();
        p.revalidate();
        p.repaint();

        try {
            List<Polozka> polozky = (List<Polozka>) ItemsManager.getItemsByID(userID, letter, hiddenLabels);
            int i = polozky.size();

            int pocetRadku = 2;
            if (i % 2 == 0) {
                pocetRadku = i / 2;
            } else {
                pocetRadku = (i + 1) / 2;
            }

            if (pocetRadku < 8) {
                pocetRadku = 8;
            }
            p.setLayout(new java.awt.GridLayout(pocetRadku, 2, 4, 5));

            Polozka tmp;
            for (int j = 0; j < i; j++) {
                JButton b = new JButton();
                tmp = polozky.get(j);
                b.setText("<html>" + tmp.getPrijmeni() + ", " + tmp.getJmeno() + "<br/>");
                b.setMaximumSize(new java.awt.Dimension(160, 62));
                b.setMinimumSize(new java.awt.Dimension(160, 62));
                b.setPreferredSize(new java.awt.Dimension(160, 62));
                b.setName("" + (tmp.getId()));
                b.setToolTipText(tmp.getStitek());
                b.addActionListener(new java.awt.event.ActionListener() {

                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        showItem(evt);
                    }
                });

                createPopUpMenu(p, b, tmp.getId(), tmp.getSearchLetter());
                p.add(b);
            }
            if (i <= pocetRadku) {
                int stop = (pocetRadku * 2) - i;
                for (int j = 0; j < stop; j++) {
                    JButton b = new JButton();
                    b.setText("");
                    b.setMaximumSize(new java.awt.Dimension(160, 62));
                    b.setMinimumSize(new java.awt.Dimension(160, 62));
                    b.setPreferredSize(new java.awt.Dimension(160, 62));
                    b.setEnabled(false);
                    b.setVisible(false);
                    p.add(b);
                }
            }
        } catch (Exception ex) {
            ErrorDialog ed = new ErrorDialog((this), true, "Error gettin contacts", ex.getMessage());
            ed.setVisible(true);
        }
        p.revalidate();
    }

    public void createBussinessCardFromSearch(JPanel p, String search) {
        this.hiddenLabels = new HashSet<String>();
        setFilterMenu();
        p.removeAll();
        p.revalidate();
        p.repaint();
        List<Polozka> polozky;
        try {
            if (jTextField1.getText().startsWith("label:")) {
                polozky = (List<Polozka>) ItemsManager.getItemsByStringSearchLabel(userID, search.substring(search.indexOf(":") + 1));
            } else {
                polozky = (List<Polozka>) ItemsManager.getItemsByStringSearch(userID, search);
            }
            int i = polozky.size();

            int pocetRadku = 2;
            if (i % 2 == 0) {
                pocetRadku = i / 2;
            } else {
                pocetRadku = (i + 1) / 2;
            }

            if (pocetRadku < 8) {
                pocetRadku = 8;
            }
            p.setLayout(new java.awt.GridLayout(pocetRadku, 2, 4, 5));

            Polozka tmp;
            for (int j = 0; j < i; j++) {
                JButton b = new JButton();
                tmp = polozky.get(j);
                b.setText("<html>" + tmp.getPrijmeni() + ", " + tmp.getJmeno() + "<br/>");
                b.setMaximumSize(new java.awt.Dimension(160, 62));
                b.setMinimumSize(new java.awt.Dimension(160, 62));
                b.setPreferredSize(new java.awt.Dimension(160, 62));
                b.setName("" + (tmp.getId()));
                b.setToolTipText(tmp.getStitek());
                b.addActionListener(new java.awt.event.ActionListener() {

                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        showItem(evt);
                    }
                });

                createPopUpMenu(p, b, tmp.getId(), tmp.getSearchLetter());
                p.add(b);
            }
            if (i <= pocetRadku) {
                int stop = (pocetRadku * 2) - i;
                for (int j = 0; j < stop; j++) {
                    JButton b = new JButton();
                    b.setText("");
                    b.setMaximumSize(new java.awt.Dimension(160, 62));
                    b.setMinimumSize(new java.awt.Dimension(160, 62));
                    b.setPreferredSize(new java.awt.Dimension(160, 62));
                    b.setEnabled(false);
                    b.setVisible(false);
                    p.add(b);
                }
            }
        } catch (Exception ex) {
            ErrorDialog ed = new ErrorDialog((this), true, "Error gettin contacts", ex.getMessage());
            ed.setVisible(true);
        }
        p.revalidate();
    }

    /**
     * Opens new jDialog with details about selected contact
     * @param evt
     */
    private void showItem(java.awt.event.ActionEvent evt) {
        JButton source = (JButton) evt.getSource();
        final MainWindow mw = this;
        final int id = Integer.valueOf(source.getName()).intValue();
        ItemDialog dialog = new ItemDialog(new javax.swing.JFrame(), true, id, userID, mw);
        dialog.setVisible(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        levyPanel = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        dolniPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        horniPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton27 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton29 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        pravyPanel = new javax.swing.JPanel();
        stredniPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        hlavniPanel = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Organizer Desktop v1.0");
        setBackground(new java.awt.Color(255, 255, 255));
        setForeground(new java.awt.Color(102, 0, 102));
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setResizable(false);

        levyPanel.setBackground(new java.awt.Color(255, 255, 255));
        levyPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 255))); // NOI18N
        levyPanel.setAutoscrolls(true);
        levyPanel.setMaximumSize(new java.awt.Dimension(400, 32767));
        levyPanel.setMinimumSize(new java.awt.Dimension(80, 300));
        levyPanel.setPreferredSize(new java.awt.Dimension(80, 300));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(45, 10));
        jPanel1.setPreferredSize(new java.awt.Dimension(55, 300));
        jPanel1.setLayout(new java.awt.GridLayout(27, 1, 0, 2));

        jButton1.setText("A");
        jButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton1.setBorderPainted(false);
        jButton1.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton1.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton1.setName("A"); // NOI18N
        jButton1.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton1);

        jButton2.setText("B");
        jButton2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton2.setBorderPainted(false);
        jButton2.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton2.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton2.setName("B"); // NOI18N
        jButton2.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton2);

        jButton3.setText("C");
        jButton3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton3.setBorderPainted(false);
        jButton3.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton3.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton3.setName("C"); // NOI18N
        jButton3.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton3);

        jButton4.setText("D");
        jButton4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton4.setBorderPainted(false);
        jButton4.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton4.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton4.setName("D"); // NOI18N
        jButton4.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton4);

        jButton5.setText("E");
        jButton5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton5.setBorderPainted(false);
        jButton5.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton5.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton5.setName("E"); // NOI18N
        jButton5.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton5);

        jButton6.setText("F");
        jButton6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton6.setBorderPainted(false);
        jButton6.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton6.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton6.setName("F"); // NOI18N
        jButton6.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton6);

        jButton7.setText("G");
        jButton7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton7.setBorderPainted(false);
        jButton7.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton7.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton7.setName("G"); // NOI18N
        jButton7.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton7);

        jButton8.setText("H");
        jButton8.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton8.setBorderPainted(false);
        jButton8.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton8.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton8.setName("H"); // NOI18N
        jButton8.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton8);

        jButton9.setText("I");
        jButton9.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton9.setBorderPainted(false);
        jButton9.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton9.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton9.setName("I"); // NOI18N
        jButton9.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton9);

        jButton10.setText("J");
        jButton10.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton10.setBorderPainted(false);
        jButton10.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton10.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton10.setName("J"); // NOI18N
        jButton10.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton10);

        jButton11.setText("K");
        jButton11.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton11.setBorderPainted(false);
        jButton11.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton11.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton11.setName("K"); // NOI18N
        jButton11.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton11);

        jButton12.setText("L");
        jButton12.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton12.setBorderPainted(false);
        jButton12.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton12.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton12.setName("L"); // NOI18N
        jButton12.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton12);

        jButton13.setText("M");
        jButton13.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton13.setBorderPainted(false);
        jButton13.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton13.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton13.setName("M"); // NOI18N
        jButton13.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton13);

        jButton14.setText("N");
        jButton14.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton14.setBorderPainted(false);
        jButton14.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton14.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton14.setName("N"); // NOI18N
        jButton14.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton14);

        jButton15.setText("O");
        jButton15.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton15.setBorderPainted(false);
        jButton15.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton15.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton15.setName("O"); // NOI18N
        jButton15.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton15);

        jButton16.setText("P");
        jButton16.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton16.setBorderPainted(false);
        jButton16.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton16.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton16.setName("P"); // NOI18N
        jButton16.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton16);

        jButton17.setText("Q");
        jButton17.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton17.setBorderPainted(false);
        jButton17.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton17.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton17.setName("Q"); // NOI18N
        jButton17.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton17);

        jButton18.setText("R");
        jButton18.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton18.setBorderPainted(false);
        jButton18.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton18.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton18.setName("R"); // NOI18N
        jButton18.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton18);

        jButton19.setText("S");
        jButton19.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton19.setBorderPainted(false);
        jButton19.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton19.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton19.setName("S"); // NOI18N
        jButton19.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton19);

        jButton20.setText("T");
        jButton20.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton20.setBorderPainted(false);
        jButton20.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton20.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton20.setName("T"); // NOI18N
        jButton20.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton20);

        jButton21.setText("U");
        jButton21.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton21.setBorderPainted(false);
        jButton21.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton21.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton21.setName("U"); // NOI18N
        jButton21.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton21);

        jButton22.setText("V");
        jButton22.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton22.setBorderPainted(false);
        jButton22.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton22.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton22.setName("V"); // NOI18N
        jButton22.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton22);

        jButton23.setText("W");
        jButton23.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton23.setBorderPainted(false);
        jButton23.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton23.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton23.setName("W"); // NOI18N
        jButton23.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton23);

        jButton24.setText("X");
        jButton24.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton24.setBorderPainted(false);
        jButton24.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton24.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton24.setName("X"); // NOI18N
        jButton24.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton24);

        jButton25.setText("Y");
        jButton25.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton25.setBorderPainted(false);
        jButton25.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton25.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton25.setName("Y"); // NOI18N
        jButton25.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton25);

        jButton26.setText("Z");
        jButton26.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton26.setBorderPainted(false);
        jButton26.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton26.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton26.setName("Z"); // NOI18N
        jButton26.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetter(evt);
            }
        });
        jPanel1.add(jButton26);

        jButton28.setText("#");
        jButton28.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, java.awt.Color.white, null, java.awt.Color.darkGray));
        jButton28.setBorderPainted(false);
        jButton28.setMaximumSize(new java.awt.Dimension(45, 5));
        jButton28.setMinimumSize(new java.awt.Dimension(45, 5));
        jButton28.setName("Z"); // NOI18N
        jButton28.setPreferredSize(new java.awt.Dimension(45, 5));
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLetterSpecial(evt);
            }
        });
        jPanel1.add(jButton28);

        levyPanel.setViewportView(jPanel1);

        getContentPane().add(levyPanel, java.awt.BorderLayout.WEST);

        dolniPanel.setBackground(new java.awt.Color(255, 255, 255));
        dolniPanel.setMaximumSize(new java.awt.Dimension(800, 22));
        dolniPanel.setMinimumSize(new java.awt.Dimension(800, 22));
        dolniPanel.setPreferredSize(new java.awt.Dimension(800, 22));
        dolniPanel.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 2, 11));
        jLabel1.setText("Alpha version");
        dolniPanel.add(jLabel1, java.awt.BorderLayout.CENTER);

        getContentPane().add(dolniPanel, java.awt.BorderLayout.SOUTH);

        horniPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/logo.png"))); // NOI18N
        jLabel2.setText("Organizer");

        jButton27.setText("Add contact");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        jButton29.setText("Search");
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 2, 9));
        jLabel4.setText("<html>Tip: If you write \"label:se\", then all contacts with labels like secret <br/> or sea will be displayed</html>");

        javax.swing.GroupLayout horniPanelLayout = new javax.swing.GroupLayout(horniPanel);
        horniPanel.setLayout(horniPanelLayout);
        horniPanelLayout.setHorizontalGroup(
            horniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(horniPanelLayout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(horniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(horniPanelLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(jButton27))
                    .addGroup(horniPanelLayout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addComponent(jLabel3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(horniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(horniPanelLayout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton29))
                    .addGroup(horniPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)))
                .addContainerGap())
        );
        horniPanelLayout.setVerticalGroup(
            horniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(horniPanelLayout.createSequentialGroup()
                .addGroup(horniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(horniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(horniPanelLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(horniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton27)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton29))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(horniPanel, java.awt.BorderLayout.NORTH);

        pravyPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pravyPanelLayout = new javax.swing.GroupLayout(pravyPanel);
        pravyPanel.setLayout(pravyPanelLayout);
        pravyPanelLayout.setHorizontalGroup(
            pravyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        pravyPanelLayout.setVerticalGroup(
            pravyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 537, Short.MAX_VALUE)
        );

        getContentPane().add(pravyPanel, java.awt.BorderLayout.EAST);

        stredniPanel.setBackground(new java.awt.Color(255, 255, 255));
        stredniPanel.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);

        hlavniPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout hlavniPanelLayout = new javax.swing.GroupLayout(hlavniPanel);
        hlavniPanel.setLayout(hlavniPanelLayout);
        hlavniPanelLayout.setHorizontalGroup(
            hlavniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 673, Short.MAX_VALUE)
        );
        hlavniPanelLayout.setVerticalGroup(
            hlavniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 561, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(hlavniPanel);

        stredniPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(stredniPanel, java.awt.BorderLayout.CENTER);

        jMenuBar1.setBackground(new java.awt.Color(215, 223, 230));

        jMenu3.setBackground(new java.awt.Color(215, 223, 230));
        jMenu3.setMnemonic('O');
        jMenu3.setText("Organizer");

        jMenu1.setText("Export");

        jMenuItem1.setText("Export to XML");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportXMLAction(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Export to HTML");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportHTMLAction(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setForeground(new java.awt.Color(153, 153, 153));
        jMenuItem3.setText("Export to TXT");
        jMenu1.add(jMenuItem3);

        jMenuItem4.setText("Export to CSV");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openCSVExportDialogAction(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem5.setText("Export to Gmail");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openExportGoogleDialog(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenu3.add(jMenu1);

        jMenu2.setText("Import");

        jMenuItem6.setText("Import XML");
        jMenu2.add(jMenuItem6);

        jMenuItem9.setBackground(new java.awt.Color(153, 153, 153));
        jMenuItem9.setText("Import CSV");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importCSVAction(evt);
            }
        });
        jMenu2.add(jMenuItem9);

        jMenuItem10.setText("Import Gmail");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importGmailAction(evt);
            }
        });
        jMenu2.add(jMenuItem10);

        jMenu3.add(jMenu2);
        jMenu3.add(jSeparator1);

        jMenuItem8.setText("Memory usage");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openInfoDialog(evt);
            }
        });
        jMenu3.add(jMenuItem8);

        jMenuBar1.add(jMenu3);

        jMenu4.setBackground(new java.awt.Color(215, 223, 230));
        jMenu4.setText("View labels");
        jMenuBar1.add(jMenu4);

        jMenu5.setText("Tools");

        jMenuItem7.setText("Remove contacts");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openDeleteDialog(evt);
            }
        });
        jMenu5.add(jMenuItem7);

        jMenuBar1.add(jMenu5);

        jMenu6.setText("Info");

        jMenuItem11.setText("About");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openAboutDialog(evt);
            }
        });
        jMenu6.add(jMenuItem11);

        jMenuBar1.add(jMenu6);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Calls methods to show contacts of the selected letter
     * @param evt
     */
    private void selectLetter(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectLetter
        JButton jb = (JButton) evt.getSource();
        createBussinessCard(getHlavniPanel(), jb.getName(), false);
    }//GEN-LAST:event_selectLetter

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        final int idUser = userID;
        NewItemDialog dialog = new NewItemDialog(new javax.swing.JFrame(), true, idUser, main);
        dialog.setVisible(true);
    }//GEN-LAST:event_jButton27ActionPerformed

    private void importGmailAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importGmailAction
        final String l = this.pismeno;
        GoogleImportDialog dialog = new GoogleImportDialog(new javax.swing.JFrame(), true, userID, l, main);
        dialog.setVisible(true);
    }//GEN-LAST:event_importGmailAction

    private void exportXMLAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportXMLAction
        ExportXMLDialog1 dialog = new ExportXMLDialog1(new javax.swing.JFrame(), true, userID);
        dialog.setVisible(true);
    }//GEN-LAST:event_exportXMLAction

    private void exportHTMLAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportHTMLAction
        ExportHTMLDialog dialog = new ExportHTMLDialog(new javax.swing.JFrame(), true, userID, name, lastname);
        dialog.setVisible(true);
    }//GEN-LAST:event_exportHTMLAction

    private void openDeleteDialog(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openDeleteDialog
        DeleteDialog dl = new DeleteDialog((new javax.swing.JFrame()), false, this.userID, this);
        dl.setVisible(true);
    }//GEN-LAST:event_openDeleteDialog

    private void selectLetterSpecial(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectLetterSpecial
        createBussinessCard(getHlavniPanel(), "#", false);
    }//GEN-LAST:event_selectLetterSpecial

    private void openInfoDialog(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openInfoDialog
        MemoryDialog md = new MemoryDialog(new javax.swing.JFrame(), false, this.userID);
        md.setVisible(true);
    }//GEN-LAST:event_openInfoDialog

    private void openExportGoogleDialog(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openExportGoogleDialog
        GoogleExporDialog gl = new GoogleExporDialog((new javax.swing.JFrame()), false, this.userID, this);
        gl.setVisible(true);
    }//GEN-LAST:event_openExportGoogleDialog

    private void importCSVAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importCSVAction
        // TODO add your handling code here:
        ImportCSVDialog ids = new ImportCSVDialog((new javax.swing.JFrame()), true, this.userID, this.pismeno, this);
        ids.setVisible(true);

    }//GEN-LAST:event_importCSVAction

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        // TODO add your handling code here:
        if (jTextField1.getText().length() > 0) {
            createBussinessCardFromSearch(getHlavniPanel(), jTextField1.getText());
        }
    }//GEN-LAST:event_jButton29ActionPerformed

    private void openCSVExportDialogAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openCSVExportDialogAction
        // TODO add your handling code here:
        ExportCSVDialog1 exp = new ExportCSVDialog1(new javax.swing.JFrame(), true, userID);
        exp.setVisible(true);
    }//GEN-LAST:event_openCSVExportDialogAction

    private void openAboutDialog(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openAboutDialog
        AboutDialog ab= new AboutDialog(new javax.swing.JFrame(), true);
        ab.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_openAboutDialog
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel dolniPanel;
    private javax.swing.JPanel hlavniPanel;
    private javax.swing.JPanel horniPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JScrollPane levyPanel;
    private javax.swing.JPanel pravyPanel;
    private javax.swing.JPanel stredniPanel;
    // End of variables declaration//GEN-END:variables
    private JButton[] tlacitka = new JButton[26];
    private boolean logged = false;
    private int userID;
    private String name;
    private String lastname;
    private String email;
    private MainWindow main;
    private String labels;
    private String pismeno;
    private Set<String> hiddenLabels = new HashSet<String>();

    /**
     * @return the userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @param userID the userID to set
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @param lastname the lastname to set
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    private void createPopUpMenu(JPanel p, JButton b, int polozka, String letter) {
        b.removeAll();
        b.revalidate();
        b.repaint();
        JMenuItem menuItem;
//JPanel snaha = new JPanel();
        JLayeredPane lpane = new JLayeredPane();
        ActionListener actionD = new DeleteItemPopListener(this, userID, polozka, letter);
        b.add(lpane);
        //Create the popup menu.
        JPopupMenu popup = new JPopupMenu();
        menuItem = new JMenuItem("Remove contact");
        menuItem.addActionListener(actionD);
        popup.add(menuItem);
        MouseListener popupListener = new NewPopupListener(popup);
        b.addMouseListener(popupListener);
        lpane.add(popup);
    }

    /**
     * @return the hlavniPanel
     */
    public javax.swing.JPanel getHlavniPanel() {
        return hlavniPanel;
    }

    /**
     *
     */
    public void setNumberOfContacts() {
        try {
            int pocet = ItemsManager.countItems(userID);
            jLabel3.setText("Total contacts: " + pocet);
        } catch (Exception ex) {
            jLabel3.setText("Total contacts: ??? ");
        }
    }
}

class DeleteItemPopListener implements ActionListener {

    private MainWindow mw;
    private int idUser;
    private int idPol;
    private String letter;

    public DeleteItemPopListener(MainWindow mw, int idU, int idP, String letter) {
        this.mw = mw;
        this.idUser = idU;
        this.idPol = idP;
        this.letter = letter;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            ItemsManager.deleteItem(idPol, idUser);
            mw.createBussinessCard(mw.getHlavniPanel(), letter, true);
            mw.setNumberOfContacts();
        } catch (Exception ex) {
            ErrorDialog ed = new ErrorDialog(new javax.swing.JFrame(), true, "Error with contact", ex.getMessage());
            ed.setVisible(true);
        }
    }
}

class NewPopupListener extends MouseAdapter {

    JPopupMenu popup;

    NewPopupListener(JPopupMenu popupMenu) {
        super();
        popup = popupMenu;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {

            popup.show(e.getComponent(),
                    e.getX(), e.getY());
            popup.repaint();
        }
    }
}


