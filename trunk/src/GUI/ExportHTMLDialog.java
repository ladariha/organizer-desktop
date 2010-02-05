package GUI;

import api.ItemsManager;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author Vladimír Řiha
 */
public class ExportHTMLDialog extends javax.swing.JDialog {

    /**
     *
     * @param parent
     * @param modal
     * @param idUser
     * @param t1
     * @param t2
     */
    public ExportHTMLDialog(java.awt.Frame parent, boolean modal, int idUser, String t1, String t2) {
        super(parent, modal);
        this.idU = idUser;
        this.jmenoU = t1;
        this.prijmeniU = t2;
        initComponents();
        jFileChooser1.addChoosableFileFilter(new FileNameExtensionFilter("HTML Document (*.html)", "html"));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jFileChooser1 = new javax.swing.JFileChooser();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Export to HTML");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel1.setText("Export contacts to HTML");

        jFileChooser1.setAcceptAllFileFilterUsed(false);
        jFileChooser1.setControlButtonsAreShown(false);
        jFileChooser1.setDialogTitle("");
        jFileChooser1.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        jFileChooser1.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        jButton1.setText("Export");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jFileChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(455, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFileChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Exports contacts to selected file
     * @param evt
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            // TODO add your handling code here:
            ItemsManager.export(this.idU, jFileChooser1.getSelectedFile().getAbsolutePath(), jmenoU, prijmeniU, "html");
                 InfoDialog id = new InfoDialog(new javax.swing.JFrame(), rootPaneCheckingEnabled, "Contacts has been exported", "  ");
                        id.setVisible(true);
                        this.setVisible(false);
        } catch (ParserConfigurationException ex) {
            ErrorDialog ed = new ErrorDialog(new javax.swing.JFrame(), true, "HTML file error", ex.getMessage());
            ed.setVisible(true);
        } catch (TransformerConfigurationException ex) {
            ErrorDialog ed = new ErrorDialog(new javax.swing.JFrame(), true, "HTML file error", ex.getMessage());
            ed.setVisible(true);
        } catch (TransformerException ex) {
            ErrorDialog ed = new ErrorDialog(new javax.swing.JFrame(), true, "HTML file error", ex.getMessage());
            ed.setVisible(true);
        } catch (IOException ex) {
            ErrorDialog ed = new ErrorDialog(new javax.swing.JFrame(), true, "File error ", ex.getMessage());
            ed.setVisible(true);
        } catch (Exception ex) {
            ErrorDialog ed = new ErrorDialog(new javax.swing.JFrame(), true, "Error ", ex.getMessage());
            ed.setVisible(true);
        }
        this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    private int idU;
    private String jmenoU;
    private String prijmeniU;
}
