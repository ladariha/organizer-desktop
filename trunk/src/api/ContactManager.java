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

    public static void addAddress(Adresa a) {
        DatabaseManager.addAddress(a);
    }

    public static void addPhone(ObecnyKontakt kontakt) {
        DatabaseManager.addObecnyKontakt(kontakt);
    }

    public static void addEmail(ObecnyKontakt kontakt) {
        DatabaseManager.addObecnyKontakt(kontakt);
    }

    public static void addIm(ObecnyKontakt kontakt) {
        DatabaseManager.addObecnyKontakt(kontakt);
    }

    public static void addUrl(ObecnyKontakt kontakt) {
        DatabaseManager.addObecnyKontakt(kontakt);
    }

    public static void addOther(ObecnyKontakt kontakt) {
        DatabaseManager.addObecnyKontakt(kontakt);
    }

    public static void deleteAddress(String id) {
        int idItem = Integer.valueOf(id).intValue();
        DatabaseManager.deleteAddress(idItem);
    }

    public static void deleteItem(String id) {
        int idItem = Integer.valueOf(id).intValue();
        DatabaseManager.deleteCommonContact(idItem);
    }

    public static void saveAddress(Adresa adresa) {
        DatabaseManager.updateAddress(adresa);
    }

    public static Adresa getAddress(int idAdresy) {
        return DatabaseManager.getAddress(idAdresy);
    }

    public static ObecnyKontakt getEmail(int id) {
        return DatabaseManager.getObecnyKontakt(id);
    }

    public static void saveEmail(ObecnyKontakt kontakt) {
        DatabaseManager.updateObecnyKontakt(kontakt);
    }

    public static ObecnyKontakt getPhone(int id) {
        return DatabaseManager.getObecnyKontakt(id);
    }

    public static void savePhone(ObecnyKontakt kontakt) {
        DatabaseManager.updateObecnyKontakt(kontakt);
    }

    public static ObecnyKontakt getIm(int id) {
        return DatabaseManager.getObecnyKontakt(id);
    }

    public static void saveIm(ObecnyKontakt kontakt) {
        DatabaseManager.updateObecnyKontakt(kontakt);
    }

    public static ObecnyKontakt getUrl(int id) {
        return DatabaseManager.getObecnyKontakt(id);

    }

    public static void saveUrl(ObecnyKontakt kontakt) {
        DatabaseManager.updateObecnyKontakt(kontakt);
    }

        public static ObecnyKontakt getOther(int id) {
        return DatabaseManager.getObecnyKontakt(id);

    }

    public static void saveOther(ObecnyKontakt kontakt) {
        DatabaseManager.updateObecnyKontakt(kontakt);
    }

}
