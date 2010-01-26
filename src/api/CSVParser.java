/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import classes.Adresa;
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

    public void main(String label, int userID) throws Exception {
        String file = "data.csv";
        CharsetDetector m = new CharsetDetector(file);
        try {
            m.getCharset();
            String charset = m.encoding;
            if (charset.equalsIgnoreCase("windows-1252")) {
                charset = "cp1250";
            }
            File f = new File(file);
            try {
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
                        }
                    }
                }
                String[] header = records.get(0).split(",");
                int addressBookID = DatabaseManager.getAdresarIDbyUserID(userID);
                for (int i = 1; i < records.size(); i++) {
                    processData(records.get(i), header, label, addressBookID);
                }
                //    print(records);

            } catch (IOException io) {
                io.printStackTrace();
                // read with default encoding
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void processData(String records, String[] header, String label, int abID) {
        try {
            records = records.replaceAll("\"", "");
            Polozka contact = new Polozka();
            String[] record = records.split(",");
            String name = record[0] + " " + record[1] + " " + record[2];
            String lastname = record[3] + " " + record[4];
            contact.setJmeno(name);
            contact.setPrijmeni(lastname);
            contact.setIdAdresare(abID);
            contact.setStitek(label);
            if (lastname.length() > 1) {
                contact.setSearchLetter(StringChecker.removeAccents(lastname));
            } else {
                if (name.length() > 2) {
                    contact.setSearchLetter(StringChecker.removeAccents(name));
                } else {
                    contact.setSearchLetter("#");
                }
            }
            int idContact  = ItemsManager.saveItem(contact, abID);

            int length = 0; // "work";
            for (int i = 8; i <= 14; i++) {
                length = length + record[i].length();
            }
            if (length > 0) {
                Adresa a = new Adresa();
                a.setIdPolozky(idContact);
                a.setTyp("work");
                String regex = "\\d+"; //matches one or more digits, might not work with (rank 1)
                Pattern myPattern = Pattern.compile(regex);
                String street = record[8] + " " + record[9] + " " + record[10];
                Matcher myMatcher = myPattern.matcher(street);
                int cp = 0;
                if (myMatcher.find()) {
                    cp = Integer.parseInt(myMatcher.group());
                }
                a.setCp(cp + "");
                a.setUlice(street);
                a.setMesto(record[11]);
                a.setPsc(Integer.valueOf(record[13]).intValue());
                ContactManager.addAddress(a);
            }



                    length = 0; // "work";
            for (int i = 15; i <= 21; i++) {
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
                    cp = Integer.parseInt(myMatcher.group());
                }
                a.setCp(cp + "");
                a.setUlice(street);
                a.setMesto(record[18]);
                a.setPsc(Integer.valueOf(record[20]).intValue());
                ContactManager.addAddress(a);
            }


                                        length = 0; // "work";
            for (int i = 22; i <= 28; i++) {
                length = length + record[i].length();
            }
            if (length > 0) {
                Adresa a = new Adresa();
                a.setIdPolozky(idContact);
                a.setTyp("home");
                String regex = "\\d+"; //matches one or more digits, might not work with (rank 1)
                Pattern myPattern = Pattern.compile(regex);
                String street = record[22] + " " + record[23] + " " + record[24];
                Matcher myMatcher = myPattern.matcher(street);
                int cp = 0;
                if (myMatcher.find()) {
                    cp = Integer.parseInt(myMatcher.group());
                }
                a.setCp(cp + "");
                a.setUlice(street);
                a.setMesto(record[25]);
                a.setPsc(Integer.valueOf(record[27]).intValue());
                ContactManager.addAddress(a);
            }



        } catch (Exception ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
        }

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
