package javafxapplication1;

import java.util.ArrayList;
import java.util.Random;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Bod {

    private int X;
    private int Y;
    public Rectangle[][] pole;
    public Rectangle posledniBod;
    public Rectangle bod;
    public Rectangle bod2;
    public Rectangle bod3;
    static ArrayList<Rectangle> Body = new ArrayList<>();
    static ArrayList<Rectangle> Body2 = new ArrayList<>();
    static ArrayList<Rectangle> Body3 = new ArrayList<>();
    static ArrayList<Rectangle> Body11 = new ArrayList<>();
    static ArrayList<Rectangle> Body21 = new ArrayList<>();
    static ArrayList<Rectangle> Body31 = new ArrayList<>();
    static ArrayList<Rectangle> Body41 = new ArrayList<>();
    static ArrayList<Rectangle> Body51 = new ArrayList<>();
    static ArrayList<Rectangle> Body61 = new ArrayList<>();

    static Random random = new Random();
    static int x;
    static ObservableList list;

    public Bod(Rectangle[][] pole, ObservableList list) {
        this.pole = pole;
        Bod.list = list;
    }

    public void Spawn(int X, int Y) {
        this.bod = new Rectangle();
        this.bod.setWidth(5);
        this.bod.setHeight(5);
        this.bod.setFill(Color.BLACK);
        Body.add(this.bod);
        this.X = X;
        this.Y = Y;
        Spawn2(X, Y);
        Spawn3(X, Y);
    }

    public void Spawn2(int X, int Y) {
        this.bod2 = new Rectangle();
        this.bod2.setWidth(5);
        this.bod2.setHeight(5);
        this.bod2.setFill(Color.BLACK);
        Body2.add(this.bod2);
        this.X = X;
        this.Y = Y;
    }

    public void Spawn3(int X, int Y) {
        this.bod3 = new Rectangle();
        this.bod3.setWidth(5);
        this.bod3.setHeight(5);
        this.bod3.setFill(Color.BLACK);
//        list.add(this.bod);
//        list.add(this.bod2);
//        list.add(this.bod3);
        Body3.add(this.bod3);
        this.X = X;
        this.Y = Y;
    }

    public void Spawn11(int X, int Y) {
        this.bod = new Rectangle();
        this.bod.setWidth(5);
        this.bod.setHeight(5);
        this.bod.setFill(Color.BLACK);
        Body11.add(this.bod);
        this.X = X;
        this.Y = Y;
        Spawn21(X, Y);
        Spawn31(X, Y);
    }

    public void Spawn21(int X, int Y) {
        this.bod2 = new Rectangle();
        this.bod2.setWidth(5);
        this.bod2.setHeight(5);
        this.bod2.setFill(Color.BLACK);
        Body21.add(this.bod2);
        this.X = X;
        this.Y = Y;
    }

    public void Spawn31(int X, int Y) {
        this.bod3 = new Rectangle();
        this.bod3.setWidth(5);
        this.bod3.setHeight(5);
        this.bod3.setFill(Color.BLACK);
        Body31.add(this.bod3);
        this.X = X;
        this.Y = Y;
    }

    public void Spawn41(int X, int Y) {
        this.bod = new Rectangle();
        this.bod.setWidth(5);
        this.bod.setHeight(5);
        this.bod.setFill(Color.BLACK);
        Body41.add(this.bod);
        this.X = X;
        this.Y = Y;
        Spawn51(X, Y);
        Spawn61(X, Y);
    }

    public void Spawn51(int X, int Y) {
        this.bod2 = new Rectangle();
        this.bod2.setWidth(5);
        this.bod2.setHeight(5);
        this.bod2.setFill(Color.BLACK);
        Body51.add(this.bod2);
        this.X = X;
        this.Y = Y;
    }

    public void Spawn61(int X, int Y) {
        this.bod3 = new Rectangle();
        this.bod3.setWidth(5);
        this.bod3.setHeight(5);
        this.bod3.setFill(Color.BLACK);
        Body61.add(this.bod3);
        this.X = X;
        this.Y = Y;
    }

    public void presun(double X, double Y) {
        this.bod.setX(Math.round(((X + (Hra.vratSirku() / 100)) / 2) * 2));
        this.bod.setY(Math.round(((Y - (Hra.vratVysku() / 20)) / 2) * 2));
        this.bod2.setX(Math.round(((X + (Hra.vratSirku() / 100)) / 2) * 2));
        this.bod2.setY(Math.round(((Y + (Hra.vratVysku() / 100)) / 2) * 2));
        this.bod3.setX(Math.round(((X - (Hra.vratSirku() / 100)) / 2) * 2));
        this.bod3.setY(Math.round(((Y + (Hra.vratVysku() / 90)) / 2) * 2));
    }

    public int vratX() {
        return this.X;
    }

    public int vratY() {
        return this.Y;
    }

    static public ArrayList vratBody(int jake) {
        switch (jake) {
            case 0:
                return Body;
            case 1:
                return Body2;
            case 2:
                return Body3;
            default:
                break;
        }
        if (jake >= 3 && !Body11.isEmpty()) {
            switch (jake) {
                case 3:
                    return Body21;
                case 4:
                    return Body31;
                case 5:
                    return Body41;
                default:
                    break;
            }
        }

        switch (jake) {
            case 0:
                return Body;
            case 1:
                return Body2;
            case 2:
                return Body3;
            case 3:
                if (!Body11.isEmpty()) {
                    return Body11;
                }
            case 4:
                if (!Body21.isEmpty()) {
                    return Body21;
                }
            case 5:
                if (!Body31.isEmpty()) {
                    return Body31;
                }
                if (jake > 6) {

                }
            default:
                return Body;
        }
    }

    static public void uklid() {
        Body.clear();
        Body2.clear();
        Body3.clear();
        Body11.clear();
        Body21.clear();
        Body31.clear();
        Body41.clear();
        Body51.clear();
        Body61.clear();
    }

}
