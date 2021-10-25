package javafxapplication1.veze;

import javafxapplication1.brouci.Nepritel;
import java.util.ArrayList;
import java.util.Collections;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafxapplication1.Hra;
import javafxapplication1.Mapa;
import javafxapplication1.brouci.NepritelInterface;

public class KulometVez extends Vez implements VezInterface {

    private int dmg;
    private int radius;
    private int pomoc;
    private int davka;
    private Timeline timelineVystrel;
    private boolean pauza = false;
    private static int cena = 800;
    private static int radiusPocatecni = 200;
    ArrayList<Circle> seznamStrel = new ArrayList<>();
    private static final double POMER_VYSKA = 0.07;
    private static final double POMER_SIRKA = 0.029;
    private static final double POMER_RADIUS = 0.2155;

    public KulometVez(Rectangle r, ObservableList list) {
        super(r, list);
        this.dmg = 5;
        this.davka = 20;
        this.pomoc = 0;
    }

    @Override
    public void spawnVez() {
        nastavPozici(Mapa.vratVybranyObdelnik().getX(), Mapa.vratVybranyObdelnik().getY() + 10);
        this.turret = new ImageView();
        this.turret.setFitHeight(POMER_VYSKA * Hra.vratVysku());
        this.turret.setFitWidth(POMER_SIRKA * Hra.vratSirku());
        this.radius = (int) (POMER_RADIUS * Math.sqrt(Hra.vratSirku() * Hra.vratVysku()));
        this.turret.setLayoutX(this.poziceX - Mapa.vratVybranyObdelnik().getWidth() / 2);
        this.turret.setLayoutY(this.poziceY - this.turret.getFitWidth());
        this.stredX = this.turret.getLayoutX() + this.turret.getFitWidth() / 2;
        this.stredY = this.turret.getLayoutY() + this.turret.getFitHeight() / 2;
        this.turret.setStyle("-fx-image: url('file:soubory/veze/kulomet.png')");
        list.add(this.turret);
        seznamVezi.add(kulometnaVez);
        this.timelineVystrel = new Timeline();
        this.timelineVystrel.setOnFinished((event) -> {
            if (this.vybranyBrouk != null) {
                this.vybranyBrouk.downHP(this.dmg);
                if (!this.vybranyBrouk.jeNazivu()) {
                    this.vybranyBrouk.zabit();
                    this.listBrouku.remove(this.vybranyBrouk);
                }
            }
            this.timelineVystrel.getKeyFrames().clear();
            list.remove(kruh);
        });
        this.odhal = new KeyFrame(Duration.millis(10), (event) -> {
            list.add(kruh);
        });
        this.turret.setOnMousePressed((event) -> {
            if (event.isSecondaryButtonDown()) {
                ContextMenu menu = new ContextMenu();
                MenuItem prvni = new MenuItem("Rychlost(1000)");
                prvni.setOnAction((udalost) -> {
                    if (Mapa.zaplat(1000)) {
                        this.davka = 20;
                    }
                });
                MenuItem druhy = new MenuItem("2x dmg(500)");
                druhy.setOnAction((udalost2)
                        -> {
                    if (Mapa.zaplat(500)) {
                        this.dmg *= 2;
                    }
                });
                menu.getItems().addAll(prvni, druhy);
                Bounds poloha = this.turret.localToScreen(this.turret.getBoundsInLocal());
                menu.show(this.turret, poloha.getMinX() + 30, poloha.getMinY());
            }
            if (event.isControlDown()) {
                prodat();
            }
        });
    }

    @Override
    public void detekuj() {
        if (!pauza) {
            this.listBrouku = Nepritel.vratListBrouku();
            if (this.listBrouku != null) {
                if (this.vybranyBrouk == null) {
                    this.vybranyBrouk = trideni();
                } else if (this.vybranyBrouk != null) {
                    this.x = this.vybranyBrouk.vratStredX() - this.turret.getLayoutX() - this.turret.getFitWidth() / 2;
                    this.y = this.vybranyBrouk.vratStredY() - this.turret.getLayoutY() - this.turret.getFitHeight() / 2;
                    this.prepona = Math.sqrt(this.x * this.x + this.y * this.y);
                    if (Math.abs(this.prepona) < this.radius && this.vybranyBrouk.jeNazivu() && this.listBrouku.contains(this.vybranyBrouk)) {
                        this.turret.setRotate((float) (90 + Math.toDegrees(Math.atan2(y, x))));
                        strilej();
                    } else {
                        this.vybranyBrouk = trideni();
                    }
                }
            }
        }
    }

    @Override
    public void strilej() {
        this.pomoc++;
        if (this.pomoc <= this.davka && this.pomoc % 3 == 0) {
            vystrel();
        }
        if (this.pomoc == 26) {
            this.pomoc = 1;
        }
    }

    @Override
    public void uklid() {
        list.remove(this.turret);
        list.remove(kruh);
    }

    public void vystrel() {
        if (this.vybranyBrouk != null && this.vybranyBrouk.jeNazivu()) {
            kruh = new Circle(this.stredX, this.stredY, 5);
            this.souradniceX = new KeyValue(kruh.centerXProperty(), this.vybranyBrouk.vratStredX());
            this.souradniceY = new KeyValue(kruh.centerYProperty(), this.vybranyBrouk.vratStredY());
            this.kamStrilej = new KeyFrame(Duration.millis(50), this.souradniceX, this.souradniceY);
            kruh.setFill(Color.PINK);
            kruh.setStroke(Color.BLACK);
            kruh.setStrokeWidth(2);
            this.timelineVystrel.getKeyFrames().addAll(this.kamStrilej, this.odhal);
            list.remove(kruh);
            zvukKulometVystrel();
            this.timelineVystrel.play();
        }
    }

    public NepritelInterface trideni() {
        if (!this.listBrouku.isEmpty()) {
            try {
                this.listBrouku = Nepritel.vratListBrouku();
                Collections.sort(this.listBrouku, (NepritelInterface t, NepritelInterface t1) -> {
                    double x1 = t.vratStredX() - turret.getLayoutX();
                    double y1 = t.vratStredY() - turret.getLayoutY();
                    double prepona1 = Math.sqrt(x1 * x1 + y1 * y1);
                    double x2 = t1.vratStredX() - turret.getLayoutX();
                    double y2 = t1.vratStredY() - turret.getLayoutY();
                    double prepona2 = Math.sqrt(x2 * x2 + y2 * y2);
                    return (int) (prepona1 - prepona2);
                });
            } catch (IllegalArgumentException e) {

            }
            return (NepritelInterface) this.listBrouku.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void zmenaVelikosti() {
        this.turret.setFitHeight(POMER_VYSKA * Hra.vratVysku());
        this.turret.setFitWidth(POMER_SIRKA * Hra.vratSirku());
        this.radius = (int) (POMER_RADIUS * Math.sqrt(Hra.vratSirku() * Hra.vratVysku()));
        this.stredX = this.turret.getLayoutX() + this.turret.getFitWidth() / 2;
        this.stredY = this.turret.getLayoutY() + this.turret.getFitHeight() / 2;
        radiusPocatecni = radius;
    }

    @Override
    public void presun(double x, double y) {
        zmenaVelikosti();
        this.turret.setLayoutX(x);
        this.turret.setLayoutY(y - this.turret.getFitHeight() / 2);
    }

    @Override
    public void pauza(boolean ano) {
        if (ano) {
            this.pauza = true;
            this.timelineVystrel.pause();
        } else if (!ano && this.pauza) {
            if (this.timelineVystrel.getStatus().equals(Timeline.Status.PAUSED)) {
                this.timelineVystrel.play();
            }
            this.pauza = false;
        }
    }

    @Override
    public void prodat() {
        Mapa.vratObsazene().remove(this.r);
        Vez.vratSeznam().remove(this);
        Mapa.prictiPenize(0.7 * cena);
        uklid();
    }

    @Override
    public int vratRadius() {
        return radius;
    }

    static public void nastavRadiusKulomet(int kolik) {
        radiusPocatecni = kolik;
    }

    static public int vratCenu() {
        return cena;
    }

    static public int navratRadius() {
        return (int) (POMER_RADIUS * Math.sqrt(Hra.vratSirku() * Hra.vratVysku()));
    }

}
