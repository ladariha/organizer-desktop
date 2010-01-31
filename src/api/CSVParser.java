/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import classes.Adresa;
import classes.ObecnyKontakt;
import classes.Polozka;
import hibernate.DatabaseManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.CharsetDetector;
import util.StringChecker;

/**
 *
 * @author Lada Riha
 */
public class CSVParser {

    public ArrayList<Polozka> contacts = new ArrayList<Polozka>();
    public ArrayList<Integer> fieldsToDel = new ArrayList<Integer>();

    /**
     * Imports contacts from csv file
     * @param label label for imported contacts
     * @param userID user id
     * @param file absolute path to the csv file
     * @return number of imported contacts
     * @throws FileNotFoundException
     * @throws IOException
     * @throws Exception
     */
    public int importCsv(String label, int userID, String file) throws FileNotFoundException, IOException, Exception {
        int importovano = 0;
        CharsetDetector m = new CharsetDetector(file);

        m.getCharset();
        String charset = m.encoding;
        if (charset.equalsIgnoreCase("windows-1252")) {
            charset = "cp1250";
        }

        FileInputStream fis = new FileInputStream(new File(file));
        InputStreamReader in = new InputStreamReader(fis, charset);
        BufferedReader b = new BufferedReader(in);
        String line;
        int krok = 0;
        ArrayList<String> records = new ArrayList<String>();
        int headerLength = 0;
        boolean cont = false;
        while (b.ready()) {

            line = b.readLine();

            if (krok == 0) {
                headerLength = line.split(",").length;
                records.add(line);
                krok++;
            } else {
                if (!cont) {
                    int length = line.split(",").length;
                    if (length < headerLength) {

                        cont = true;
                    }
                    records.add(line);
                } else {
                    String tmp = records.get(records.size() - 1);
                    records.set(records.size() - 1, tmp + line);
                    cont = false;
                }
            }
        }
        String[] header = (records.get(0).replaceAll("\"", "")).split(",");
        int addressBookID = DatabaseManager.getAdresarIDbyUserID(userID);
        for (int i = 1; i < records.size(); i++) {

            importovano = importovano + processData(records.get(i), header, label, addressBookID);
        }

        return importovano;
    }

    /**
     * Creates new contact for each record from original csv file. In this file there are some fields (values) that aren't supported by this application (like birthday...).
     * All this detailes will be imported as "other" to organizer so nothing will be lost.
     * @param records one line from csv file (= one business card)
     * @param header first line of csv file
     * @param label  label for new contact
     * @param abID address book id
     * @return 1 if success, 0 othervise
     */
    private int processData(String records, String[] header, String label, int abID) {
        try {
            System.out.println("RR " + records);
            fieldsToDel = new ArrayList<Integer>();
            records = records.replaceAll("\"", "");
            Polozka contact = new Polozka();
            String[] record = records.split(",");
            String name = record[0] + " " + record[1] + " " + record[2];
            String lastname = record[3] + " " + record[4];
            contact.setJmeno(name);
            contact.setPrijmeni(lastname);
            contact.setIdAdresare(abID);
            contact.setStitek(label);
            fieldsToDel.add(0);
            fieldsToDel.add(1);
            fieldsToDel.add(2);
            fieldsToDel.add(3);
            fieldsToDel.add(4);
            if (lastname.length() > 1) {
                contact.setSearchLetter(StringChecker.removeAccents(lastname));
            } else {
                if (name.length() > 2) {
                    contact.setSearchLetter(StringChecker.removeAccents(name));
                } else {
                    contact.setSearchLetter("#");
                }
            }
            int idContact = ItemsManager.saveItem(contact, abID);

            int length = 0; // "work";
            for (int i = 8; i <= 14; i++) {
                length = length + record[i].length();
                fieldsToDel.add(i);
            }
            if (length > 0) {
                Adresa a = new Adresa();
                a.setIdPolozky(idContact);
                a.setTyp("work");
                String regex = "\\d+"; //matches one or more digits, might not work with (rank 1)
                Pattern myPattern = Pattern.compile(regex);
                String street = (record[8] + " " + record[9] + " " + record[10]).trim();
                Matcher myMatcher = myPattern.matcher(street);
                int cp = 0;
                if (myMatcher.find()) {
                    String tmp = myMatcher.group();
                    cp = Integer.parseInt(tmp);
                    street = street.replaceAll(tmp, "");
                }
                a.setCp(cp + "");
                a.setUlice(street);
                a.setMesto(record[11]);
                a.setPsc(Integer.valueOf(record[13]).intValue());
                ContactManager.addAddress(a);
            }



            length = 0; // "work";
            for (int i = 15; i <= 21; i++) {
                fieldsToDel.add(i);
                length = length + record[i].length();
            }
            if (length > 0) {
                Adresa a = new Adresa();
                a.setIdPolozky(idContact);
                a.setTyp("home");
                String regex = "\\d+"; //matches one or more digits, might not work with (rank 1)
                Pattern myPattern = Pattern.compile(regex);
                String street = record[15] + " " + record[16] + " " + record[17];
                Matcher myMatcher = myPattern.matcher(street);
                int cp = 0;
                if (myMatcher.find()) {
                    String tmp = myMatcher.group();
                    cp = Integer.parseInt(tmp);
                    street = street.replaceAll(tmp, "");
                }
                a.setCp(cp + "");
                a.setUlice(street);
                a.setMesto(record[18]);
                a.setPsc(Integer.valueOf(record[20]).intValue());
                ContactManager.addAddress(a);
            }


            length = 0; // "work";
            for (int i = 22; i <= 28; i++) {
                fieldsToDel.add(i);
                length = length + record[i].length();
            }
            if (length > 0) {
                Adresa a = new Adresa();
                a.setIdPolozky(idContact);
                a.setTyp("other");
                String regex = "\\d+"; //matches one or more digits, might not work with (rank 1)
                Pattern myPattern = Pattern.compile(regex);
                String street = record[22] + " " + record[23] + " " + record[24];
                Matcher myMatcher = myPattern.matcher(street);
                int cp = 0;
                if (myMatcher.find()) {
                    String tmp = myMatcher.group();
                    cp = Integer.parseInt(tmp);
                    street = street.replaceAll(tmp, "");
                }
                a.setCp(cp + "");
                a.setUlice(street);
                a.setMesto(record[25]);
                a.setPsc(Integer.valueOf(record[27]).intValue());
                ContactManager.addAddress(a);
            }

            // phones
            for (int i = 29; i <= 47; i++) {
                fieldsToDel.add(i);
                if (record[i].length() > 0) {
                    ObecnyKontakt o = new ObecnyKontakt();
                    o.setIdPolozky(idContact);
                    o.setHodnota(record[i]);
                    o.setTyp("telefon");
                    o.setOznaceni(header[i]);
                    ContactManager.addPhone(o);
                }
            }

            // mail A
            for (int i = 51; i < 60; i++) {
                fieldsToDel.add(i);
            }
            if (record[51].length() > 0) {
                ObecnyKontakt o = new ObecnyKontakt();
                o.setHodnota(record[51]);
                o.setTyp("email");
                try {
                    o.setTyp2(record[51].substring(record[51].indexOf('@') + 1).toUpperCase());
                } catch (Exception e) {
                    o.setTyp2(" ");
                }
                o.setIdPolozky(idContact);
                o.setOznaceni("Email 1");
                ContactManager.addEmail(o);
            }

            if (record[54].length() > 0) {
                ObecnyKontakt o = new ObecnyKontakt();
                o.setHodnota(record[54]);
                try {
                    o.setTyp2(record[54].substring(record[54].indexOf('@') + 1).toUpperCase());
                } catch (Exception e) {
                    o.setTyp2(" ");
                }
                o.setTyp("email");
                o.setIdPolozky(idContact);
                o.setOznaceni("Email 2");
                ContactManager.addEmail(o);
            }


            if (record[57].length() > 0) {
                ObecnyKontakt o = new ObecnyKontakt();
                o.setHodnota(record[57]);
                try {
                    o.setTyp2(record[57].substring(record[57].indexOf('@') + 1).toUpperCase());
                } catch (Exception e) {
                    o.setTyp2(" ");
                }
                o.setTyp("email");
                o.setIdPolozky(idContact);
                o.setOznaceni("Email 3");
                ContactManager.addEmail(o);
            }


            if (record[record.length - 2].length() > 0) {
                fieldsToDel.add(record.length - 2);
                ObecnyKontakt o = new ObecnyKontakt();
                o.setIdPolozky(idContact);
                o.setHodnota(record[record.length - 2]);
                o.setTyp("url");
                ContactManager.addUrl(o);
            }


            for (int i = 0; i < record.length; i++) {
                if (record[i].length() > 0 && !fieldsToDel.contains(i)) {
                    ObecnyKontakt o = new ObecnyKontakt();
                    o.setHodnota(record[i]);
                    o.setTyp("other");
                    o.setIdPolozky(idContact);
                    try {
                        o.setOznaceni(header[i]);
                    } catch (Exception a) {
                        o.setOznaceni("unknown");
                    }
                    ContactManager.addOther(o);
                }


            }
            return 1;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    private static void print(ArrayList<String> records) {

        String[] p = records.get(0).split(",");
        String[] q = records.get(1).split(",");
        int stop = Math.min(p.length, q.length);
        for (int i = 0; i < stop; i++) {
            System.out.println(p[i] + " :  " + q[i]);
        }


    }
}
