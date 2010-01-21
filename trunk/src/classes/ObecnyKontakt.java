/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;



/**
 *
 * @author Vladimír Řiha
 */
public class ObecnyKontakt implements Comparable<ObecnyKontakt> {
private int id;
private String hodnota;
private String typ; //email, IM
private String typ2; // gmail, icq
private String oznaceni;
private int idPolozky;

    public ObecnyKontakt(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHodnota() {
        return hodnota;
    }

    public void setHodnota(String hodnota) {
        this.hodnota = hodnota;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getOznaceni() {
        return oznaceni;
    }

    public void setOznaceni(String oznaceni) {
        this.oznaceni = oznaceni;
    }

    public String getTyp2() {
        return typ2;
    }

    public void setTyp2(String typ2) {
        this.typ2 = typ2;
    }

    public int getIdPolozky() {
        return idPolozky;
    }

    public void setIdPolozky(int idAdresare) {
        this.idPolozky = idAdresare;
    }

    public int compareTo(ObecnyKontakt o) {
     	if(this.id>o.getId()){
		return 1;
		}
		return 0;
	}
    }
    

