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

    /**
     * Add address
     * @param a address to Add
     * @throws Exception
     */
    public static void addAddress(Adresa a) throws Exception {
        DatabaseManager.addAddress(a);
    }

    /**
     * Add phone
     * @param kontakt phone to Add
     * @throws Exception
     */
    public static void addPhone(ObecnyKontakt kontakt) throws Exception {
        DatabaseManager.addObecnyKontakt(kontakt);
    }

    /**
     * Add email
     * @param kontakt email  to Add
     * @throws Exception
     */
    public static void addEmail(ObecnyKontakt kontakt) throws Exception {
        DatabaseManager.addObecnyKontakt(kontakt);
    }

    /**
     * Add im
     * @param kontakt im to Add
     * @throws Exception
     */
    public static void addIm(ObecnyKontakt kontakt) throws Exception {
        DatabaseManager.addObecnyKontakt(kontakt);
    }

    /**
     * Add url
     * @param kontakt url to Add
     * @throws Exception
     */
    public static void addUrl(ObecnyKontakt kontakt) throws Exception {
        DatabaseManager.addObecnyKontakt(kontakt);
    }

    /**
     * Add other
     * @param kontakt other to Add
     * @throws Exception
     */
    public static void addOther(ObecnyKontakt kontakt) throws Exception {
        DatabaseManager.addObecnyKontakt(kontakt);
    }

    /**
 * Deletes address
 * @param id ID of address to be deleted
 * @throws Exception
 */
    public static void deleteAddress(String id) throws Exception {
        int idItem = Integer.valueOf(id).intValue();
        DatabaseManager.deleteAddress(idItem);
    }

    /**
 * Deletes contact
 * @param id ID of contact to be deleted
 * @throws Exception
 */
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
