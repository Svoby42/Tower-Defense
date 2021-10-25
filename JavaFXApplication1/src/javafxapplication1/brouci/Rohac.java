package javafxapplication1.brouci;

import javafxapplication1.brouci.Nepritel;
import java.util.Random;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafxapplication1.Bod;
import javafxapplication1.Mapa;

public class Rohac extends Nepritel implements NepritelInterface {

    private int HP;
    private int cisloCesta;
    private int kdeVykrojit;
    private int spinac;
    private String smer = "";
    private double stredX;
    private double stredY;
    private boolean doselDoKonce = false;
    private boolean pauza;
    private Rectangle dalsi;
    private static final int PUVODNI_RYCHLOST = 2;
    private static int rychlost = PUVODNI_RYCHLOST;
    private static final double POMER_SIRKA = 0.03;
    private static final double POMER_VYSKA = 0.0633;
    private static final int MEZERA = 100;
    Random randomCesta = new Random();

    public Rohac(ObservableList list) {
        super(list);
        this.HP = 10;
        this.cisloCesta = randomCesta.nextInt(Mapa.vratPocetCest());
        this.Body = Bod.vratBody(this.cisloCesta);
        this.pocet = 1;
        this.spinac = 0;
        this.kdeVykrojit = 0;
        this.pauza = false;
        this.dalsi = Body.get(this.pocet);
    }

    static public void nastavRychlost(int kolik) {
        rychlost = kolik;
    }

    static public void resetujRychlost() {
        rychlost = PUVODNI_RYCHLOST;
    }

    @Override
    public void objevSe() {
        this.broukImage = new ImageView();
        this.broukImage.setStyle("-fx-image: url('file:soubory/brouci/rohac_sprite_sheet.png')");
        nastavZacatek(this.Body.get(0));
        list.add(this.broukImage);
        listBrouku.add(rohac);
        zmenaVelikosti();
    }

    @Override
    public void jdi() {
        if (!this.pauza) {
            for (int i = 0; i < rychlost; i++) {
                if (this.broukImage.getX() < this.dalsi.getLayoutBounds().getMaxX() && this.broukImage.getLayoutBounds().getMinX() < this.dalsi.getLayoutBounds().getMinX()) {
                    this.smer = "D";
                    this.broukImage.setX(this.broukImage.getX() + 1);
                    this.stredX += 1;
                } else if (this.broukImage.getY() < this.dalsi.getLayoutBounds().getMinY() && this.broukImage.getLayoutBounds().getMinY() < this.dalsi.getLayoutBounds().getMinY()) {
                    this.smer = "S";
                    this.broukImage.setY(this.broukImage.getY() + 1);
                    this.stredY += 1;
                } else if (this.broukImage.getY() > this.dalsi.getLayoutBounds().getMinY() && this.broukImage.getLayoutBounds().getMaxY() > this.dalsi.getLayoutBounds().getMinY()) {
                    this.smer = "W";
                    this.broukImage.setY(this.broukImage.getY() - 1);
                    this.stredY -= 1;
                } else if (this.broukImage.getX() > this.dalsi.getLayoutBounds().getMinX() && this.broukImage.getLayoutBounds().getMaxX() > this.dalsi.getLayoutBounds().getMinX()) {
                    this.smer = "A";
                    this.broukImage.setX(this.broukImage.getX() - 1);
                    this.stredX -= 1;
                } else {
                    if (this.pocet >= this.Body.size() - 1) {
                        doselDoCile();
                        break;
                    } else {
                        this.pocet++;
                        this.dalsi = this.Body.get(this.pocet);
                    }
                }
                if (this.spinac % 4 == 0) {
                    if (this.smer.equals("S")) {
                        this.broukImage.setViewport(new Rectangle2D(MEZERA * this.kdeVykrojit, 0, MEZERA, MEZERA));
                    } else if (this.smer.equals("W")) {
                        this.broukImage.setViewport(new Rectangle2D(MEZERA * this.kdeVykrojit, MEZERA, MEZERA, MEZERA));
                    } else if (this.smer.equals("A")) {
                        this.broukImage.setViewport(new Rectangle2D(MEZERA * this.kdeVykrojit, MEZERA * 2, MEZERA, MEZERA));
                    } else if (this.smer.equals("D")) {
                        this.broukImage.setViewport(new Rectangle2D(MEZERA * this.kdeVykrojit, MEZERA * 3, MEZERA, MEZERA));
                    }
                    this.kdeVykrojit++;
                    if (this.kdeVykrojit == 10) {
                        this.kdeVykrojit = 0;
                    }
                }
                this.spinac++;
            }
        }
    }

    @Override
    public void doselDoCile() {
        this.doselDoKonce = true;
        Mapa.odectiZivoty(20);
        uklid();
    }

    @Override
    public void zabit() {
        Mapa.prictiPenize(10);
        uklid();
    }

    @Override
    public void uklid() {
        list.remove(this.broukImage);
        pauza(true);
    }

    @Override
    public void downHP(int kolik) {
        this.HP -= kolik;
        prehrajZvukHit();
    }

    @Override
    public void zmenaVelikosti() {
        this.broukImage.setFitHeight(Math.round(POMER_VYSKA * Mapa.vratVyskuMapy()));
        this.broukImage.setFitWidth(Math.round(POMER_SIRKA * Mapa.vratSirkuMapy()));
        this.broukImage.setX(this.Body.get(pocet - 1).getX());
        this.broukImage.setY(this.Body.get(pocet - 1).getY());
        this.stredX = this.broukImage.getX() + this.broukImage.getFitWidth() / 2;
        this.stredY = this.broukImage.getY() + this.broukImage.getFitHeight() / 2;
    }

    @Override
    public void presun(double x, double y) {
        this.broukImage.setLayoutX(x);
        this.broukImage.setLayoutY(y);
    }

    @Override
    public void pauza(boolean ano) {
        if (ano) {
            this.pauza = true;
        } else if (!ano && this.pauza) {
            this.pauza = false;
        }

    }

    @Override
    public boolean dosel() {
        return this.doselDoKonce;
    }

    @Override
    public boolean jeNazivu() {
        return this.HP > 0;
    }

    @Override
    public double vratStredX() {
        return this.stredX;
    }

    @Override
    public double vratStredY() {
        return this.stredY;
    }

    @Override
    public int vratHP() {
        return this.HP;
    }

}
