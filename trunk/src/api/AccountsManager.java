/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import exception.*;
import hibernate.*;
import classes.*;
import java.util.*;
import util.*;

/**
 *
 * @author Vladimír Řiha
 */
public class AccountsManager {

    private int userID;
    private String name;
    private String lastname;
    private String email;

    public boolean registerNewUser(String jmeno, String prijmeni, String email, char[] pole) throws RegisterOrganizerException, Exception {
        int vysledek = DatabaseManager.getUserIdByUsername(email);
        if (vysledek == -1) {

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < pole.length; i++) {
                sb.append(pole[i]);
            }


            Ucet ucet = new Ucet();
            StringChecker sc = new StringChecker();
            ucet.setPassword(sc.getHash(sb.toString() + email));
            ucet.setUsername(email);
            DatabaseManager.saveNewUcet(ucet);

            Uzivatel uzivatel = new Uzivatel();
            uzivatel.setEmail(email);
            uzivatel.setJmeno(jmeno);
            uzivatel.setPrijmeni(prijmeni);
            uzivatel.setUcet(ucet);

            int idVlastnika = ucet.getId();

            Adresar adresar = new Adresar();
            adresar.setIdVlastnika(idVlastnika);
            Set<Polozka> setp = new HashSet<Polozka>();
            adresar.setPolozky(setp);
            DatabaseManager.saveAdresar(adresar);

            int idAdresare = adresar.getId();

            Polozka polozka = new Polozka();
            polozka.setJmeno(jmeno);
            polozka.setPrijmeni(prijmeni);
            //polozka.setIdAdresare(idAdresare);
            polozka.setIdAdresare(idAdresare);
            polozka.setStitek("organizer");
            polozka.setSearchLetter(StringChecker.removeAccents(prijmeni.substring(0, 1).toUpperCase()));
            adresar.getPolozky().add(polozka);


            DatabaseManager.saveNewPolozka(polozka);

//                System.out.println("ID JE "+polozka.getId());
            ObecnyKontakt k1 = new ObecnyKontakt();
            k1.setIdPolozky(polozka.getId());
            k1.setHodnota(email);

            k1.setTyp("email");
            k1.setTyp2(email.substring(email.indexOf('@') + 1).toUpperCase());
            k1.setOznaceni("Hlavní");
            Set<ObecnyKontakt> setk = new HashSet<ObecnyKontakt>();
            setk.add(k1);
            polozka.setKontakty(setk);

            DatabaseManager.saveNewUzivatel(uzivatel);
            DatabaseManager.saveNewKontakt(k1);
            this.setUserID(ucet.getId());
            this.setEmail(email);
            this.setLastname(prijmeni);
            this.setName(jmeno);
            return true;
        } else {
            throw new RegisterOrganizerException("This email address is already registered ");
        }


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
}
