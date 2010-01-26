/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ItemDialog.java
 *
 * Created on 6.8.2009, 13:50:46
 */
package GUI;

import api.*;
import java.awt.event.ActionEvent;
import util.*;
import classes.Adresa;
import classes.ObecnyKontakt;
import classes.Polozka;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Vladimír Řiha
 */
public class ItemDialog extends javax.swing.JDialog {

    /** Creates new form ItemDialog
     * @param parent
     * @param modal
     * @param idItem
     * @param idUser
     * @param main
     */
    public ItemDialog(java.awt.Frame parent, boolean modal, int idItem, int idUser, MainWindow main) {
        super(parent, false);
        try {
            initComponents();
            Polozka p = ItemsManager.getFullItemByID(idItem, idUser);
            this.letter = p.getSearchLetter();
            this.idP = idItem;
            this.idUser = idUser;
            this.main = main;
            Set kontakty = p.getKontakty();
            // najit telefony
            ArrayList<ObecnyKontakt> telefonyA = new ArrayList<ObecnyKontakt>();
            ArrayList<ObecnyKontakt> mailyA = new ArrayList<ObecnyKontakt>();
            ArrayList<ObecnyKontakt> imsA = new ArrayList<ObecnyKontakt>();
            ArrayList<ObecnyKontakt> urlsA = new ArrayList<ObecnyKontakt>();
            ArrayList<ObecnyKontakt> otherA = new ArrayList<ObecnyKontakt>();
            ObecnyKontakt ob;
            Iterator it = kontakty.iterator();
            String typ;
            while (it.hasNext()) {
                ob = (ObecnyKontakt) it.next();
                typ = ob.getTyp();
                if (typ.equalsIgnoreCase("email")) {
                    mailyA.add(ob);
                } else if (typ.equalsIgnoreCase("telefon")) {
                    telefonyA.add(ob);
                } else if (typ.equalsIgnoreCase("im")) {
                    imsA.add(ob);
                } else if (typ.equalsIgnoreCase("url")) {
                    urlsA.add(ob);
                } else {
                    otherA.add(ob);
                }
            }
            ObecnyKontakt[] telefony = new ObecnyKontakt[telefonyA.size()];
            telefonyA.toArray(telefony);
            Arrays.sort(telefony);
            ObecnyKontakt[] maily = new ObecnyKontakt[mailyA.size()];
            mailyA.toArray(maily);
            Arrays.sort(maily);
            ObecnyKontakt[] ims = new ObecnyKontakt[imsA.size()];
            imsA.toArray(ims);
            Arrays.sort(ims);
            ObecnyKontakt[] urls = new ObecnyKontakt[urlsA.size()];
            urlsA.toArray(urls);
            Arrays.sort(urls);
            ObecnyKontakt[] other = new ObecnyKontakt[otherA.size()];
            otherA.toArray(other);
            Arrays.sort(other);
            Set<Adresa> adresy = p.getAdresy();
            ArrayList<Adresa> adresyA = new ArrayList<Adresa>();
            Iterator<Adresa> l = adresy.iterator();
            int i = 0;
            while (l.hasNext()) {
                adresyA.add(l.next());
            }
            Adresa[] adresyAA = new Adresa[adresyA.size()];
            adresyA.toArray(adresyAA);
            Arrays.sort(adresyAA);
            int radkySloupecA = 3 + adresyAA.length + maily.length + urls.length;
            int radkySloupecB = 3 + telefony.length + ims.length + other.length;
            StringChecker checker = new StringChecker();
            this.jmeno = checker.removeAscii(p.getPrijmeni());
            this.prijmeni = checker.removeAscii(p.getJmeno());
            this.setTitle(checker.removeAscii(p.getPrijmeni()) + ", " + checker.removeAscii(p.getJmeno()));
            jLabel1.setText("<html>" + p.getPrijmeni() + ", " + p.getJmeno());
            createBussinessCard(radkySloupecA, radkySloupecB, adresyAA, maily, urls, telefony, ims, other);
            //        this.setAlwaysOnTop(false);
        } catch (Exception ex) {
            ErrorDialog ed = new ErrorDialog(new javax.swing.JFrame(), true, "Error with contact", ex.getMessage());
            ed.setVisible(true);
        }
    }

    /**
     *
     */
    public void reCreate() {
        try {
            jPanel3.removeAll();
            jPanel3.revalidate();
            jPanel3.repaint();
            Polozka p = ItemsManager.getFullItemByID(idP, idUser);
            Set kontakty = p.getKontakty();
            // najit telefony
            ArrayList<ObecnyKontakt> telefonyA = new ArrayList<ObecnyKontakt>();
            ArrayList<ObecnyKontakt> mailyA = new ArrayList<ObecnyKontakt>();
            ArrayList<ObecnyKontakt> imsA = new ArrayList<ObecnyKontakt>();
            ArrayList<ObecnyKontakt> urlsA = new ArrayList<ObecnyKontakt>();
            ArrayList<ObecnyKontakt> otherA = new ArrayList<ObecnyKontakt>();
            ObecnyKontakt ob;
            Iterator it = kontakty.iterator();
            String typ;
            while (it.hasNext()) {
                ob = (ObecnyKontakt) it.next();
                typ = ob.getTyp();
                if (typ.equalsIgnoreCase("email")) {
                    mailyA.add(ob);
                } else if (typ.equalsIgnoreCase("telefon")) {
                    telefonyA.add(ob);
                } else if (typ.equalsIgnoreCase("im")) {
                    imsA.add(ob);
                } else if (typ.equalsIgnoreCase("url")) {
                    urlsA.add(ob);
                } else {
                    otherA.add(ob);
                }
            }
            ObecnyKontakt[] telefony = new ObecnyKontakt[telefonyA.size()];
            telefonyA.toArray(telefony);
            Arrays.sort(telefony);
            ObecnyKontakt[] maily = new ObecnyKontakt[mailyA.size()];
            mailyA.toArray(maily);
            Arrays.sort(maily);
            ObecnyKontakt[] ims = new ObecnyKontakt[imsA.size()];
            imsA.toArray(ims);
            Arrays.sort(ims);
            ObecnyKontakt[] urls = new ObecnyKontakt[urlsA.size()];
            urlsA.toArray(urls);
            Arrays.sort(urls);
            ObecnyKontakt[] other = new ObecnyKontakt[otherA.size()];
            otherA.toArray(other);
            Arrays.sort(other);
            Set<Adresa> adresy = p.getAdresy();
            ArrayList<Adresa> adresyA = new ArrayList<Adresa>();
            Iterator<Adresa> l = adresy.iterator();
            int i = 0;
            while (l.hasNext()) {
                adresyA.add(l.next());
            }
            Adresa[] adresyAA = new Adresa[adresyA.size()];
            adresyA.toArray(adresyAA);
            Arrays.sort(adresyAA);
            int radkySloupecA = 3 + adresyAA.length + maily.length + urls.length;
            int radkySloupecB = 3 + telefony.length + ims.length + other.length;
            StringChecker checker = new StringChecker();
            this.jmeno = checker.removeAscii(p.getPrijmeni());
            this.prijmeni = checker.removeAscii(p.getJmeno());
            this.setTitle(checker.removeAscii(p.getPrijmeni()) + ", " + checker.removeAscii(p.getJmeno()));
            jLabel1.setText("<html>" + p.getPrijmeni() + ", " + p.getJmeno());
            createBussinessCard(radkySloupecA, radkySloupecB, adresyAA, maily, urls, telefony, ims, other);
            //        this.setAlwaysOnTop(false);
        } catch (Exception ex) {
            ErrorDialog ed = new ErrorDialog(new javax.swing.JFrame(), true, "Error with contact", ex.getMessage());
            ed.setVisible(true);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();

        setMinimumSize(new java.awt.Dimension(440, 78));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(153, 153, 153))); // NOI18N
        jPanel1.setMaximumSize(new java.awt.Dimension(400, 49));
        jPanel1.setMinimumSize(new java.awt.Dimension(400, 49));
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 40));

        jLabel1.setBackground(new java.awt.Color(238, 238, 238));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Contact");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/vcard.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(400, 269));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(400, 400));
        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jMenuBar1.setBackground(new java.awt.Color(215, 223, 230));
        jMenuBar1.setBorder(null);
        jMenuBar1.setPreferredSize(new java.awt.Dimension(118, 25));

        jMenu1.setBackground(new java.awt.Color(215, 223, 230));
        jMenu1.setText("Contact");

        jMenuItem1.setText("Delete");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteItem(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Edit");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editItem(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setBackground(new java.awt.Color(215, 223, 230));
        jMenu2.setText("Add detail");

        jMenuItem3.setText("Add address");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createAddress(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem4.setText("Add phone number");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createPhone(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem5.setText("Add email");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createEmail(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuItem6.setText("Add IM");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createIm(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuItem7.setText("Add URL address");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createUrl(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenuItem8.setText("Add other");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createOther(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void deleteItem(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteItem
        try {
            ItemsManager.deleteItem(idP, idUser);
            this.setVisible(false);
            main.createBussinessCard(main.getHlavniPanel(), this.letter, true);
            main.setNumberOfContacts();
        } catch (Exception ex) {
            ErrorDialog ed = new ErrorDialog(new javax.swing.JFrame(), true, "Item was not removed", ex.getMessage());
            ed.setVisible(true);
        }
    }//GEN-LAST:event_deleteItem

    private void editItem(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editItem
        // TODO add your handling code here:
        final ItemDialog iDialog = this;
        EditItemDialog dialog = new EditItemDialog(new javax.swing.JFrame(), true, jmeno, prijmeni, idP, idUser, iDialog, main);
        dialog.setVisible(true);
    }//GEN-LAST:event_editItem

    private void createAddress(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createAddress
        final int idPol = idP;
        final ItemDialog iDialog = this;
        AddressDialog dialog = new AddressDialog(new javax.swing.JFrame(), true, idPol, iDialog);
        dialog.setVisible(true);
    }//GEN-LAST:event_createAddress

    private void createPhone(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createPhone
        // TODO add your handling code here:
        final int idPol = idP;
        final ItemDialog iDialog = this;
        TelephoneDialog dialog = new TelephoneDialog(new javax.swing.JFrame(), true, idPol, iDialog);
        dialog.setVisible(true);
    }//GEN-LAST:event_createPhone

    private void createEmail(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createEmail
        // TODO add your handling code here:
        final int idPol = idP;
        final ItemDialog iDialog = this;
        EmailDialog dialog = new EmailDialog(new javax.swing.JFrame(), true, idPol, iDialog);
        dialog.setVisible(true);
    }//GEN-LAST:event_createEmail

    private void createIm(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createIm
        // TODO add your handling code here:
        final int idPol = idP;
        final ItemDialog iDialog = this;
        ImDialog dialog = new ImDialog(new javax.swing.JFrame(), true, idPol, iDialog);
        dialog.setVisible(true);
    }//GEN-LAST:event_createIm

    private void createUrl(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createUrl
        // TODO add your handling code here:
        final int idPol = idP;
        final ItemDialog iDialog = this;
        UrlDialog dialog = new UrlDialog(new javax.swing.JFrame(), true, idPol, iDialog);
        dialog.setVisible(true);
    }//GEN-LAST:event_createUrl

    private void createOther(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createOther
        // TODO add your handling code here:
        final int idPol = idP;
        final ItemDialog iDialog = this;
        OtherDialog dialog = new OtherDialog(new javax.swing.JFrame(), true, idPol, iDialog);
        dialog.setVisible(true);
    }//GEN-LAST:event_createOther
    /**
     * @param args the command line arguments
     */
    private int idUser;
    private int idP;
    private MainWindow main;
    private String letter;
    private String jmeno;
    private String prijmeni;
    private javax.swing.JPanel jPanel3;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    private void createBussinessCard(int pocetA, int pocetB, Adresa[] adresyAA, ObecnyKontakt[] maily, ObecnyKontakt[] urls, ObecnyKontakt[] telefony, ObecnyKontakt[] ims, ObecnyKontakt[] other) {
        StringChecker checker = new StringChecker();
//        int pocetRadku = Math.max(pocetA, pocetB);
        jPanel3 = new javax.swing.JPanel();
        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new java.awt.GridLayout(1, 2, 0, 0));
        jScrollPane1.setViewportView(jPanel3);
        Adresa tmp;

        JPanel levy = new JPanel();
        levy.setLayout(new BoxLayout(levy, BoxLayout.PAGE_AXIS));
        levy.setOpaque(true);
        levy.setBackground(new Color(255, 255, 255));

        jPanel3.add(levy);
        levy.setName("levy");

        JLabel labelA = new javax.swing.JLabel();
        labelA.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelA.setText("Address");
        labelA.setBackground(new Color(255, 255, 255));
        labelA.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane paneA = new javax.swing.JScrollPane();
        paneA.setAlignmentX(Component.LEFT_ALIGNMENT);
        paneA.setMaximumSize(new java.awt.Dimension(180, 35));
        paneA.setMinimumSize(new java.awt.Dimension(180, 35));
        paneA.setPreferredSize(new java.awt.Dimension(180, 35));
        paneA.setBorder(null);
        paneA.setBackground(new java.awt.Color(255, 255, 255));
        labelA.setBackground(new java.awt.Color(255, 255, 255));
        labelA.setOpaque(true);
        paneA.setViewportView(labelA);


        levy.add(paneA);

        if (adresyAA.length > 0) {

            for (int j = 0; j < adresyAA.length; j++) {

                JScrollPane pane2 = new javax.swing.JScrollPane();
                pane2.setAlignmentX(Component.LEFT_ALIGNMENT);
                pane2.setMaximumSize(new java.awt.Dimension(150, 60));
                pane2.setMinimumSize(new java.awt.Dimension(150, 60));
                pane2.setPreferredSize(new java.awt.Dimension(150, 60));
                pane2.setBackground(new java.awt.Color(238, 238, 238));
                pane2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
                JTextArea area = new JTextArea();
                area.setBackground(new java.awt.Color(238, 238, 238));
                area.setAlignmentX(Component.LEFT_ALIGNMENT);
                area.setEditable(false);
                area.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
                area.setRows(3);

                pane2.setViewportView(area);

                tmp = adresyAA[j];
                area.setName(tmp.getId() + "");
                addMenu(area, 0, this, "address");
                area.setText(checker.removeAscii(tmp.getTyp()) + System.getProperty("line.separator") + checker.removeAscii(tmp.getUlice()) + " " + checker.removeAscii(tmp.getCp()) + System.getProperty("line.separator") + checker.removeAscii(tmp.getMesto()) + ", " + tmp.getPsc());
                area.setName("" + (tmp.getId()));
                area.setOpaque(true);
                levy.add(pane2);
            }
        }


// EMAILY

        JLabel labelB = new javax.swing.JLabel();
        labelB.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelB.setText("Email");
        labelB.setAlignmentX(Component.LEFT_ALIGNMENT);
        JScrollPane paneB = new javax.swing.JScrollPane();
        paneB.setBorder(null);
        paneB.setAlignmentX(Component.LEFT_ALIGNMENT);
        paneB.setMaximumSize(new java.awt.Dimension(180, 35));
        paneB.setMinimumSize(new java.awt.Dimension(180, 35));
        paneB.setPreferredSize(new java.awt.Dimension(180, 35));
        paneB.setBackground(new java.awt.Color(255, 255, 255));
        labelB.setBackground(new java.awt.Color(255, 255, 255));
        labelB.setOpaque(true);
        paneB.setViewportView(labelB);

        ObecnyKontakt ob;
        levy.add(paneB);

        if (maily.length > 0) {

            for (int j = 0; j < maily.length; j++) {

                JScrollPane pane2 = new javax.swing.JScrollPane();
                pane2.setAlignmentX(Component.LEFT_ALIGNMENT);
                pane2.setMaximumSize(new java.awt.Dimension(150, 40));
                pane2.setMinimumSize(new java.awt.Dimension(150, 40));
                pane2.setPreferredSize(new java.awt.Dimension(150, 40));
                pane2.setBackground(new java.awt.Color(238, 238, 238));
                pane2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
                JTextArea area = new JTextArea();
                area.setBackground(new java.awt.Color(238, 238, 238));
                area.setEditable(false);
                area.setAlignmentX(Component.LEFT_ALIGNMENT);
                area.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
                area.setRows(2);
                pane2.setViewportView(area);

                ob = maily[j];
                area.setName(ob.getId() + "");
                addMenu(area, 1, this, "email");
                area.setText(checker.removeAscii(ob.getOznaceni()) + " - " + checker.removeAscii(ob.getTyp2()) + System.getProperty("line.separator") + checker.removeAscii(ob.getHodnota()));
                area.setName("" + (ob.getId()));
                area.setOpaque(true);
                levy.add(pane2);

            }
        }


// TELEFONY

        labelB = new javax.swing.JLabel();
        labelB.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelB.setText("Phones");
        labelB.setAlignmentX(Component.LEFT_ALIGNMENT);
        paneB = new javax.swing.JScrollPane();
        paneB.setBorder(null);
        paneB.setMaximumSize(new java.awt.Dimension(180, 35));
        paneB.setMinimumSize(new java.awt.Dimension(180, 35));
        paneB.setPreferredSize(new java.awt.Dimension(180, 35));
        paneB.setBackground(new java.awt.Color(255, 255, 255));
        paneB.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelB.setBackground(new java.awt.Color(255, 255, 255));
        labelB.setOpaque(true);
        paneB.setViewportView(labelB);
        levy.add(paneB);

        if (telefony.length > 0) {

            for (int j = 0; j < telefony.length; j++) {

                JScrollPane pane2 = new javax.swing.JScrollPane();
                pane2.setAlignmentX(Component.LEFT_ALIGNMENT);
                pane2.setMaximumSize(new java.awt.Dimension(150, 40));
                pane2.setMinimumSize(new java.awt.Dimension(150, 40));
                pane2.setPreferredSize(new java.awt.Dimension(150, 40));
                pane2.setBackground(new java.awt.Color(238, 238, 238));
                pane2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
                JTextArea area = new JTextArea();
                area.setAlignmentX(Component.LEFT_ALIGNMENT);
                area.setBackground(new java.awt.Color(238, 238, 238));
                area.setEditable(false);
                area.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
                area.setRows(2);
                pane2.setViewportView(area);

                ob = telefony[j];
                area.setName(ob.getId() + "");
                addMenu(area, 1, this, "phone");
                area.setText(checker.removeAscii(ob.getOznaceni()) + System.getProperty("line.separator") + checker.removeAscii(ob.getHodnota()));
                area.setName("" + (ob.getId()));
                area.setOpaque(true);
                levy.add(pane2);
            }
        }


        JPanel pravy = new JPanel();
        pravy.setLayout(new BoxLayout(pravy, BoxLayout.PAGE_AXIS));
        pravy.setOpaque(true);
        pravy.setBackground(new Color(255, 255, 255));
//pravy.setPreferredSize(new java.awt.Dimension(sirka, vyska));
        jPanel3.add(pravy);
        pravy.setName("pravy");


        labelB = new javax.swing.JLabel();
        labelB.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelB.setText("IM");
        labelB.setAlignmentX(Component.LEFT_ALIGNMENT);
        paneB = new javax.swing.JScrollPane();
        paneB.setBorder(null);
        paneB.setMaximumSize(new java.awt.Dimension(180, 35));
        paneB.setMinimumSize(new java.awt.Dimension(180, 35));
        paneB.setPreferredSize(new java.awt.Dimension(180, 35));
        paneB.setBackground(new java.awt.Color(255, 255, 255));
        paneB.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelB.setBackground(new java.awt.Color(255, 255, 255));
        labelB.setOpaque(true);
        paneB.setViewportView(labelB);
        pravy.add(paneB);


        if (ims.length > 0) {

            for (int j = 0; j < ims.length; j++) {

                JScrollPane pane2 = new javax.swing.JScrollPane();
                pane2.setAlignmentX(Component.LEFT_ALIGNMENT);
                pane2.setMaximumSize(new java.awt.Dimension(150, 40));
                pane2.setMinimumSize(new java.awt.Dimension(150, 40));
                pane2.setPreferredSize(new java.awt.Dimension(150, 40));
                pane2.setBackground(new java.awt.Color(238, 238, 238));
                pane2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
                JTextArea area = new JTextArea();
                area.setBackground(new java.awt.Color(238, 238, 238));
                area.setEditable(false);
                area.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
                area.setRows(2);
                area.setAlignmentX(Component.LEFT_ALIGNMENT);
                pane2.setViewportView(area);

                ob = ims[j];
                area.setName(ob.getId() + "");
                addMenu(area, 1, this, "im");
                area.setText(checker.removeAscii(ob.getTyp2()) + " - " + checker.removeAscii(ob.getOznaceni()) + System.getProperty("line.separator") + checker.removeAscii(ob.getHodnota()));
                area.setName("" + (ob.getId()));
                area.setOpaque(true);
                pravy.add(pane2);

            }
        }

        labelB = new javax.swing.JLabel();
        labelB.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelB.setText("URL address");
        labelB.setAlignmentX(Component.LEFT_ALIGNMENT);
        paneB = new javax.swing.JScrollPane();
        paneB.setBorder(null);
        paneB.setMaximumSize(new java.awt.Dimension(180, 35));
        paneB.setMinimumSize(new java.awt.Dimension(180, 35));
        paneB.setPreferredSize(new java.awt.Dimension(180, 35));
        paneB.setBackground(new java.awt.Color(255, 255, 255));
        paneB.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelB.setBackground(new java.awt.Color(255, 255, 255));
        labelB.setOpaque(true);
        paneB.setViewportView(labelB);
        pravy.add(paneB);

        if (urls.length > 0) {

            for (int j = 0; j < urls.length; j++) {

                JScrollPane pane2 = new javax.swing.JScrollPane();
                pane2.setAlignmentX(Component.LEFT_ALIGNMENT);
                pane2.setMaximumSize(new java.awt.Dimension(150, 40));
                pane2.setMinimumSize(new java.awt.Dimension(150, 40));
                pane2.setPreferredSize(new java.awt.Dimension(150, 40));
                pane2.setBackground(new java.awt.Color(238, 238, 238));
                pane2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
                JTextArea area = new JTextArea();
                area.setBackground(new java.awt.Color(238, 238, 238));
                area.setEditable(false);
                area.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
                area.setRows(2);
                area.setAlignmentX(Component.LEFT_ALIGNMENT);
                pane2.setViewportView(area);

                ob = urls[j];
                area.setName(ob.getId() + "");
                addMenu(area, 1, this, "url");
                area.setText(checker.removeAscii(ob.getTyp2()) + " - " + checker.removeAscii(ob.getOznaceni()) + System.getProperty("line.separator") + checker.removeAscii(ob.getHodnota()));
                area.setName("" + (ob.getId()));
                area.setOpaque(true);
                pravy.add(pane2);
            }
        }

        labelB = new javax.swing.JLabel();
        labelB.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelB.setText("Other");
        labelB.setAlignmentX(Component.LEFT_ALIGNMENT);
        paneB = new javax.swing.JScrollPane();
        paneB.setBorder(null);
        paneB.setMaximumSize(new java.awt.Dimension(180, 35));
        paneB.setMinimumSize(new java.awt.Dimension(180, 35));
        paneB.setPreferredSize(new java.awt.Dimension(180, 35));
        paneB.setBackground(new java.awt.Color(255, 255, 255));
        paneB.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelB.setBackground(new java.awt.Color(255, 255, 255));
        labelB.setOpaque(true);
        paneB.setViewportView(labelB);
        pravy.add(paneB);

        if (other.length > 0) {

            for (int j = 0; j < other.length; j++) {

                JScrollPane pane2 = new javax.swing.JScrollPane();
                pane2.setAlignmentX(Component.LEFT_ALIGNMENT);
//                pane2.setMaximumSize(new java.awt.Dimension(150, 40));
//                pane2.setMinimumSize(new java.awt.Dimension(150, 40));
//                pane2.setPreferredSize(new java.awt.Dimension(150, 40));
                pane2.setBackground(new java.awt.Color(238, 238, 238));
                pane2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
                JTextArea area = new JTextArea();
                area.setBackground(new java.awt.Color(238, 238, 238));
                area.setEditable(false);
                area.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
                area.setRows(2);
                pane2.setViewportView(area);

                ob = other[j];
                area.setName(ob.getId() + "");
                addMenu(area, 1, this, "other");
                area.setAlignmentX(Component.LEFT_ALIGNMENT);
                area.setText(checker.removeAscii(checker.removeAscii(ob.getOznaceni()) + System.getProperty("line.separator") + checker.removeAscii(ob.getHodnota())));
                area.setName("" + (ob.getId()));
                area.setOpaque(true);
                pravy.add(pane2);

            }
        }
    }

    private void addMenu(JTextArea area, int variant, ItemDialog iDiag, String type) {

        if (variant == 0) {
            ActionListener actionListener = new DeleteAddressPopListener(iDiag);
            ActionListener actionListener2 = new EditPopListener(iDiag, idP, type);


            JMenuItem menuItem;
            JLayeredPane lpane = new JLayeredPane();
            area.add(lpane);
            JPopupMenu popup = new JPopupMenu();
            menuItem = new JMenuItem("Edit item");
            menuItem.setName(area.getName());
            menuItem.addActionListener(actionListener2);
            popup.add(menuItem);
            menuItem = new JMenuItem("Remove item");
            menuItem.setName(area.getName());
            menuItem.addActionListener(actionListener);
            popup.add(menuItem);
            MouseListener popupListener = new NewPopupListenerI(popup);
            area.addMouseListener(popupListener);
            lpane.add(popup);

        } else {
            ActionListener actionListener = new CommonPopListener(iDiag);
            ActionListener actionListener2 = new EditPopListener(iDiag, idP, type);
            JMenuItem menuItem;
            JLayeredPane lpane = new JLayeredPane();
            area.add(lpane);
            JPopupMenu popup = new JPopupMenu();
            menuItem = new JMenuItem("Edit item");
            menuItem.setName(area.getName());
            menuItem.addActionListener(actionListener2);
            popup.add(menuItem);
            menuItem = new JMenuItem("Remove item");
            menuItem.setName(area.getName());
            menuItem.addActionListener(actionListener);
            popup.add(menuItem);
            MouseListener popupListener = new NewPopupListenerI(popup);
            area.addMouseListener(popupListener);
            lpane.add(popup);

        }

    }

    private void nastavCursor(JLabel label) {
        Cursor cursor = new Cursor(Cursor.TEXT_CURSOR);
        label.setCursor(cursor);
    }

    /**
     * @return the jLabel1
     */
    public javax.swing.JLabel getjLabel1() {
        return jLabel1;
    }
}

class DeleteAddressPopListener implements ActionListener {

    private ItemDialog iDialog;

    public DeleteAddressPopListener(ItemDialog iDialog) {
        this.iDialog = iDialog;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            JMenuItem it = (JMenuItem) e.getSource();
            ContactManager.deleteAddress(it.getName());
            iDialog.reCreate();
        } catch (Exception ex) {
            ErrorDialog ed = new ErrorDialog(new javax.swing.JFrame(), true, "Address was not removed", ex.getMessage());
            ed.setVisible(true);
        }
    }

    /**
     * @return the iDialog
     */
    public ItemDialog getiDialog() {
        return iDialog;
    }

    /**
     * @param iDialog the iDialog to set
     */
    public void setiDialog(ItemDialog iDialog) {
        this.iDialog = iDialog;
    }
}

class EditPopListener implements ActionListener {

    private ItemDialog iDialog;
    private int idP;
    private String type;

    public EditPopListener(ItemDialog iDialog, int idP, String type) {
        this.iDialog = iDialog;
        this.idP = idP;
        this.type = type;
    }

    public void actionPerformed(ActionEvent e) {

        if (type.equals("address")) {
            final int idPol = idP;
            final ItemDialog iDial = iDialog;
            JMenuItem it = (JMenuItem) e.getSource();
            final int idAdresy = Integer.valueOf(it.getName()).intValue();
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    EditAddressDialog dialog = new EditAddressDialog(new javax.swing.JFrame(), true, idPol, iDial, idAdresy);
                    dialog.setVisible(true);
                }
            });
        } else if (type.equals("email")) {

            final int idPol = idP;
            final ItemDialog iDial = iDialog;
            JMenuItem it = (JMenuItem) e.getSource();
            final int id = Integer.valueOf(it.getName()).intValue();
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    EditEmailDialog dialog = new EditEmailDialog(new javax.swing.JFrame(), true, idPol, iDial, id);
                    dialog.setVisible(true);
                }
            });
        } else if (type.equals("phone")) {

            final int idPol = idP;
            final ItemDialog iDial = iDialog;
            JMenuItem it = (JMenuItem) e.getSource();
            final int id = Integer.valueOf(it.getName()).intValue();
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    EditTelephoneDialog dialog = new EditTelephoneDialog(new javax.swing.JFrame(), true, idPol, iDial, id);
                    dialog.setVisible(true);
                }
            });



        } else if (type.equals("im")) {
            final int idPol = idP;
            final ItemDialog iDial = iDialog;
            JMenuItem it = (JMenuItem) e.getSource();
            final int id = Integer.valueOf(it.getName()).intValue();
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    EditImDialog dialog = new EditImDialog(new javax.swing.JFrame(), true, idPol, iDial, id);
                    dialog.setVisible(true);
                }
            });




        } else if (type.equals("url")) {

            final int idPol = idP;
            final ItemDialog iDial = iDialog;
            JMenuItem it = (JMenuItem) e.getSource();
            final int id = Integer.valueOf(it.getName()).intValue();
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    EditUrlDialog dialog = new EditUrlDialog(new javax.swing.JFrame(), true, idPol, iDial, id);
                    dialog.setVisible(true);
                }
            });

        } else if (type.equals("other")) {

            final int idPol = idP;
            final ItemDialog iDial = iDialog;
            JMenuItem it = (JMenuItem) e.getSource();
            final int id = Integer.valueOf(it.getName()).intValue();
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    EditOtherDialog dialog = new EditOtherDialog(new javax.swing.JFrame(), true, idPol, iDial, id);
                    dialog.setVisible(true);
                }
            });

        }

    }

    /**
     * @return the iDialog
     */
    public ItemDialog getiDialog() {
        return iDialog;
    }

    /**
     * @param iDialog the iDialog to set
     */
    public void setiDialog(ItemDialog iDialog) {
        this.iDialog = iDialog;
    }
}

class CommonPopListener implements ActionListener {

    private ItemDialog iDialog;

    public CommonPopListener(ItemDialog iDialog) {
        this.iDialog = iDialog;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            JMenuItem it = (JMenuItem) e.getSource();
            ContactManager.deleteItem(it.getName());
            iDialog.reCreate();
        } catch (Exception ex) {
            ErrorDialog ed = new ErrorDialog(new javax.swing.JFrame(), true, "Item was not removed", ex.getMessage());
            ed.setVisible(true);
        }
    }

    /**
     * @return the iDialog
     */
    public ItemDialog getiDialog() {
        return iDialog;
    }

    /**
     * @param iDialog the iDialog to set
     */
    public void setiDialog(ItemDialog iDialog) {
        this.iDialog = iDialog;
    }
}

class NewPopupListenerI extends MouseAdapter {

    JPopupMenu popup;

    NewPopupListenerI(JPopupMenu popupMenu) {
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
