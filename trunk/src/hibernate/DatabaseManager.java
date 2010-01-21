/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hibernate;

import classes.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import util.*;

/**
 *
 * @author Vladimír Řiha
 */
public class DatabaseManager {

    public static int getUserIdByUsername(String username) {
        try {
            Session session = NewHibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query q = session.createQuery("SELECT id FROM Ucet WHERE username='" + username + "'");
            List<Integer> resultList = q.list();

            int idA = -1;
            for (int i = 0; i < resultList.size(); i++) {
                idA = (int) resultList.get(i);

            }
            session.getTransaction().commit();

            return idA;
        } catch (Exception a) {
            a.printStackTrace();
        }
        return -1;
    }

    public static int getUserIdByPassword(String password) {
        try {
            StringChecker sc = new StringChecker();
            String p2 = password;
            password = sc.getHash(password);

            Session session = NewHibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query q = session.createQuery("SELECT id FROM Ucet WHERE password='" + password + "'");
            List<Integer> resultList = q.list();

            int idA = -1;
            for (int i = 0; i < resultList.size(); i++) {
                idA = (int) resultList.get(i);
            }
            session.getTransaction().commit();

            return idA;
        } catch (Exception a) {
            a.printStackTrace();
        }


        return -1;
    }

    public static void saveNewUcet(Ucet u) {

        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(u);
        session.getTransaction().commit();

    }

    public static void saveNewPolozka(Polozka polozka) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(polozka);
        session.getTransaction().commit();
    }

    public static void saveNewKontakt(ObecnyKontakt k1) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(k1);
        session.getTransaction().commit();
    }

    public static void saveNewUzivatel(Uzivatel uzivatel) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(uzivatel);
        session.getTransaction().commit();
    }

    public static List getItemsByID(int id, String letter,Set<String> hiddenLabels) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        String labelsCondition = "";
        if (hiddenLabels.size() > 0) {
            labelsCondition = getLabelCondition(hiddenLabels);
        }

        System.out.println(labelsCondition);
        int idA = getAdresarIDbyUserID(id);
        if (idA != -1) {
            session.beginTransaction();
            Query q = session.createQuery("FROM Polozka WHERE idAdresare=" + idA + " AND searchLetter='" + letter + "'" +labelsCondition);
            List<Polozka> polozky = q.list();
            return polozky;
        }
        return null;
    }

    public static int getAdresarIDbyUserID(int id) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query q = session.createQuery("SELECT id FROM Adresar WHERE idVlastnika=" + id + "");
        List<Integer> rs = q.list();
        try {
            return rs.get(0);

        } catch (Exception ex) {
            ex.printStackTrace();
             System.out.println("ID je "+id);
            return -1;
        }
    }

    public static Polozka getItemsByID(int idP, int idAdresare) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query q = session.createQuery("FROM Polozka WHERE idAdresare=" + idAdresare + " AND id=" + idP + "");
        List<Polozka> polozky = q.list();
        return polozky.get(0);

    }

    public static void deleteItem(int idP) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query q = session.createQuery("DELETE FROM Polozka WHERE id=" + idP + "");
        q.executeUpdate();
        q = session.createQuery("DELETE FROM ObecnyKontakt WHERE idPolozky=" + idP + "");
        q.executeUpdate();
        session.getTransaction().commit();

    }

    public static void updateItem(int idP, String jmeno, String prijmeni, String label) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query q = session.createQuery("FROM Polozka WHERE id=" + idP + "");
        List<Polozka> polozky = q.list();

        Polozka p = polozky.get(0);
        p.setJmeno(jmeno);
        p.setPrijmeni(prijmeni);
        if(label.equals("")){
p.setStitek("organizer");
        }else{
p.setStitek(label);
        }
        String tmp = StringChecker.removeAccents(prijmeni);
        p.setSearchLetter(tmp.substring(0, 1));
        session.save(p);

        session.getTransaction().commit();
    }

    public static void addAddress(Adresa a) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(a);
        session.getTransaction().commit();
    }

    public static void addObecnyKontakt(ObecnyKontakt kontakt) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(kontakt);
        session.getTransaction().commit();
    }

    public static void deleteAddress(int idItem) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query q = session.createQuery("DELETE FROM Adresa WHERE id=" + idItem + "");
        q.executeUpdate();
        session.getTransaction().commit();
    }

    public static void deleteCommonContact(int idItem) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query q = session.createQuery("DELETE FROM ObecnyKontakt WHERE id=" + idItem + "");
        q.executeUpdate();
        session.getTransaction().commit();
    }

    public static void updateAddress(Adresa adresa) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(adresa);
        session.getTransaction().commit();
    }

    public static Adresa getAddress(int idAdresy) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query q = session.createQuery("FROM Adresa WHERE id=" + idAdresy + "");
        List<Adresa> polozky = q.list();
        return polozky.get(0);
    }

    public static ObecnyKontakt getObecnyKontakt(int id) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query q = session.createQuery("FROM ObecnyKontakt WHERE id = " + id + "");
        List<ObecnyKontakt> list = q.list();
        return list.get(0);
    }

    public static void updateObecnyKontakt(ObecnyKontakt kontakt) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(kontakt);
        session.getTransaction().commit();
    }

    public static int getItemsCount(int archivId) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query q = session.createQuery("FROM Polozka WHERE idAdresare=" + archivId + "");
        return q.list().size();
    }

    public static void saveItem(String jmeno, String prijmeni, int idAdresare, String stitek) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Polozka p = new Polozka();
        p.setJmeno(jmeno);
        p.setPrijmeni(prijmeni);
        p.setStitek(stitek);
        p.setIdAdresare(idAdresare);
        String tmp = StringChecker.removeAccents(prijmeni);
        p.setSearchLetter(tmp.substring(0, 1));
        session.save(p);

        session.getTransaction().commit();
    }

    public synchronized static int saveItem(Polozka p, int idAdr) {
        try {

            Session session = NewHibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(p);
            Query q = session.createQuery("SELECT max(id) FROM Polozka WHERE idAdresare=" + idAdr + "");
            int vysledek = (Integer) q.list().get(0);
            session.getTransaction().commit();
            return vysledek;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static List<Polozka> getItemsByID(int userID) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();

        int idA = getAdresarIDbyUserID(userID);
        if (idA != -1) {
            session.beginTransaction();
            Query q = session.createQuery("FROM Polozka WHERE idAdresare=" + idA + "");
            List<Polozka> polozky = q.list();
            return polozky;
        }
        return null;
    }

    public static String getUserLabels(int userID) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query q = session.createQuery("SELECT stitky FROM Uzivatel WHERE id=" + userID + "");
        List<String> resultList = q.list();

        String labels = "";
        for (int i = 0; i < resultList.size(); i++) {
            labels = resultList.get(i);
        }
        session.getTransaction().commit();
        return labels;
    }

    public static void saveAdresar(Adresar adresar) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(adresar);
        session.getTransaction().commit();
    }

    private static String getLabelCondition(Set<String> hiddenLabels) {
        StringBuffer sb = new StringBuffer();

        Iterator<String> it = hiddenLabels.iterator();
        while(it.hasNext()){
        String label = it.next();
         sb.append("AND stitek!='"+label + "' ");

        }
       
         return sb.toString();
    }



    public static String getLabelByID(int id) {

          Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query q = session.createQuery("SELECT stitek FROM Polozka WHERE id=" + id + "");
        List<String> resultList = q.list();

        String label = "";
        for (int i = 0; i < resultList.size(); i++) {
            label = resultList.get(i);
        }
        session.getTransaction().commit();
        return label;

    }

    public static void updateLabelList(int idU) {
        int idAdresare = DatabaseManager.getAdresarIDbyUserID(idU);
              Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query q = session.createQuery("SELECT DISTINCT stitek FROM Polozka WHERE idAdresare=" + idAdresare + "");
        List<String> resultList = q.list();
        StringBuffer sb = new StringBuffer();
        if(resultList!=null){
        for(int i = 0;i<resultList.size();i++){
        sb.append(resultList.get(i)+",");
        }
        }
        String labels = sb.toString();
        labels = labels.substring(0, labels.length()-1);

        q = session.createQuery("FROM Uzivatel WHERE id=" + idU + "");
        Uzivatel u = (Uzivatel)(q.list()).get(0);
        u.setStitky(labels);
            session.save(u);
        session.getTransaction().commit();
    }

        public static void resetLabelList(int idU) {
              Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
         
        Query q = session.createQuery("FROM Uzivatel WHERE id=" + idU + "");
        Uzivatel u = (Uzivatel)(q.list()).get(0);
        u.setStitky("");
            session.save(u);
        session.getTransaction().commit();
    }

    public static List<Polozka> getAllItems( int idAdresare) {
             Session session = NewHibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
              Query q = session.createQuery("FROM Polozka WHERE idAdresare=" + idAdresare + "");
        List<Polozka> resultList = q.list();
    session.getTransaction().commit();
        return resultList;
    }

    public static void deleteAddressBook(int idAdresare) {
            Session session = NewHibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            // vem id vsech polozek
                  Query q = session.createQuery("SELECT id FROM Polozka WHERE idAdresare=" + idAdresare + "");
             List<Integer> resultList = q.list();


               q = session.createQuery("DELETE FROM Polozka WHERE idAdresare=" + idAdresare + "");
        q.executeUpdate();

        for(int i = 0; i < resultList.size();i++){
             q = session.createQuery("DELETE FROM ObecnyKontakt WHERE idPolozky=" + resultList.get(i) + "");
        q.executeUpdate();
                     q = session.createQuery("DELETE FROM Adresa WHERE idPolozky=" + resultList.get(i) + "");
        q.executeUpdate();

        }
    session.getTransaction().commit();
       
    }

    public static void deleteItemsByLetter(Set<String> lettersToDelete, int idU) {
        if(lettersToDelete.size()>0){
        int idAdresare = getAdresarIDbyUserID(idU);
          Session session = NewHibernateUtil.getSessionFactory().openSession();
          String condition = getLetterCondition(lettersToDelete);
            session.beginTransaction();
              Query q = session.createQuery("DELETE FROM Polozka WHERE idAdresare=" + idAdresare + " "+condition);
q.executeUpdate();
    session.getTransaction().commit();
        }

        
    }

    private static String getLetterCondition(Set<String> letters) {
        Iterator<String> it = letters.iterator();
        int t = 0;
        StringBuffer sb = new StringBuffer();
        sb.append("AND (");
while(it.hasNext()){
    if(t==0){
  sb.append("  searchLetter='"+it.next()+"'");
    }else{
    sb.append(" OR searchLetter='"+it.next()+"'");
    }

t++;
}
        sb.append(")");
        return sb.toString();
    }

        private static String getLabelsCondition(Set<String> lettersToDelete) {
        Iterator<String> it = lettersToDelete.iterator();
        int t = 0;
        StringBuffer sb = new StringBuffer();
        sb.append("AND (");
while(it.hasNext()){
    if(t==0){
  sb.append("  stitek='"+it.next()+"'");
    }else{
    sb.append(" OR stitek='"+it.next()+"'");
    }

t++;
}
        sb.append(")");
        return sb.toString();
    }

    public static void deleteItemsByLabel(Set<String> labelsToDelete, int idU) {

               if(labelsToDelete.size()>0){
        int idAdresare = getAdresarIDbyUserID(idU);
          Session session = NewHibernateUtil.getSessionFactory().openSession();
          String condition = getLabelsCondition(labelsToDelete);
            session.beginTransaction();
              Query q = session.createQuery("DELETE FROM Polozka WHERE idAdresare=" + idAdresare + " "+condition);
q.executeUpdate();
    session.getTransaction().commit();
        }

    }

    public static int getContactsCount(String type) {
             Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        //Query q = session.createQuery("FROM O WHERE idAdresare=" + archivId + "");
        //return q.list().size();
        return 0;
    }

    public static List<Polozka> getItemsByLetter(int idU, Set<String> lettersToExport) {

                if(lettersToExport.size()>0){
        int idAdresare = getAdresarIDbyUserID(idU);
          Session session = NewHibernateUtil.getSessionFactory().openSession();
          String condition = getLetterCondition(lettersToExport);
            session.beginTransaction();
              Query q = session.createQuery(" FROM Polozka WHERE idAdresare=" + idAdresare + " "+condition);
//q.executeUpdate();
    session.getTransaction().commit();
    
        return q.list();
        }
return null;
    }

    public static List<Polozka> getItemsByLabel(int idU, Set<String> lettersToExport) {
         if(lettersToExport.size()>0){
        int idAdresare = getAdresarIDbyUserID(idU);
          Session session = NewHibernateUtil.getSessionFactory().openSession();
          String condition = getLabelsCondition(lettersToExport);
            session.beginTransaction();
              Query q = session.createQuery(" FROM Polozka WHERE idAdresare=" + idAdresare + " "+condition);
//q.executeUpdate();
    session.getTransaction().commit();

        return q.list();
        }
return null;
    }



    private void executeHQLQuery(String hql) {
        try {
            Session session = NewHibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query q = session.createQuery(hql);
            List resultList = q.list();

            session.getTransaction().commit();
        } catch (HibernateException he) {
            he.printStackTrace();
        }
    }

    public static String getUsernameByID(int id) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query q = session.createQuery("SELECT username FROM Ucet WHERE id=" + id + "");
        List<String> resultList = q.list();

        String username = "";
        for (int i = 0; i < resultList.size(); i++) {
            username = resultList.get(i);
        }
        session.getTransaction().commit();
        return username;
    }

    public static String getNameByID(int id) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query q = session.createQuery("SELECT jmeno FROM Uzivatel WHERE id=" + id + "");
        List<String> resultList = q.list();

        String username = "";
        for (int i = 0; i < resultList.size(); i++) {
            username = resultList.get(i);
        }
        session.getTransaction().commit();
        return username;
    }

    public static String getLastnameByID(int id) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query q = session.createQuery("SELECT prijmeni FROM Uzivatel WHERE id=" + id + "");
        List<String> resultList = q.list();

        String username = "";
        for (int i = 0; i < resultList.size(); i++) {
            username = resultList.get(i);
        }
        session.getTransaction().commit();
        return username;
    }

    public static int getAdresarIDbyItemID(int idP) {

        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query q = session.createQuery("SELECT idAdresare FROM Polozka WHERE id=" + idP + "");
        List<Integer> resultList = q.list();
        session.getTransaction().commit();
        return resultList.get(0);

    }
}
