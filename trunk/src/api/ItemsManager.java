/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import classes.*;
import classes.Adresa;
import hibernate.*;
import java.net.MalformedURLException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import util.StringChecker;
import com.google.gdata.client.*;
import com.google.gdata.client.contacts.*;
import com.google.gdata.data.*;
import com.google.gdata.data.contacts.*;
import com.google.gdata.data.extensions.*;
import com.google.gdata.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Vladimír Řiha
 */
public class ItemsManager {

    public static void deleteItemsByLetter(Set<String> lettersToDelete, int idU) throws Exception {
        DatabaseManager.deleteItemsByLetter(lettersToDelete, idU);
    }

    public static void deleteItemsByLabel(Set<String> labelsToDelete, int idU) throws Exception {
        DatabaseManager.deleteItemsByLabel(labelsToDelete, idU);
    }

    public static List getItemsByStringSearch(int userID, String search) throws Exception {
        return DatabaseManager.getItemsByStringSearch(userID, search);
    }

    public static List getItemsByStringSearchLabel(int userID, String search) throws Exception {
        return DatabaseManager.getItemsByStringSearchLabel(userID, search);
    }

    public static List getItemsByID(int id, String letter, Set<String> hiddenLabels) throws Exception {

        return DatabaseManager.getItemsByID(id, letter, hiddenLabels);
    }

    public static String getLabelByID(int id) throws Exception {

        return DatabaseManager.getLabelByID(id);
    }

    public static Polozka getFullItemByID(int idP, int idU) throws Exception {
        int idAdresare = DatabaseManager.getAdresarIDbyUserID(idU);
        Polozka polozka = DatabaseManager.getItemsByID(idP, idAdresare);
        return polozka;
    }

    public static void deleteItems(int idU) throws Exception {

        int idAdresare = DatabaseManager.getAdresarIDbyUserID(idU);
        DatabaseManager.deleteAddressBook(idAdresare);
        DatabaseManager.resetLabelList(idU);

    }

    public static int countItems(String type) {

        return DatabaseManager.getContactsCount(type);
    }

    public static void deleteItem(int idP, int idU) throws Exception {
        int idAdresare = DatabaseManager.getAdresarIDbyUserID(idU);
        int idAdresare2 = DatabaseManager.getAdresarIDbyItemID(idP);
        if (idAdresare == idAdresare2) {
            DatabaseManager.deleteItem(idP);
        }
    }

    public static String updateItem(int idP, int idU, String jmeno, String prijmeni, String label) throws Exception {
        int idAdresare = DatabaseManager.getAdresarIDbyUserID(idU);
        int idAdresare2 = DatabaseManager.getAdresarIDbyItemID(idP);
        if (idAdresare == idAdresare2) {
            DatabaseManager.updateItem(idP, jmeno, prijmeni, label);
            DatabaseManager.updateLabelList(idU);
        }
        return (StringChecker.removeAccents(prijmeni)).substring(0, 1);
    }

    public static int countItems(int userID) throws Exception {
        int archivId = DatabaseManager.getAdresarIDbyUserID(userID);
        return DatabaseManager.getItemsCount(archivId);
    }

    public static String saveItem(int idUser, String jmeno, String prijmeni, String stitek) throws Exception {
        int idAdresare = DatabaseManager.getAdresarIDbyUserID(idUser);
        if (stitek.equals("")) {
            DatabaseManager.saveItem(jmeno, prijmeni, idAdresare, "organizer");
        } else {
            DatabaseManager.saveItem(jmeno, prijmeni, idAdresare, stitek);
        }
        DatabaseManager.updateLabelList(idUser);
        return (StringChecker.removeAccents(prijmeni)).substring(0, 1);
    }

    public static int saveItem(Polozka p, int idA) throws Exception {
        return DatabaseManager.saveItem(p, idA);
    }

    public static int importGmail(String username, char[] passwordChar, int idUser, String label) throws AuthenticationException, ServiceException, IOException, Exception {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < passwordChar.length; i++) {
            sb.append(passwordChar[i]);
        }
        String password = sb.toString();
        ContactsService myService = new ContactsService("Organizer Desktop aplha");
        myService.setUserCredentials(username, password);
        return importGContacts(myService, username, idUser, label);
    }

    public static void export(int userID, String cesta, String jmenoU, String prijmeniU, String format) throws ParserConfigurationException, TransformerConfigurationException, TransformerException, IOException, Exception {

        if (format.equalsIgnoreCase("xml")) {
            List<Polozka> polozky = DatabaseManager.getItemsByID(userID);
            exportXML(userID, cesta, jmenoU, prijmeniU, polozky);
        } else if (format.equalsIgnoreCase("html")) {
            exportHTML(userID, cesta, jmenoU, prijmeniU);
        } else if (format.equalsIgnoreCase("csv")) {
            List<Polozka> polozky = DatabaseManager.getItemsByID(userID);
            exportCSV(userID, cesta, jmenoU, prijmeniU, polozky);
        }
    }

    private static void exportHTML(int userID, String cesta, String jmenoU, String prijmeniU) throws IOException, Exception {
        List<Polozka> polozky = DatabaseManager.getItemsByID(userID);

        if (polozky != null && polozky.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><head></head><body>");
            sb.append(System.getProperty("line.separator"));

            for (int i = 0; i < polozky.size(); i++) {
                try {
                    Polozka p = polozky.get(i);
                    sb.append(" <h1 class=\"blueH\">" + p.getJmeno() + " " + p.getPrijmeni() + "</h1>");
                    sb.append("  <div class=\"left_main\">");
                    Set<Adresa> adr = p.getAdresy();
                    if (adr.size() > 0) {
                        sb.append(System.getProperty("line.separator"));
                        sb.append("<h3>Adresa</h3>");
                        Iterator<Adresa> it = adr.iterator();
                        while (it.hasNext()) {
                            Adresa a = it.next();
                            sb.append("<div class=\"top2\"></div><div class=\"contact\"><div class=\"contactIn\">");
                            sb.append(System.getProperty("line.separator"));
                            sb.append("<span class=\"italic\">" + a.getTyp() + "</span><br/>");
                            sb.append(System.getProperty("line.separator"));
                            sb.append(a.getUlice() + " " + a.getCp() + " <br/>");
                            sb.append(System.getProperty("line.separator"));
                            sb.append(a.getPsc() + " " + a.getMesto());
                            sb.append(System.getProperty("line.separator"));
                            sb.append("</div></div><div class=\"down2\"></div>");
                        }
                    }

                    Set kontakty = p.getKontakty();
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

                    if (mailyA.size() > 0) {
                        sb.append(System.getProperty("line.separator"));
                        sb.append("<h3>Email</h3>");

                        for (int j = 0; j < mailyA.size(); j++) {
                            ObecnyKontakt o = mailyA.get(j);
                            sb.append("<div class=\"top2\"></div><div class=\"contact\"><div class=\"contactIn\">");
                            sb.append(System.getProperty("line.separator"));
                            sb.append(" <table class=\"cc\"><thead><tr><td></td><td></td></tr></thead><tbody>");
                            sb.append(System.getProperty("line.separator"));
                            sb.append("<tr><td><span class=\"italic\">" + o.getOznaceni() + "</span>: </td> <td>Služba:</td></tr>");
                            sb.append(System.getProperty("line.separator"));
                            sb.append("<tr> <td><a href=\"mailto:" + o.getHodnota() + "\">" + o.getHodnota() + "</a></td><td>" + o.getTyp2() + "</td></tr></tbody></table>");
                            sb.append(System.getProperty("line.separator"));
                            sb.append("</div></div><div class=\"down2\"></div>");
                        }
                    }

                    if (urlsA.size() > 0) {
                        sb.append("<h3>URL adresa</h3>");
                        for (int j = 0; j < urlsA.size(); j++) {
                            ObecnyKontakt o = urlsA.get(j);
                            sb.append("<div class=\"top2\"></div><div class=\"contact\"><div class=\"contactIn\">");
                            sb.append(System.getProperty("line.separator"));
                            sb.append(" <table class=\"cc\"><thead><tr><td></td></tr></thead><tbody>");
                            sb.append(System.getProperty("line.separator"));
                            sb.append("<tr><td><span class=\"italic\">" + o.getTyp2() + " - " + o.getOznaceni() + "</span>: </td></tr>");
                            sb.append(System.getProperty("line.separator"));
                            sb.append("<tr> <td><a href=\"" + o.getHodnota() + "\">" + o.getHodnota() + "</a></td></tr></tbody></table>");
                            sb.append(System.getProperty("line.separator"));
                            sb.append("</div></div><div class=\"down2\"></div>");
                        }
                    }
                    sb.append("</div><div class=\"right_main\">");

                    if (telefonyA.size() > 0) {
                        sb.append("<h3>Telefony</h3>");

                        for (int j = 0; j < telefonyA.size(); j++) {
                            ObecnyKontakt o = telefonyA.get(j);
                            sb.append("<div class=\"top2\"></div><div class=\"contact\"><div class=\"contactIn\">");
                            sb.append(System.getProperty("line.separator"));
                            sb.append(" <table class=\"cc\"><thead><tr><td></td></tr></thead><tbody>");
                            sb.append(System.getProperty("line.separator"));
                            sb.append("<tr><td><span class=\"italic\">" + o.getOznaceni() + "</span>: </td></tr>");
                            sb.append(System.getProperty("line.separator"));
                            sb.append("<tr> <td>" + o.getHodnota() + "</td></tr></tbody></table>");
                            sb.append(System.getProperty("line.separator"));
                            sb.append("</div></div><div class=\"down2\"></div>");
                        }
                    }

                    if (imsA.size() > 0) {
                        sb.append("<h3>IM kontakty</h3>");

                        for (int j = 0; j < imsA.size(); j++) {
                            ObecnyKontakt o = imsA.get(j);
                            sb.append("<div class=\"top2\"></div><div class=\"contact\"><div class=\"contactIn\">");
                            sb.append(System.getProperty("line.separator"));
                            sb.append(" <table class=\"cc\"><thead><tr><td></td></tr></thead><tbody>");
                            sb.append(System.getProperty("line.separator"));
                            sb.append("<tr><td><span class=\"italic\">" + o.getTyp2() + " - " + o.getOznaceni() + "</span>: </td></tr>");
                            sb.append(System.getProperty("line.separator"));
                            sb.append("<tr> <td>" + o.getHodnota() + "</td></tr></tbody></table>");
                            sb.append(System.getProperty("line.separator"));
                            sb.append("</div></div><div class=\"down2\"></div>");
                        }
                    }

                    if (otherA.size() > 0) {
                        sb.append("<h3>Ostatní</h3>");

                        for (int j = 0; j < otherA.size(); j++) {
                            ObecnyKontakt o = otherA.get(j);
                            sb.append("<div class=\"top2\"></div><div class=\"contact\"><div class=\"contactIn\">");
                            sb.append(System.getProperty("line.separator"));
                            sb.append(" <table class=\"cc\"><thead><tr><td></td></tr></thead><tbody>");
                            sb.append(System.getProperty("line.separator"));
                            sb.append("<tr><td><span class=\"italic\">" + o.getOznaceni() + "</span>: </td></tr>");
                            sb.append(System.getProperty("line.separator"));
                            sb.append("<tr> <td>" + o.getHodnota() + "</td></tr></tbody></table>");
                            sb.append(System.getProperty("line.separator"));
                            sb.append("</div></div><div class=\"down2\"></div>");
                        }
                    }
                } catch (Exception a) {
                    a.printStackTrace();
                }

            }
            sb.append("</div></body></html>");
            String t = sb.toString();
            File file = new File(cesta + System.getProperty("file.separator") + "organizer.html");
            Writer output = new BufferedWriter(new FileWriter(file, false));
            output.write(t);
            output.close();
        }
    }

    private static void exportXML(int userID, String cesta, String jmenoU, String prijmeniU, List<Polozka> polozky) throws ParserConfigurationException, TransformerConfigurationException, TransformerException, IOException {

        if (polozky != null && polozky.size() > 0) {
            int idAdresare = polozky.get(0).getIdAdresare();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;

            builder = dbf.newDocumentBuilder();
            DOMImplementation impl = builder.getDOMImplementation();
            Document doc = impl.createDocument(null, null, null);

            Element root = doc.createElement("organizer");
            StringChecker sc = new StringChecker();
            String jmenoNoAscii = sc.removeAscii(jmenoU);
            String prijmeniNoAscii = sc.removeAscii(prijmeniU);

            root.setAttribute("jmenoUzivatele", jmenoNoAscii);
            root.setAttribute("prijmeniUzivatele", prijmeniNoAscii);
            doc.appendChild(root);

            Element adresar = doc.createElement("adresar");
            root.appendChild(adresar);
            adresar.setAttribute("id", idAdresare + "");
            adresar.setAttribute("idVlastnika", userID + "");

            Polozka p;
            Element polozka;
            Element jmeno;
            Element prijmeni;
            Element adresy;
            Element emaily;
            Element telefony;
            Element imKontakty;
            Element urlAdresy;
            Element ostatni;

            for (int i = 0; i < polozky.size(); i++) {
                try {
                    p = polozky.get(i);
                    polozka = doc.createElement("polozka");
                    adresar.appendChild(polozka);
                    polozka.setAttribute("id", p.getId() + "");
                    polozka.setAttribute("stitek", p.getStitek() + "");
                    polozka.setAttribute("pismeno", p.getSearchLetter());
                    jmeno = doc.createElement("jmeno");
                    jmeno.setTextContent(p.getJmeno());
                    polozka.appendChild(jmeno);

                    prijmeni = doc.createElement("prijmeni");
                    prijmeni.setTextContent(p.getPrijmeni());
                    polozka.appendChild(prijmeni);

                    Set<Adresa> adr = p.getAdresy();
                    if (adr.size() > 0) {
                        adresy = doc.createElement("adresy");
                        polozka.appendChild(adresy);
                        Iterator<Adresa> it = adr.iterator();
                        while (it.hasNext()) {
                            Adresa a = it.next();
                            Element adresa = doc.createElement("adresa");
                            adresa.setAttribute("oznaceni", a.getTyp());
                            adresy.appendChild(adresa);

                            Element ulice = doc.createElement("ulice");
                            ulice.setTextContent(a.getUlice());
                            adresa.appendChild(ulice);

                            Element cisloPop = doc.createElement("cisloPopisne");
                            cisloPop.setTextContent(a.getCp());
                            adresa.appendChild(cisloPop);

                            Element mesto = doc.createElement("mesto");
                            mesto.setTextContent(a.getMesto());
                            adresa.appendChild(mesto);

                            Element psc = doc.createElement("psc");
                            psc.setTextContent(a.getPsc() + "");
                            adresa.appendChild(psc);
                        }
                    }

                    Set kontakty = p.getKontakty();
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

                    if (mailyA.size() > 0) {
                        emaily = doc.createElement("emaily");
                        polozka.appendChild(emaily);

                        for (int j = 0; j < mailyA.size(); j++) {
                            ObecnyKontakt o = mailyA.get(j);
                            Element email = doc.createElement("email");
                            email.setAttribute("oznaceni", o.getOznaceni());
                            emaily.appendChild(email);

                            Element emailAdresa = doc.createElement("emailAdresa");
                            emailAdresa.setTextContent(o.getHodnota());
                            email.appendChild(emailAdresa);

                            Element sluzba = doc.createElement("sluzba");
                            sluzba.setTextContent(o.getTyp2());
                            email.appendChild(sluzba);
                        }
                    }

                    if (telefonyA.size() > 0) {
                        telefony = doc.createElement("telefony");
                        polozka.appendChild(telefony);

                        for (int j = 0; j < telefonyA.size(); j++) {
                            ObecnyKontakt o = telefonyA.get(j);
                            Element telefon = doc.createElement("telefon");
                            telefon.setAttribute("oznaceni", o.getOznaceni());
                            telefony.appendChild(telefon);

                            Element cislo = doc.createElement("cislo");
                            cislo.setTextContent(o.getHodnota());
                            telefon.appendChild(cislo);
                        }
                    }
                    if (imsA.size() > 0) {
                        imKontakty = doc.createElement("imKontakty");
                        polozka.appendChild(imKontakty);

                        for (int j = 0; j < imsA.size(); j++) {
                            ObecnyKontakt o = imsA.get(j);
                            Element imKontakt = doc.createElement("imKontakt");
                            imKontakt.setAttribute("oznaceni", o.getOznaceni());
                            imKontakt.setAttribute("protokol", o.getTyp2());

                            Element kontakt = doc.createElement("kontakt");
                            kontakt.setTextContent(o.getHodnota());
                            imKontakt.appendChild(kontakt);
                            imKontakty.appendChild(imKontakt);
                        }
                    }

                    if (urlsA.size() > 0) {
                        urlAdresy = doc.createElement("urlAdresy");
                        polozka.appendChild(urlAdresy);

                        for (int j = 0; j < urlsA.size(); j++) {
                            ObecnyKontakt o = urlsA.get(j);
                            Element urlAdresa = doc.createElement("urlAdresa");
                            urlAdresa.setAttribute("oznaceni", o.getOznaceni());
                            Element url = doc.createElement("url");
                            url.setTextContent(o.getHodnota());
                            urlAdresa.appendChild(url);
                            urlAdresy.appendChild(urlAdresa);
                        }
                    }

                    if (otherA.size() > 0) {
                        ostatni = doc.createElement("ostatni");
                        polozka.appendChild(ostatni);

                        for (int j = 0; j < otherA.size(); j++) {
                            ObecnyKontakt o = otherA.get(j);
                            Element poznamka = doc.createElement("poznamka");
                            poznamka.setAttribute("oznaceni", o.getOznaceni());
                            Element text = doc.createElement("text");
                            text.setTextContent(o.getHodnota());
                            poznamka.appendChild(text);
                            ostatni.appendChild(poznamka);
                        }
                    }
                } catch (Exception v) {
                    v.printStackTrace();
                }
            }

            StringWriter sw = new StringWriter();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://www.ladariha.cz/file/organizer/organizer.dtd");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(sw);
            transformer.transform(source, result);
            String s = sw.toString();

            File file = new File(cesta + System.getProperty("file.separator") + "organizer.xml");
            Writer output = new BufferedWriter(new FileWriter(file, false));
            //       boolean neww = file.createNewFile();
            output.write(s);
            output.close();
        }
    }

    public static int importGContacts(
            ContactsService myService, String username, int idUser, String label) throws ServiceException,
            IOException,
            Exception {

        URL feedUrl = new URL("http://www.google.com/m8/feeds/contacts/" + username + "/full");
        Query myQuery = new Query(feedUrl);
        myQuery.setMaxResults(3000);
        com.google.gdata.data.contacts.ContactFeed resultFeed = myService.query(myQuery, com.google.gdata.data.contacts.ContactFeed.class);

        int importovano = 0;
        int idAdresare = DatabaseManager.getAdresarIDbyUserID(idUser);
        for (int i = 0; i < resultFeed.getEntries().size(); i++) {


            try {
                com.google.gdata.data.contacts.ContactEntry entry = resultFeed.getEntries().get(i);
                Name name = entry.getName();
                String jmeno = entry.getName().getFullName().getValue().trim();

                if (jmeno.length() > 0) {
                    Polozka polozka = new Polozka();
                    polozka.setIdAdresare(idAdresare);
                    polozka.setStitek(label);

                    try {
                        if (jmeno.indexOf(" ") != -1) {
                            String t1 = jmeno.substring(0, jmeno.indexOf(" "));
                            if (t1 == null || t1.length() == 0) {
                                t1 = " ";
                            }
                            polozka.setJmeno(t1);
                            String t2 = jmeno.substring(jmeno.indexOf(" ") + 1);
                            if (t2 == null || t2.length() == 0) {
                                t2 = " ";
                            }
                            polozka.setPrijmeni(t2);
                            t2 = StringChecker.removeAccents(t2).trim();
                            t1 = StringChecker.removeAccents(t1).trim();
                            try {
                                polozka.setSearchLetter(t2.charAt(0) + "");
                            } catch (Exception e) {
                                try {
                                    polozka.setSearchLetter(t1.charAt(0) + "");
                                } catch (Exception ee) {
                                    polozka.setSearchLetter("A");
                                }
                            }
                        } else {
                            polozka.setPrijmeni(jmeno);
                            polozka.setJmeno(" ");
                            try {
                                polozka.setSearchLetter(StringChecker.removeAccents(jmeno).trim().charAt(0) + "");
                            } catch (Exception exa) {
                                polozka.setSearchLetter("A");
                            }
                        }
                    } catch (Exception g) {
                        polozka.setPrijmeni(jmeno);
                        polozka.setJmeno(" ");
                        jmeno = StringChecker.removeAccents(jmeno);
                        try {
                            polozka.setSearchLetter(jmeno.trim().charAt(0) + "");
                        } catch (Exception exa) {
                            polozka.setSearchLetter("#");
                        }
                        g.printStackTrace();
                    }

                    int idPolozky = saveItem(polozka, idAdresare);
                    if (idPolozky != -1) {
                        try {
                            List<StructuredPostalAddress> adressy = entry.getStructuredPostalAddresses();
                            if (adressy != null) {

                                for (int j = 0; j < adressy.size(); j++) {
                                    try {
                                        StructuredPostalAddress adresa = adressy.get(j);
                                        Adresa adr = new Adresa();
                                        adr.setIdPolozky(idPolozky);
                                        try {
                                            adr.setTyp(adresa.getRel().substring(adresa.getRel().indexOf('#') + 1));
                                        } catch (Exception ex) {
                                            try {
                                                adr.setTyp(adresa.getLabel());
                                            } catch (Exception e) {
                                                adr.setTyp("Neurčeno");
                                            }
                                        }

                                        try {
                                            String text = adresa.getFormattedAddress().getValue();
                                            BufferedReader bf = new BufferedReader(new StringReader((text)));
                                            String radek;
                                            int krok = 1;
                                            while ((radek = bf.readLine()) != null) {

                                                if (radek.length() > 0) {
                                                    if (krok == 1) {
                                                        int lastindex = radek.lastIndexOf(' ');
                                                        String ulice = radek.substring(0, lastindex);
                                                        String cp = radek.substring(lastindex);
                                                        adr.setUlice(ulice);
                                                        adr.setCp(cp);

                                                    } else if (krok == 2) {
                                                        adr.setMesto(radek);
                                                    } else if (krok == 3) {
                                                        try {
                                                            radek = radek.replaceAll(" ", "");
                                                            adr.setPsc(Integer.valueOf(radek).intValue());
                                                        } catch (Exception e) {
                                                            adr.setPsc(0);
                                                        }
                                                    }
                                                }
                                                krok++;
                                            }
                                            ContactManager.addAddress(adr);

                                        } catch (Exception ex) {
                                            adr.setTyp("Neurčen");
                                        }
                                    } catch (Exception ad) {
                                        ad.printStackTrace();
                                    }
                                }
                            }

                            List<Email> maily = entry.getEmailAddresses();
                            ObecnyKontakt ob = new ObecnyKontakt();
                            if (maily != null) {
                                for (int k = 0; k < maily.size(); k++) {
                                    try {
                                        Email mail = maily.get(k);
                                        if (mail.getAddress().length() > 0) {
                                            ob.setIdPolozky(idPolozky);
                                            ob.setTyp("email");
                                            ob.setHodnota(mail.getAddress());
                                            try {
                                                ob.setOznaceni(mail.getRel().substring(mail.getRel().indexOf('#') + 1));
                                            } catch (Exception ex) {

                                                try {
                                                    ob.setOznaceni(mail.getLabel());
                                                } catch (Exception e) {
                                                    ob.setOznaceni("Neurčen");
                                                }
                                            }

                                            try {
                                                ob.setTyp2((mail.getAddress().substring(mail.getAddress().indexOf('@') + 1).toUpperCase()));
                                            } catch (Exception ex) {
                                                ob.setTyp2("");
                                            }
                                            ContactManager.addEmail(ob);
                                        }
                                    } catch (Exception ad) {
                                        ad.printStackTrace();
                                    }
                                }
                            }
                            ob = new ObecnyKontakt();
                            List<PhoneNumber> cisla = entry.getPhoneNumbers();
                            if (cisla != null) {
                                for (int l = 0; l < cisla.size(); l++) {
                                    PhoneNumber cislo = cisla.get(l);
                                    if (cislo.getPhoneNumber().length() > 0) {
                                        ob.setIdPolozky(idPolozky);
                                        ob.setTyp("telefon");
                                        ob.setHodnota(cislo.getPhoneNumber());
                                        try {
                                            ob.setOznaceni(cislo.getRel().substring(cislo.getRel().indexOf('#') + 1));
                                        } catch (Exception ad) {
                                            try {
                                                ob.setOznaceni(cislo.getLabel());
                                            } catch (Exception e) {
                                                ob.setOznaceni("Neurčeno");
                                            }
                                        }
                                        ContactManager.addPhone(ob);
                                    }
                                }
                            }

                            ob = new ObecnyKontakt();
                            List<Im> ims = entry.getImAddresses();
                            if (ims != null) {
                                for (int m = 0; m < ims.size(); m++) {
                                    try {
                                        Im im = ims.get(m);
                                        if (im.getAddress().length() > 0) {
                                            ob.setIdPolozky(idPolozky);
                                            ob.setTyp("im");
                                            ob.setHodnota(im.getAddress());

                                            try {
                                                ob.setOznaceni(im.getRel().substring(im.getRel().indexOf('#') + 1));
                                            } catch (Exception ad) {
                                                try {
                                                    ob.setOznaceni(im.getLabel());
                                                } catch (Exception e) {
                                                    ob.setOznaceni("Neurčen");
                                                }
                                            }

                                            try {
                                                ob.setTyp2(im.getProtocol().toString().substring(im.getProtocol().toString().indexOf('#') + 1));
                                            } catch (Exception ad) {

                                                try {
                                                    ob.setTyp2(im.getProtocol());
                                                } catch (Exception e) {
                                                    ob.setTyp2("Neurčen");
                                                }
                                            }
                                            ContactManager.addIm(ob);
                                        }

                                    } catch (Exception ad) {
                                        ad.printStackTrace();
                                    }
                                }
                            }

                            ob = new ObecnyKontakt();
                            List<Website> urls = entry.getWebsites();
                            if (urls != null) {
                                for (int n = 0; n < urls.size(); n++) {
                                    try {
                                        Website web = urls.get(n);
                                        if (web.getHref().length() > 0) {
                                            ob.setIdPolozky(idPolozky);
                                            ob.setTyp("url");
                                            ob.setHodnota(web.getHref());

                                            try {
                                                ob.setOznaceni(web.getRel().toString().substring(web.getRel().toString().indexOf('#') + 1));
                                            } catch (Exception ad) {
                                                try {
                                                    ob.setOznaceni(web.getLabel());
                                                } catch (Exception e) {
                                                    ob.setOznaceni("Neurčen");
                                                }
                                            }
                                            ContactManager.addUrl(ob);
                                        }
                                    } catch (Exception ad) {
                                        ad.printStackTrace();
                                    }
                                }
                            }
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                    }
                    importovano++;
                }
            } catch (Exception ew) {
            }

        }
        DatabaseManager.updateLabelList(idUser);
        return importovano;
    }

    public static ArrayList<String> getGmailGroups(String username, char[] passwordChar) throws ServiceException, MalformedURLException, IOException {
        ArrayList<String> groups = new ArrayList<String>();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < passwordChar.length; i++) {
            sb.append(passwordChar[i]);
        }
        String password = sb.toString();

        ContactsService myService = new ContactsService("Organizer Desktop beta");
        myService.setUserCredentials(username, password);
        URL feedUrl = new URL("http://www.google.com/m8/feeds/groups/" + username + "/full");
        ContactGroupFeed resultFeed = myService.getFeed(feedUrl, ContactGroupFeed.class);

        for (int i = 0; i < resultFeed.getEntries().size(); i++) {
            ContactGroupEntry groupEntry = resultFeed.getEntries().get(i);
            groups.add(groupEntry.getTitle().getPlainText());
        }
        return groups;
    }

    public static ArrayList<String> getGmailGroupsID(String username, char[] passwordChar) throws ServiceException, MalformedURLException, IOException {
        ArrayList<String> groups = new ArrayList<String>();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < passwordChar.length; i++) {
            sb.append(passwordChar[i]);
        }
        String password = sb.toString();
        ContactsService myService = new ContactsService("Organizer Desktop beta");
        myService.setUserCredentials(username, password);
        URL feedUrl = new URL("http://www.google.com/m8/feeds/groups/" + username + "/full");
        ContactGroupFeed resultFeed = myService.getFeed(feedUrl, ContactGroupFeed.class);

        for (int i = 0; i < resultFeed.getEntries().size(); i++) {
            ContactGroupEntry groupEntry = resultFeed.getEntries().get(i);
            System.out.println("IDGroup " + groupEntry.getId());
            groups.add(groupEntry.getId());
        }
        return groups;
    }

    public static int importGmail(String username, char[] passwordChar, int idU, String string, String group) throws AuthenticationException, IOException, ServiceException, Exception {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < passwordChar.length; i++) {
            sb.append(passwordChar[i]);
        }
        String password = sb.toString();

        ContactsService myService = new ContactsService("Organizer Desktop beta");
        myService.setUserCredentials(username, password);
        URL feedUrl = new URL("http://www.google.com/m8/feeds/contacts/" + username + "/full");
        Query myQuery = new Query(feedUrl);
        myQuery.setMaxResults(3000);
        myQuery.setStringCustomParameter("group", group);
        com.google.gdata.data.contacts.ContactFeed resultFeed = myService.query(myQuery, com.google.gdata.data.contacts.ContactFeed.class);

        int importovano = 0;
        int idAdresare = DatabaseManager.getAdresarIDbyUserID(idU);
        for (int i = 0; i < resultFeed.getEntries().size(); i++) {

            com.google.gdata.data.contacts.ContactEntry entry = resultFeed.getEntries().get(i);
            Name name = entry.getName();

            try {
                String jmeno = entry.getName().getFullName().getValue().trim();
                if (jmeno.length() > 0) {
                    Polozka polozka = new Polozka();
                    polozka.setIdAdresare(idAdresare);
                    polozka.setStitek(string);

                    try {
                        if (jmeno.indexOf(" ") != -1) {
                            String t1 = jmeno.substring(0, jmeno.indexOf(" "));
                            if (t1 == null || t1.length() == 0) {
                                t1 = " ";
                            }
                            polozka.setJmeno(t1);
                            String t2 = jmeno.substring(jmeno.indexOf(" ") + 1);
                            if (t2 == null || t2.length() == 0) {
                                t2 = " ";
                            }
                            polozka.setPrijmeni(t2);
                            t2 = StringChecker.removeAccents(t2).trim();
                            t1 = StringChecker.removeAccents(t1).trim();
                            try {
                                polozka.setSearchLetter(t2.charAt(0) + "");
                            } catch (Exception e) {
                                try {
                                    polozka.setSearchLetter(t1.charAt(0) + "");
                                } catch (Exception ee) {
                                    polozka.setSearchLetter("A");
                                }
                            }
                        } else {
                            polozka.setPrijmeni(jmeno);
                            polozka.setJmeno(" ");
                            try {
                                polozka.setSearchLetter(StringChecker.removeAccents(jmeno).trim().charAt(0) + "");
                            } catch (Exception exa) {
                                polozka.setSearchLetter("A");
                            }
                        }
                    } catch (Exception g) {
                        polozka.setPrijmeni(jmeno);
                        polozka.setJmeno(" ");
                        jmeno = StringChecker.removeAccents(jmeno);
                        try {
                            polozka.setSearchLetter(jmeno.trim().charAt(0) + "");
                        } catch (Exception exa) {
                            polozka.setSearchLetter("#");
                        }
                        g.printStackTrace();
                    }

                    int idPolozky = saveItem(polozka, idAdresare);
                    if (idPolozky != -1) {
                        try {
                            List<StructuredPostalAddress> adressy = entry.getStructuredPostalAddresses();
                            if (adressy != null) {

                                for (int j = 0; j < adressy.size(); j++) {
                                    try {
                                        StructuredPostalAddress adresa = adressy.get(j);
                                        Adresa adr = new Adresa();
                                        adr.setIdPolozky(idPolozky);
                                        try {
                                            adr.setTyp(adresa.getRel().substring(adresa.getRel().indexOf('#') + 1));
                                        } catch (Exception ex) {
                                            try {
                                                adr.setTyp(adresa.getLabel());
                                            } catch (Exception e) {
                                                adr.setTyp("Neurčeno");
                                            }
                                        }

                                        try {
                                            String text = adresa.getFormattedAddress().getValue();
                                            BufferedReader bf = new BufferedReader(new StringReader((text)));
                                            String radek;
                                            int krok = 1;
                                            while ((radek = bf.readLine()) != null) {

                                                if (radek.length() > 0) {
                                                    if (krok == 1) {
                                                        // ulice CP
                                                        int lastindex = radek.lastIndexOf(' ');
                                                        String ulice = radek.substring(0, lastindex);
                                                        String cp = radek.substring(lastindex);
                                                        adr.setUlice(ulice);
                                                        adr.setCp(cp);

                                                    } else if (krok == 2) {
                                                        adr.setMesto(radek);
                                                    } else if (krok == 3) {
                                                        try {
                                                            radek = radek.replaceAll(" ", "");
                                                            adr.setPsc(Integer.valueOf(radek).intValue());
                                                        } catch (Exception e) {
                                                            adr.setPsc(0);
                                                        }
                                                    }
                                                }
                                                krok++;
                                            }
                                            ContactManager.addAddress(adr);
                                        } catch (Exception ex) {
                                            adr.setTyp("Neurčen");
                                        }
                                    } catch (Exception ad) {
                                    }
                                }
                            }

                            List<Email> maily = entry.getEmailAddresses();
                            ObecnyKontakt ob = new ObecnyKontakt();
                            if (maily != null) {
                                for (int k = 0; k < maily.size(); k++) {
                                    try {
                                        Email mail = maily.get(k);
                                        if (mail.getAddress().length() > 0) {
                                            ob.setIdPolozky(idPolozky);
                                            ob.setTyp("email");
                                            ob.setHodnota(mail.getAddress());
                                            try {
                                                ob.setOznaceni(mail.getRel().substring(mail.getRel().indexOf('#') + 1));
                                            } catch (Exception ex) {

                                                try {
                                                    ob.setOznaceni(mail.getLabel());
                                                } catch (Exception e) {
                                                    ob.setOznaceni("Neurčen");
                                                }
                                            }

                                            try {
                                                ob.setTyp2((mail.getAddress().substring(mail.getAddress().indexOf('@') + 1).toUpperCase()));
                                            } catch (Exception ex) {
                                                ob.setTyp2("");
                                            }
                                            ContactManager.addEmail(ob);
                                        }
                                    } catch (Exception ad) {
                                    }
                                }
                            }
                            ob = new ObecnyKontakt();
                            List<PhoneNumber> cisla = entry.getPhoneNumbers();
                            if (cisla != null) {
                                for (int l = 0; l < cisla.size(); l++) {
                                    PhoneNumber cislo = cisla.get(l);
                                    if (cislo.getPhoneNumber().length() > 0) {
                                        ob.setIdPolozky(idPolozky);
                                        ob.setTyp("telefon");
                                        ob.setHodnota(cislo.getPhoneNumber());
                                        try {
                                            ob.setOznaceni(cislo.getRel().substring(cislo.getRel().indexOf('#') + 1));
                                        } catch (Exception ad) {
                                            try {
                                                ob.setOznaceni(cislo.getLabel());
                                            } catch (Exception e) {
                                                ob.setOznaceni("Neurčeno");
                                            }
                                        }
                                        ContactManager.addPhone(ob);
                                    }
                                }
                            }

                            ob = new ObecnyKontakt();
                            List<Im> ims = entry.getImAddresses();
                            if (ims != null) {
                                for (int m = 0; m < ims.size(); m++) {
                                    try {
                                        Im im = ims.get(m);
                                        if (im.getAddress().length() > 0) {
                                            ob.setIdPolozky(idPolozky);
                                            ob.setTyp("im");
                                            ob.setHodnota(im.getAddress());
                                            try {
                                                ob.setOznaceni(im.getRel().substring(im.getRel().indexOf('#') + 1));
                                            } catch (Exception ad) {
                                                try {
                                                    ob.setOznaceni(im.getLabel());
                                                } catch (Exception e) {
                                                    ob.setOznaceni("Neurčen");
                                                }
                                            }

                                            try {
                                                ob.setTyp2(im.getProtocol().toString().substring(im.getProtocol().toString().indexOf('#') + 1));
                                            } catch (Exception ad) {

                                                try {
                                                    ob.setTyp2(im.getProtocol());
                                                } catch (Exception e) {
                                                    ob.setTyp2("Neurčen");
                                                }
                                            }
                                            ContactManager.addIm(ob);
                                        }
                                    } catch (Exception ad) {
                                    }
                                }
                            }

                            ob = new ObecnyKontakt();
                            List<Website> urls = entry.getWebsites();
                            if (urls != null) {
                                for (int n = 0; n < urls.size(); n++) {
                                    try {
                                        Website web = urls.get(n);
                                        if (web.getHref().length() > 0) {
                                            ob.setIdPolozky(idPolozky);
                                            ob.setTyp("url");
                                            ob.setHodnota(web.getHref());

                                            try {
                                                ob.setOznaceni(web.getRel().toString().substring(web.getRel().toString().indexOf('#') + 1));
                                            } catch (Exception ad) {
                                                try {
                                                    ob.setOznaceni(web.getLabel());
                                                } catch (Exception e) {
                                                    ob.setOznaceni("Neurčen");
                                                }
                                            }
                                            ContactManager.addUrl(ob);
                                        }
//                                    System.out.println("URL: " + web.getRel().toString());
//                                    System.out.println(web.getHref());
                                    } catch (Exception ad) {
                                    }
                                }
                            }
                        } catch (Exception ee) {
                        }
                    }
                    importovano++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        DatabaseManager.updateLabelList(idU);
        return importovano;
    }

    public static void export(int idU, String absolutePath, String jmenoU, String prijmeniU, String format, Set<String> lettersToExport, int labelOrLetter) throws ParserConfigurationException, TransformerConfigurationException, TransformerException, IOException, Exception {
        List<Polozka> polozky;
        System.out.println("START " + absolutePath);
        if (format.equalsIgnoreCase("xml")) {

            if (labelOrLetter == 2) {
                polozky = DatabaseManager.getItemsByLetter(idU, lettersToExport);
            } else {
                polozky = DatabaseManager.getItemsByLabel(idU, lettersToExport);
            }
            if (polozky != null) {
                exportXML(idU, absolutePath, jmenoU, prijmeniU, polozky);
            }
        } else if (format.equalsIgnoreCase("html")) {
            exportHTML(idU, absolutePath, jmenoU, prijmeniU);
        } else if (format.equalsIgnoreCase("csv")) {

            if (labelOrLetter == 2) {
                polozky = DatabaseManager.getItemsByLetter(idU, lettersToExport);
            } else {
                polozky = DatabaseManager.getItemsByLabel(idU, lettersToExport);
            }
            if (polozky != null) {
                exportCSV(idU, absolutePath, jmenoU, prijmeniU, polozky);
            }
        }
    }

    public static String exportGmail(int idU, String username, char[] passwordChar, String name) throws AuthenticationException, ServiceException, MalformedURLException, IOException, Exception {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < passwordChar.length; i++) {
            sb.append(passwordChar[i]);
        }
        String password = sb.toString();

        ArrayList<String> groups = getGmailGroups(username, passwordChar);
        int i = 1;
        String puvodni = name;
        // ensure unique
        while (groups.contains(name)) {
            name = puvodni + "_" + i;
            i++;
        }
        ContactsService myService = new ContactsService("Organizer Desktop beta");
        myService.setUserCredentials(username, password);
        // create group
        URL postUrl = new URL("http://www.google.com/m8/feeds/contacts/" + username + "/full");
        ContactGroupEntry skupina = createContactGroup(myService, name, username);

        String groupID = skupina.getId();
        // get all contacts
        int adresarID = DatabaseManager.getAdresarIDbyUserID(idU);
        List<Polozka> polozky = DatabaseManager.getAllItems(adresarID);
        // upload all contacts

        for (int j = 0; j < polozky.size(); j++) {
            createGoogleContact(myService, polozky.get(j), postUrl, skupina, groupID);
        }
        return name;

    }

    public static ContactGroupEntry createContactGroup(ContactsService service,
            String name, String username)
            throws ServiceException, IOException {

        ContactGroupEntry group = new ContactGroupEntry();
        group.setTitle(new PlainTextConstruct(name));
        URL url = new URL("http://www.google.com/m8/feeds/groups/" + username + "/full");
        System.out.println("URL JE " + url.toString());
        return service.insert(url, group);
    }

    private static void createGoogleContact(ContactsService myService, Polozka p, URL feedURL, ContactGroupEntry groupEntry, String groupID) throws IOException, ServiceException {

        com.google.gdata.data.contacts.ContactEntry contact = new com.google.gdata.data.contacts.ContactEntry();
        Name name = new Name();
        final String NO_YOMI = null;
        String fullName = p.getJmeno() + " " + p.getPrijmeni();
        name.setFullName(new FullName(fullName, NO_YOMI));
        contact.setName(name);
        Iterator<Adresa> it = p.getAdresy().iterator();
        while (it.hasNext()) {
            Adresa a = it.next();
            StructuredPostalAddress spa = new StructuredPostalAddress();
            spa.setCity(new City(a.getMesto()));
            spa.setCountry(null);
            spa.setStreet(new Street(a.getUlice() + " " + a.getCp()));
            spa.setPostcode(new PostCode(a.getPsc() + ""));
            //  spa.setRel("http://schemas.google.com/g/2005#other");
            spa.setLabel(StringChecker.removeAccents(a.getTyp()));
            contact.addStructuredPostalAddress(spa);
        }
        Set<ObecnyKontakt> kontakty = p.getKontakty();
        Iterator<ObecnyKontakt> itk = kontakty.iterator();
        while (itk.hasNext()) {
            ObecnyKontakt o = itk.next();
            if (o.getTyp().equalsIgnoreCase("email")) {
                Email e = new Email();
                e.setLabel(StringChecker.removeAccents(o.getOznaceni()));
                //       e.setRel("http://schemas.google.com/g/2005#other");
                e.setAddress(o.getHodnota());
                e.setDisplayName(fullName);
                contact.addEmailAddress(e);
            }
            if (o.getTyp().equalsIgnoreCase("telefon")) {
                PhoneNumber ph = new PhoneNumber();
                //      ph.setRel("http://schemas.google.com/g/2005#other");
                ph.setLabel(StringChecker.removeAccents(o.getOznaceni()));
                ph.setPhoneNumber(o.getHodnota());
                contact.addPhoneNumber(ph);
            }
            if (o.getTyp().equalsIgnoreCase("IM")) {
                Im im = new Im();
                im.setAddress(o.getHodnota());
                //   im.setRel("http://schemas.google.com/g/2005#other");
                im.setLabel(StringChecker.removeAccents(o.getOznaceni()));
                im.setProtocol(o.getTyp2());
                contact.addImAddress(im);
            }
            if (o.getTyp().equalsIgnoreCase("other")) {
                ExtendedProperty ep = new ExtendedProperty();
                ep.setName(o.getOznaceni());

                ep.setValue(o.getHodnota());
                contact.addExtendedProperty(ep);
            }
            if (o.getTyp().equalsIgnoreCase("url")) {
                Website web = new Website();
                web.setLabel(StringChecker.removeAccents(o.getOznaceni()));
                //    web.setRel(Website.Rel.OTHER);
                web.setHref(o.getHodnota());
                contact.addWebsite(web);
            }
        }

        GroupMembershipInfo groupInfo = new GroupMembershipInfo();
        groupInfo.setDeleted(false);
        groupInfo.setHref(groupID);
        contact.addGroupMembershipInfo(groupInfo);
        myService.insert(feedURL, contact);
    }

    public static String exportGmail(int idU, String username, char[] passwordChar, String name, Set<String> labelsToExport, boolean labels) throws MalformedURLException, AuthenticationException, ServiceException, IOException, Exception {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < passwordChar.length; i++) {
            sb.append(passwordChar[i]);
        }
        String password = sb.toString();
        ArrayList<String> groups = getGmailGroups(username, passwordChar);
        int i = 1;
        String puvodni = name;
        while (groups.contains(name)) {
            name = puvodni + "_" + i;
            i++;
        }
        ContactsService myService = new ContactsService("Organizer Desktop beta");
        myService.setUserCredentials(username, password);
        URL postUrl = new URL("http://www.google.com/m8/feeds/contacts/" + username + "/full");
        ContactGroupEntry skupina = createContactGroup(myService, name, username);

        String groupID = skupina.getId();
        int adresarID = DatabaseManager.getAdresarIDbyUserID(idU);
        List<Polozka> polozky;
        if (labels) {
            polozky = DatabaseManager.getItemsByLabel(idU, labelsToExport);
        } else {
            polozky = DatabaseManager.getItemsByLetter(idU, labelsToExport);
        }
        for (int j = 0; j < polozky.size(); j++) {
            createGoogleContact(myService, polozky.get(j), postUrl, skupina, groupID);
        }
        return name;
    }

    public static int importCSV(String absolutePath, String label, int userID) throws Exception {
        CSVParser parser = new CSVParser();
        return parser.importCsv(label, userID, absolutePath);
    }

    private static void exportCSV(int idU, String absolutePath, String jmenoU, String prijmeniU, List<Polozka> polozky) throws IOException {
        // create file
        File file = new File(absolutePath + System.getProperty("file.separator") + "organizer.csv");
        StringBuffer sb = new StringBuffer();
        ArrayList<String> lines = new ArrayList<String>();
        String header = "\"Titul\",\"Jméno\",\"2. křestní jméno\",\"Příjmení\",\"Za příjmením\",\"Společnost\",\"Oddělení\",\"Funkce\",\"Ulice (zam.)\",\"Ulice 2 (zam.)\",\"Ulice 3 (zam.)\",\"Město (zam.)\",\"Země (zam.)\",\"PSČ (zam.)\",\"Země (zaměstnání)\",\"Ulice (dom.)\",\"Ulice 2 (dom.)\",\"Ulice 3 (dom.)\",\"Město (dom.)\",\"Země (dom.)\",\"PSČ (dom.)\",\"Země (domů)\",\"Jiná ulice\",\"Jiná ulice 2\",\"Jiná ulice 3\",\"Jiné město\",\"Jiný okres\",\"Jiné PSČ\",\"Jiná země\",\"Telefon asistenta\",\"Fax (zam.)\",\"Telefon (zam.)\",\"Telefon 2 (zam.)\",\"Zpětné volání\",\"Telefon do auta\",\"Telefon společnosti\",\"Fax domů\",\"Telefon domů\",\"Telefon 2 domů\",\"ISDN\",\"Mobilní telefon\",\"Jiný fax\",\"Jiný telefon\",\"Operátor\",\"Primární telefon\",\"Radiotelefon\",\"Pro neslyšící\",\"Dálnopis\",\"Adresářový server\",\"Děti\",\"Doporučil\",\"E-mailová adresa\",\"Typ e-mailu\",\"Zobrazované jméno e-mailu\",\"E-mailová adresa 2\",\"Typ e-mailu 2\",\"Zobrazované jméno e-mailu 2\",\"E-mailová adresa 3\",\"Typ e-mailu 3\",\"Zobrazované jméno e-mailu 3\",\"Faktury\",\"IČO\",\"Informace o volném čase v síti Internet\",\"Iniciály\",\"Jazyk\",\"Jiná poštovní přihrádka\",\"Jméno asistenta\",\"Jméno správce\",\"Kategorie\",\"Klíč. slova\",\"Manžel/manželka\",\"Narozeniny\",\"Pohlaví\",\"Poštovní přihrádka domů\",\"Poštovní přihrádka zaměstnání\",\"Poznámky\",\"Priorita\",\"Profese\",\"Rodné číslo\",\"Soukromé\",\"Účet\",\"Umístění\",\"Umístění pracoviště\",\"Utajení\",\"Uživatel 1\",\"Uživatel 2\",\"Uživatel 3\",\"Uživatel 4\",\"Výročí\",\"Vzdálenost\",\"Webová stránka\",\"Záliby\"";
        // add header
        sb.append(header);
        String newLine = System.getProperty("line.separator");

        // for each P in polozky add line

        for (int i = 0; i < polozky.size(); i++) {
            sb.append(newLine);
            sb.append(makeCSVLine(polozky.get(i), newLine));
        }

        Writer output = new BufferedWriter(new FileWriter(file, false));
        //   boolean neww = file.createNewFile();
        output.write(sb.toString());
        output.close();

    }

    private static String makeCSVLine(Polozka p, String newLine) {
        String[] line = new String[94];
        StringBuffer note = new StringBuffer();

        for (int i = 0; i < line.length; i++) {
            line[i] = "";
        }

        line[1] = p.getJmeno();
        line[3] = p.getPrijmeni();

        // process addresses
        Set<Adresa> adresy = p.getAdresy();
        Iterator<Adresa> it = adresy.iterator();
        int step = 0;
        while (it.hasNext()) {
            Adresa a = it.next();
            if (step < 3) {
                line[8 + step * 7] = a.getUlice() + " " + a.getCp();
                line[11 + step * 7] = a.getMesto();
                line[13 + step * 7] = a.getPsc() + "";
                step++;
            } else {
                note.append("Address(" + a.getTyp() + "): " + a.getUlice() + " " + a.getCp() + " " + a.getMesto() + " " + a.getPsc() + " ; ");
                //note.append(newLine);
            }
        }

        ArrayList<ObecnyKontakt> phones = new ArrayList<ObecnyKontakt>();
        ArrayList<ObecnyKontakt> emails = new ArrayList<ObecnyKontakt>();
        ArrayList<ObecnyKontakt> ims = new ArrayList<ObecnyKontakt>();
        ArrayList<ObecnyKontakt> urls = new ArrayList<ObecnyKontakt>();
        ArrayList<ObecnyKontakt> others = new ArrayList<ObecnyKontakt>();
        ObecnyKontakt ob;
        Set kontakty = p.getKontakty();

        Iterator<ObecnyKontakt> ito = kontakty.iterator();
        String typ;
        while (ito.hasNext()) {
            ob = (ObecnyKontakt) ito.next();
            typ = ob.getTyp();

            if (typ.equalsIgnoreCase("email")) {
                emails.add(ob);
            } else if (typ.equalsIgnoreCase("telefon")) {
                phones.add(ob);
            } else if (typ.equalsIgnoreCase("im")) {
                ims.add(ob);
            } else if (typ.equalsIgnoreCase("url")) {
                urls.add(ob);
            } else {
                others.add(ob);
            }
        }


        // process phones

        step = 29;
        // csv top is 19

        for (int i = 0; i < phones.size(); i++) {
            if (i < 19) {
                line[step] = phones.get(i).getHodnota();

            } else {
                note.append("Phone (" + phones.get(i).getOznaceni() + "): " + phones.get(i).getHodnota() + " ; ");
            }
            step++;
        }
        step = 51;
        for (int i = 0; i < emails.size(); i++) {
            if (i < 3) {
                step = 51 + i * 3;
                line[step] = emails.get(i).getHodnota();
                line[step + 2] = p.getJmeno() + " " + p.getPrijmeni();
            } else {
                note.append("Email (" + emails.get(i).getOznaceni() + "): " + emails.get(i).getHodnota() + " ; ");
            }
            step++;
        }

// url

        for (int i = 0; i < urls.size(); i++) {
            if (i < 1) {
                line[92] = urls.get(i).getHodnota();
            } else {
                note.append("URL (" + urls.get(i).getOznaceni() + "): " + urls.get(i).getHodnota() + " ; ");
            }


        }

// im
        for (int i = 0; i < ims.size(); i++) {
            note.append("IM (" + ims.get(i).getTyp2() + " - " + ims.get(i).getOznaceni() + "): " + ims.get(i).getHodnota() + "; ");
        }

// other
        for (int i = 0; i < others.size(); i++) {
            note.append("Other (" + others.get(i).getOznaceni() + "): " + others.get(i).getHodnota() + "; ");
        }

        line[77] = note.toString();

        //"","","","","","","","",,,,,,,,,,,,,,,,,,,,,,,,"",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"","",,,,,"",,"0.0.00","Neznámé",,,"
//","Střední",,,"False","","",,"Normální",,,,,"0.0.00",,""


        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < line.length; i++) {
            sb.append("\"" + line[i] + "\",");
        }
        String t = sb.toString();
        t = t.substring(0, t.length() - 1);
        return t;
    }
}
