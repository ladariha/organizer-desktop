/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;

/**
 *
 * @author Vladimír Řiha
 */
public class Uzivatel {
private int id;
private String jmeno;
private String prijmeni;
private String email;
    private String stitky;
private Ucet ucet;

public Uzivatel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public Ucet getUcet() {
        return ucet;
    }

    public void setUcet(Ucet idUcet) {
        this.ucet = idUcet;
    }

    /**
     * @return the stitky
     */
    public String getStitky() {
        return stitky;
    }

    /**
     * @param stitky the stitky to set
     */
    public void setStitky(String stitky) {
        this.stitky = stitky;
    }

      
}
