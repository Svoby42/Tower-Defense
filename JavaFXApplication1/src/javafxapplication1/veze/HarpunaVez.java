package javafxapplication1.veze;

import javafxapplication1.veze.Vez;
import javafxapplication1.brouci.Nepritel;
import java.io.Serializable;
import java.util.Collections;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafxapplication1.Hra;
import javafxapplication1.Mapa;
import javafxapplication1.brouci.NepritelInterface;
import static javafxapplication1.veze.Vez.seznamVezi;

public class HarpunaVez extends Vez implements VezInterface, Serializable {

    private final int dmg;
    private int radius;
    static int cena = 2000;
    private boolean vystrelil = false;
    private final boolean podleHP = true;
    private Timeline vystrel;
    private Timeline prebijeni;
    private ImageView strela;
    private boolean pauza = false;
    private double prodleva;
    static final double POMER_VYSKA = 0.1;
    static final double POMER_SIRKA = 0.016;
    static final double POMER_RADIUS = 0.6455;
    static int radiusPocatecni;

    public HarpunaVez(Rectangle r, ObservableList list) {
        super(r, list);
        this.dmg = 200;
        this.prodleva = 3;
    }

    @Override
    public void spawnVez() {
        nastavPozici(Mapa.vratVybranyObdelnik().getX(), Mapa.vratVybranyObdelnik().getY() + 10);
        this.turret = new ImageView();
        this.turret.setFitHeight(POMER_VYSKA * Hra.vratVysku());
        this.turret.setFitWidth(POMER_SIRKA * Hra.vratSirku());
        this.radius = (int) (POMER_RADIUS * Math.sqrt(Hra.vratSirku() * Hra.vratVysku()));
        this.turret.setLayoutX(this.poziceX);
        this.turret.setLayoutY(this.poziceY - this.turret.getFitHeight() / 2);
        this.stredX = this.turret.getLayoutX() + this.turret.getFitWidth() / 2;
        this.stredY = this.turret.getLayoutY() + this.turret.getFitHeight() / 2;
        this.turret.setStyle("-fx-image: url('file:soubory/veze/harpuna_loaded.png')");
        list.add(this.turret);
        seznamVezi.add(harpunaVez);
        this.turret.setOnMousePressed((event) -> {
            if (event.isSecondaryButtonDown()) {
                ContextMenu menu = new ContextMenu();
                MenuItem prvni = new MenuItem("Rychlost(6000)");
                prvni.setOnAction((udalost) -> {
                    if (Mapa.zaplat(6000)) {
                        this.prodleva = 0.3;
                    }
                });
                menu.getItems().addAll(prvni);
                Bounds poloha = this.turret.localToScreen(this.turret.getBoundsInLocal());
                menu.show(this.turret, poloha.getMinX() + 30, poloha.getMinY());
            }
            if (event.isControlDown()) {
                prodat();
            }
        });
        this.vystrel = new Timeline();
        this.prebijeni = new Timeline();
        this.strela = new ImageView();
        this.strela.setStyle("-fx-image: url('file:soubory/veze/harpuna_strela.png')");
    }

    @Override
    public void detekuj() {
        if (!this.pauza) {
            this.listBrouku = Nepritel.vratListBrouku();
            if (this.listBrouku != null) {
                if (this.vybranyBrouk == null) {
                    if (this.podleHP) {
                        this.vybranyBrouk = trideniPodleHP();
                    } else {
                        this.vybranyBrouk = trideni();
                    }
                } else if (this.vybranyBrouk != null) {
                    this.x = this.vybranyBrouk.vratStredX() - this.turret.getLayoutX() - this.turret.getFitWidth() / 2;
                    this.y = this.vybranyBrouk.vratStredY() - this.turret.getLayoutY() - this.turret.getFitHeight() / 2;
                    this.prepona = Math.sqrt(this.x * this.x + this.y * this.y);
                    if (Math.abs(this.prepona) < this.radius && this.vybranyBrouk.jeNazivu() && this.listBrouku.contains(this.vybranyBrouk)) {
                        this.turret.setRotate((float) (90 + Math.toDegrees(Math.atan2(y, x))));
                        strilej();
                    } else {
                        if (this.podleHP) {
                            if (this.podleHP) {
                                this.vybranyBrouk = trideniPodleHP();
                                if (Math.abs(this.prepona) > this.radius) {
                                    this.vybranyBrouk = trideni();
                                }
                            }
                        } else {
                            this.vybranyBrouk = trideni();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void strilej() {
        vystrel();
    }

    @Override
    public void uklid() {
        list.remove(this.turret);
        list.remove(this.strela);
        this.prebijeni.stop();
        this.vystrel.stop();
    }

    public void vystrel() {
        if (this.vybranyBrouk != null && this.vybranyBrouk.jeNazivu()) {
            if (!this.vystrelil) {
                this.vystrelil = true;
                if (this.strela != null) {
                    list.remove(this.strela);
                }
                this.strela = new ImageView();
                this.strela.setStyle("-fx-image: url('file:soubory/veze/harpuna_strela.png')");
                this.strela.setFitHeight(this.turret.getFitHeight());
                this.strela.setFitWidth(this.turret.getFitWidth() / 1.2);
                this.strela.setLayoutX(this.turret.getLayoutX());
                this.strela.setLayoutY(this.turret.getLayoutY());
                double x2 = this.vybranyBrouk.vratStredX() - this.strela.getLayoutX() - this.strela.getFitWidth() / 2;
                double y2 = this.vybranyBrouk.vratStredY() - this.strela.getLayoutY() - this.strela.getFitHeight() / 2;
                list.add(this.strela);
                this.turret.setStyle("-fx-image: url('file:soubory/veze/harpuna_unloaded.png')");
                KeyValue x3 = new KeyValue(this.strela.xProperty(), x2);
                KeyValue y3 = new KeyValue(this.strela.yProperty(), y2);
                KeyFrame keyframe = new KeyFrame(Duration.millis(Math.sqrt(x2 * x2 + y2 * y2)), x3, y3);
                this.vystrel.getKeyFrames().add(keyframe);
                this.strela.setRotate((float) (90 + Math.toDegrees(Math.atan2(y2, x2))));
                this.vystrel.setOnFinished((ev) -> {
                    KeyFrame prebijenidoba = new KeyFrame(Duration.seconds(this.prodleva), (event) -> {
                        this.vystrelil = false;
                    });
                    this.prebijeni.getKeyFrames().add(prebijenidoba);
                    this.prebijeni.setOnFinished((event) -> {
                        this.prebijeni.getKeyFrames().clear();
                    });
                    this.prebijeni.play();
                    if (this.vybranyBrouk != null) {
                        this.vybranyBrouk.downHP(this.dmg);
                        if (!this.vybranyBrouk.jeNazivu()) {
                            this.vybranyBrouk.zabit();
                            this.listBrouku.remove(this.vybranyBrouk);
                        }
                    }
                    this.turret.setStyle("-fx-image: url('file:soubory/veze/harpuna_loaded.png')");
                    list.remove(this.strela);
                    this.vystrel.getKeyFrames().clear();

                });
                this.vystrel.play();
                zvukHarpunaVystrel();
            }
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

    public NepritelInterface trideniPodleHP() {
        if (!this.listBrouku.isEmpty()) {
            try {
                this.listBrouku = Nepritel.vratListBrouku();
                Collections.sort(this.listBrouku, (NepritelInterface t, NepritelInterface t1) -> {
                    int x1 = t.vratHP();
                    int x2 = t1.vratHP();
                    int porovnat = Integer.valueOf(x2).compareTo(x1);
                    if (porovnat != 0) {
                        return porovnat;
                    }
                    double x3 = t.vratStredX() - turret.getLayoutX();
                    double y2 = t.vratStredY() - turret.getLayoutY();
                    double prepona1 = Math.sqrt(x3 * x3 + y2 * y2);
                    double x4 = t1.vratStredX() - turret.getLayoutX();
                    double y3 = t1.vratStredY() - turret.getLayoutY();
                    double prepona2 = Math.sqrt(x4 * x4 + y3 * y3);
                    return (int) (prepona1 - prepona2);
                });
            } catch (IllegalArgumentException e) {

            }
            return this.listBrouku.get(0);
        } else {
            return trideni();
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
            this.vystrel.pause();
            this.prebijeni.pause();
            this.pauza = true;
        } else if (!ano && this.pauza) {
            this.vystrel.play();
            this.prebijeni.play();
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
        return this.radius;
    }

    static public void nastavRadiusHarpuna(int kolik) {
        radiusPocatecni = kolik;
    }

    static public int vratCenu() {
        return cena;
    }

    static public int navratRadius() {
        return (int) (POMER_RADIUS * Math.sqrt(Hra.vratSirku() * Hra.vratVysku()));
    }

}
