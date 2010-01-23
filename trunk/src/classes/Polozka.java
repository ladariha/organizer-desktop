/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.util.*;

/**
 *
 * @author Vladimír Řiha
 */
public class Polozka implements Comparable<Polozka>{

    private Set kontakty;
    private int id;
    private Set adresy;
    private int idAdresare;
    private String jmeno;
    private String prijmeni;
    private String searchLetter;
private String stitek;

    public Polozka() {
    }

    public Set getAdresy() {
        return adresy;
    }

    public void setAdresy(Set adresy) {
        this.adresy = adresy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAdresare() {
        return idAdresare;
    }

    public void setIdAdresare(int idAdresare) {
        this.idAdresare = idAdresare;
    }

    public Set getKontakty() {
        return kontakty;
    }

    public void setKontakty(Set kontakty) {
        this.kontakty = kontakty;
    }

    public String getJmeno() {
        return jmeno;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public void setPrijmeni(String prijmeni) {
        this.prijmeni = prijmeni;
    }

    public String getSearchLetter() {
        return searchLetter;
    }

    public void setSearchLetter(String searchLetter) {
        if(Character.isLetter(searchLetter.charAt(0))){
        this.searchLetter = searchLetter;}else{
        this.searchLetter = "#";
        }
    }


    /**
     * @return the stitek
     */
    public String getStitek() {
        return stitek;
    }

    /**
     * @param stitek the stitek to set
     */
    public void setStitek(String stitek) {
        this.stitek = stitek;
    }

    public int compareTo(Polozka  o) {
       String o1 = o.getJmeno().trim();
       String o2 = o.getPrijmeni().trim();

       String oo =  o1+" "+o2;

       String t1 = this.getJmeno().trim();
       String t2 = this.getPrijmeni().trim();

       String tt =  t1+" "+t2;
       if(tt.equalsIgnoreCase(oo)){
       return 0 ;
       }
       if(true)
       return 1;
       return -1;
    }


}
