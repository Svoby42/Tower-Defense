package javafxapplication1;

import javafxapplication1.veze.Vez;
import javafxapplication1.brouci.Nepritel;
import javafx.collections.ObservableList;

public class Hra {

    static public ObservableList list;
    Mapa mapa1;
    static private int volba;
    static public int velikost;
    static public double sirka;
    static public double vyska;
    static public boolean ztlumeno = false;

    static void zmenaVelikosti() {
        Mapa.zmenaVelikosti();
        Nepritel.nastavVelikost();
        Vez.nastavVelikost();
    }

    static public void nastavSirku(double volba) {
        sirka = volba;
    }

    static public void nastavVysku(double volba) {
        vyska = volba;
    }

    /**
     * nastaví naprostý základ pro spuštění jakéhokoli levelu a mapy
     */
    static public void nastavZaklad() {
        Mapa.nastavList(list);
        Mapa.buttony();
        Nepritel.nastavPole(Mapa.vratPole());
        Nepritel.nastavList(list);
        Vez.nastavPole(Mapa.vratPole());
        Vez.nastavList(list);
    }

    static public void spustPrvniLevel() {
        Mapa.vykreslitMapu(1);
        Nepritel.spustPrvniLevel();
    }

    static public void nastavList(ObservableList listVyber) {
        list = listVyber;
    }

    static public void ztlumit(boolean volba) {
        Nepritel.ztlum(volba);
        Vez.ztlum(volba);
        ztlumeno = volba;
    }

    static public boolean jeTicho() {
        return ztlumeno;
    }

    static public double vratSirku() {
        return sirka;
    }

    static public double vratVysku() {
        return vyska;
    }

    static public ObservableList vratList() {
        return list;
    }

    static public int vratVelikost() {
        return volba;
    }
}
