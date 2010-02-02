/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import classes.Polozka;
import hibernate.DatabaseManager;
import java.io.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.validation.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
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
    public static void main(String[] args) {
        // TODO code application logic here

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docbuilder = factory.newDocumentBuilder();
            factory.setValidating(true);
            docbuilder.setErrorHandler(new ErrorOutput());
            Document doc = docbuilder.parse("organizer.xml");
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(System.out);
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://www.ladariha.cz/file/organizer/organizer.dtd");
            transformer.transform(source, result);
            deleteWhiteSpaces(doc);
            parseDocument(doc);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static void deleteWhiteSpaces(Document doc) {
        Node n = doc.getDocumentElement();
        NodeIterator ni = ((DocumentTraversal) doc).createNodeIterator(doc.getDocumentElement(), NodeFilter.SHOW_TEXT, new EmptyText(), false);
        while ((n = ni.nextNode()) != null) {
            Node rodic = n.getParentNode();
            rodic.removeChild(n);
        }
    }

    private static void parseDocument(Document doc) {

        NodeList addressBooks = doc.getElementsByTagName("adresar");
        Node addressBook = addressBooks.item(0);
        NamedNodeMap attributes = addressBook.getAttributes();
        int xmlABid = Integer.valueOf(attributes.item(0).getNodeValue()).intValue(); // addressbook id
        int xmlOid = Integer.valueOf(attributes.item(1).getNodeValue()).intValue();// owner id

        
        if (true) {
            NodeList items = doc.getElementsByTagName("polozka");
        
            for (int i = 0; i < items.getLength(); i++) {
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
                    }
                }


                // setJmeno...


                //for each adresa in adresy
                //for each email in emaily



            }



        }




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
