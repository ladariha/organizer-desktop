/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import GUI.*;

/**
 *
 * @author Vladimír Řiha
 */
public class Organizer {

    private int userID;
    private String name;
    private String lastname;
    private String email;
    private String labels;

    public void run() {

        final String t = this.email;
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                System.out.println("IDD je "+userID);
                MainWindow mw = new MainWindow(userID, name, lastname, email);
                mw.setVisible(true);
                mw.setTitle("Organizer Desktop - " + t);
                
            }
        });


    }

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
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
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

    public void setLabels(String labels) {
        this.labels = labels;
    }
}
