/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import classes.Adresa;
import classes.ObecnyKontakt;
import hibernate.*;

/**
 *
 * @author Vladimír Řiha
 */
public class ContactManager {

    public static void addAddress(Adresa a) throws Exception {
        DatabaseManager.addAddress(a);
    }

    public static void addPhone(ObecnyKontakt kontakt) throws Exception {
        DatabaseManager.addObecnyKontakt(kontakt);
    }

    public static void addEmail(ObecnyKontakt kontakt) throws Exception {
        DatabaseManager.addObecnyKontakt(kontakt);
    }

    public static void addIm(ObecnyKontakt kontakt) throws Exception {
        DatabaseManager.addObecnyKontakt(kontakt);
    }

    public static void addUrl(ObecnyKontakt kontakt) throws Exception {
        DatabaseManager.addObecnyKontakt(kontakt);
    }

    public static void addOther(ObecnyKontakt kontakt) throws Exception {
        DatabaseManager.addObecnyKontakt(kontakt);
    }

    public static void deleteAddress(String id) throws Exception {
        int idItem = Integer.valueOf(id).intValue();
        DatabaseManager.deleteAddress(idItem);
    }

    public static void deleteItem(String id) throws Exception {
        int idItem = Integer.valueOf(id).intValue();
        DatabaseManager.deleteCommonContact(idItem);
    }

    public static void saveAddress(Adresa adresa) throws Exception {
        DatabaseManager.updateAddress(adresa);
    }

    public static Adresa getAddress(int idAdresy) throws Exception {
        return DatabaseManager.getAddress(idAdresy);
    }

    public static ObecnyKontakt getEmail(int id) throws Exception {
        return DatabaseManager.getObecnyKontakt(id);
    }

    public static void saveEmail(ObecnyKontakt kontakt) throws Exception {
        DatabaseManager.updateObecnyKontakt(kontakt);
    }

    public static ObecnyKontakt getPhone(int id) throws Exception {
        return DatabaseManager.getObecnyKontakt(id);
    }

    public static void savePhone(ObecnyKontakt kontakt) throws Exception {
        DatabaseManager.updateObecnyKontakt(kontakt);
    }

    public static ObecnyKontakt getIm(int id) throws Exception {
        return DatabaseManager.getObecnyKontakt(id);
    }

    public static void saveIm(ObecnyKontakt kontakt) throws Exception {
        DatabaseManager.updateObecnyKontakt(kontakt);
    }

    public static ObecnyKontakt getUrl(int id) throws Exception {
        return DatabaseManager.getObecnyKontakt(id);

    }

    public static void saveUrl(ObecnyKontakt kontakt) throws Exception {
        DatabaseManager.updateObecnyKontakt(kontakt);
    }

        public static ObecnyKontakt getOther(int id) throws Exception {
        return DatabaseManager.getObecnyKontakt(id);

    }

    public static void saveOther(ObecnyKontakt kontakt) throws Exception {
        DatabaseManager.updateObecnyKontakt(kontakt);
    }

}
