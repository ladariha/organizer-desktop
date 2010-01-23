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
public class Adresar {
private Set polozky;
private int id;
private int idVlastnika;

    public Adresar(){}

    public Set getPolozky() {
        return polozky;
    }

    public void setPolozky(Set polozka) {
        this.polozky = polozka;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdVlastnika() {
        return idVlastnika;
    }

    public void setIdVlastnika(int idVlastnika) {
        this.idVlastnika = idVlastnika;
    }
    
}
