/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import classes.Adresa;
import classes.ObecnyKontakt;
import classes.Polozka;
import java.io.IOException;
import java.util.ArrayList;
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

/**
 *
 * @author Lada Riha
 */
public class XMLParser {

    /**
     * @param args the command line arguments
     */
    public static int parse(int idUser, int idAdresar, String file) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {
        // TODO code application logic here
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docbuilder = factory.newDocumentBuilder();
        factory.setValidating(true);
        docbuilder.setErrorHandler(new ErrorOutput());
        Document doc = docbuilder.parse(file);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(System.out);
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://www.ladariha.cz/file/organizer/organizer.dtd");
        transformer.transform(source, result);
        deleteWhiteSpaces(doc);
        
        return parseDocument(doc, idAdresar);
    }

    public static void deleteWhiteSpaces(Document doc) {
        Node n = doc.getDocumentElement();
        NodeIterator ni = ((DocumentTraversal) doc).createNodeIterator(doc.getDocumentElement(), NodeFilter.SHOW_TEXT, new EmptyText(), false);
        while ((n = ni.nextNode()) != null) {
            Node rodic = n.getParentNode();
            rodic.removeChild(n);
        }
    }

    private static int parseDocument(Document doc, int idAdresar) {
        int imported = 0;
        NodeList addressBooks = doc.getElementsByTagName("adresar");
        Node addressBook = addressBooks.item(0);
        NamedNodeMap attributes = addressBook.getAttributes();

        if (true) {
            NodeList items = doc.getElementsByTagName("polozka");

            for (int i = 0; i < items.getLength(); i++) {
                ArrayList<Adresa> addresses = new ArrayList<Adresa>();
                ArrayList<ObecnyKontakt> emails = new ArrayList<ObecnyKontakt>();
                ArrayList<ObecnyKontakt> phones = new ArrayList<ObecnyKontakt>();
                ArrayList<ObecnyKontakt> ims = new ArrayList<ObecnyKontakt>();
                ArrayList<ObecnyKontakt> urls = new ArrayList<ObecnyKontakt>();
                ArrayList<ObecnyKontakt> others = new ArrayList<ObecnyKontakt>();

                // get search letter a stitek
                attributes = items.item(i).getAttributes();
                String searchLetter = "";
                String label = "";

                for (int d = 0; d < attributes.getLength(); d++) {
                    if (attributes.item(d).getNodeName().equalsIgnoreCase("pismeno")) {
                        searchLetter = attributes.item(d).getNodeValue();
                    } else if (attributes.item(d).getNodeName().equalsIgnoreCase("stitek")) {
                        label = attributes.item(d).getNodeValue();
                    }
                }


                Polozka p = new Polozka();
                p.setStitek(label);
                p.setSearchLetter(searchLetter);
                NodeList inPolozka = items.item(i).getChildNodes();
                for (int j = 0; j < inPolozka.getLength(); j++) {
                    Node u = inPolozka.item(j);
                    if (u.getNodeName().equalsIgnoreCase("jmeno")) {
                        p.setJmeno(u.getTextContent());
                    } else if (u.getNodeName().equalsIgnoreCase("prijmeni")) {
                        p.setPrijmeni(u.getTextContent());
                    } else if (u.getNodeName().equalsIgnoreCase("adresy")) {

                        NodeList addressNodes = u.getChildNodes();

                        for (int k = 0; k < addressNodes.getLength(); k++) { // for each address
                            Adresa a = new Adresa();

                            attributes = addressNodes.item(k).getAttributes();

                            a.setTyp(attributes.item(0).getNodeValue());

                            NodeList aparts = addressNodes.item(k).getChildNodes();

                            for (int l = 0; l < aparts.getLength(); l++) {
                                Node n = aparts.item(l);
                                if (n.getNodeName().equalsIgnoreCase("ulice")) {
                                    a.setUlice(n.getTextContent());
                                } else if (n.getNodeName().equalsIgnoreCase("cisloPopisne")) {
                                    a.setCp(n.getTextContent());
                                } else if (n.getNodeName().equalsIgnoreCase("mesto")) {
                                    a.setMesto(n.getTextContent());
                                } else if (n.getNodeName().equalsIgnoreCase("psc")) {
                                    a.setPsc(Integer.valueOf(n.getTextContent()).intValue());
                                }
                            }
                            addresses.add(a);
                        }


                    } else if (u.getNodeName().equalsIgnoreCase("emaily")) {
                        NodeList emailsNodes = u.getChildNodes();

                        for (int k = 0; k < emailsNodes.getLength(); k++) { // for each mail
                            ObecnyKontakt o = new ObecnyKontakt();

                            attributes = emailsNodes.item(k).getAttributes();

                            o.setOznaceni(attributes.item(0).getNodeValue());
                            o.setTyp("email");
                            NodeList aparts = emailsNodes.item(k).getChildNodes();

                            for (int l = 0; l < aparts.getLength(); l++) {
                                Node n = aparts.item(l);
                                if (n.getNodeName().equalsIgnoreCase("emailAdresa")) {
                                    o.setHodnota(n.getTextContent());
                                } else if (n.getNodeName().equalsIgnoreCase("sluzba")) {
                                    o.setTyp2(n.getTextContent());
                                }
                            }
                            emails.add(o);
                        }
                    } else if (u.getNodeName().equalsIgnoreCase("telefony")) {
                        NodeList phoneNodes = u.getChildNodes();

                        for (int k = 0; k < phoneNodes.getLength(); k++) { // for each phone
                            ObecnyKontakt o = new ObecnyKontakt();

                            attributes = phoneNodes.item(k).getAttributes();

                            o.setOznaceni(attributes.item(0).getNodeValue());
                            o.setTyp("telefon");
                            NodeList aparts = phoneNodes.item(k).getChildNodes();

                            for (int l = 0; l < aparts.getLength(); l++) {
                                Node n = aparts.item(l);
                                if (n.getNodeName().equalsIgnoreCase("cislo")) {
                                    o.setHodnota(n.getTextContent());
                                }
                            }
                            phones.add(o);
                        }
                    } else if (u.getNodeName().equalsIgnoreCase("imKontakty")) {
                        NodeList imNodes = u.getChildNodes();

                        for (int k = 0; k < imNodes.getLength(); k++) { // for each IM
                            ObecnyKontakt o = new ObecnyKontakt();

                            attributes = imNodes.item(k).getAttributes();

                            for (int q = 0; q < attributes.getLength(); q++) {
                                if (attributes.item(q).getNodeName().equalsIgnoreCase("oznaceni")) {
                                    o.setOznaceni(attributes.item(q).getNodeValue());
                                } else {
                                    o.setTyp2(attributes.item(q).getNodeValue());
                                }

                            }
                            o.setTyp("im");
                            NodeList aparts = imNodes.item(k).getChildNodes();

                            for (int l = 0; l < aparts.getLength(); l++) {
                                Node n = aparts.item(l);
                                if (n.getNodeName().equalsIgnoreCase("kontakt")) {
                                    o.setHodnota(n.getTextContent());
                                }
                            }
                            ims.add(o);
                        }
                    } else if (u.getNodeName().equalsIgnoreCase("ostatni")) {
                        NodeList phoneNodes = u.getChildNodes();

                        for (int k = 0; k < phoneNodes.getLength(); k++) { // for each phone
                            ObecnyKontakt o = new ObecnyKontakt();

                            attributes = phoneNodes.item(k).getAttributes();

                            o.setOznaceni(attributes.item(0).getNodeValue());
                            o.setTyp("ostatni");
                            NodeList aparts = phoneNodes.item(k).getChildNodes();

                            for (int l = 0; l < aparts.getLength(); l++) {
                                Node n = aparts.item(l);
                                if (n.getNodeName().equalsIgnoreCase("text")) {
                                    o.setHodnota(n.getTextContent());
                                }
                            }
                            others.add(o);
                        }
                    }
                }
                try {
                    p.setIdAdresare(idAdresar);

                    int idP = ItemsManager.saveItem(p, idAdresar);
                    imported++;
                    for (int t = 0; t < addresses.size(); t++) {
                        Adresa aa = addresses.get(t);
                        aa.setIdPolozky(idP);
                        ContactManager.addAddress(aa);
                    }

                    for (int t = 0; t < phones.size(); t++) {
                        ObecnyKontakt aa = phones.get(t);
                        aa.setIdPolozky(idP);
                        ContactManager.addPhone(aa);
                    }

                    for (int t = 0; t < emails.size(); t++) {
                        ObecnyKontakt aa = emails.get(t);
                        aa.setIdPolozky(idP);
                        ContactManager.addEmail(aa);
                    }

                    for (int t = 0; t < ims.size(); t++) {
                        ObecnyKontakt aa = ims.get(t);
                        aa.setIdPolozky(idP);
                        ContactManager.addIm(aa);
                    }

                    for (int t = 0; t < urls.size(); t++) {
                        ObecnyKontakt aa = urls.get(t);
                        aa.setIdPolozky(idP);
                        ContactManager.addUrl(aa);
                    }

                    for (int t = 0; t < others.size(); t++) {
                        ObecnyKontakt aa = others.get(t);
                        aa.setIdPolozky(idP);
                        ContactManager.addOther(aa);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return imported;
    }

    static class EmptyText implements NodeFilter {

        public short acceptNode(Node n) {
            if (n.getNodeValue().trim().length() == 0) {
                return NodeFilter.FILTER_ACCEPT;
            } else {
                return NodeFilter.FILTER_SKIP;
            }
        }
    }

    static class ErrorOutput implements ErrorHandler {

        public ErrorOutput() {
        }

        public void warning(SAXParseException e) {
            System.err.print("ERROR: " + e.toString());
            System.out.println("Error at " + e.getLineNumber() + " line.");
            System.out.println(e.getMessage());
            System.exit(0);
        }

        public void fatalError(SAXParseException e) throws SAXException {
            throw new SAXException("ERROR: " + e.toString());
        }

        public void error(SAXParseException e) throws SAXException {
            throw new SAXException("ERROR: " + e.toString());
        }
    }
}
