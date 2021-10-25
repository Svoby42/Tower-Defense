package javafxapplication1.veze;

import javafxapplication1.brouci.Nepritel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

public class RaketometVez extends Vez implements VezInterface {

    private final int dmg;
    private int radius;
    static int cena = 3000;
    private int radiusExploze;
    static int radiusPocatecni = 500;
    private Color barvaVybuchu;
    private boolean vystrelil = false;
    private boolean pauza = false;
    private boolean vybuchl = false;
    private boolean mrazak = false;
    private Timeline vystrel;
    private Timeline vybuch;
    private Timeline prebijeni;
    private Circle bum;
    private ImageView raketa;
    static public ArrayList<NepritelInterface> seznamZmrazenejch = new ArrayList<>();
    static final double POMER_VYSKA = 0.1;
    static final double POMER_SIRKA = 0.016;
    static final double POMER_RADIUS = 0.6455;
    static final double POMER_VYBUCH = 0.09;

    public RaketometVez(Rectangle r, ObservableList list) {
        super(r, list);
        this.dmg = 20;
    }

    @Override
    public void spawnVez() {
        nastavPozici(Mapa.vratVybranyObdelnik().getX(), Mapa.vratVybranyObdelnik().getY() + 10);
        this.turret = new ImageView();
        this.turret.setFitHeight(POMER_VYSKA * Hra.vratVysku());
        this.turret.setFitWidth(POMER_SIRKA * Hra.vratSirku());
        this.radius = (int) (POMER_RADIUS * Math.sqrt(Hra.vratSirku() * Hra.vratVysku()));
        this.radiusExploze = (int) (POMER_VYBUCH * Math.sqrt(Hra.vratVysku() * Hra.vratSirku()));
        this.turret.setLayoutX(this.poziceX);
        this.turret.setLayoutY(this.poziceY - this.turret.getFitHeight() / 2);
        this.stredX = this.turret.getLayoutX() + this.turret.getFitWidth() / 2;
        this.stredY = this.turret.getLayoutY() + this.turret.getFitHeight() / 2;
        this.turret.setStyle("-fx-image: url('file:soubory/veze/raketometLoaded.png')");
        list.add(this.turret);
        seznamVezi.add(raketometVez);
        this.turret.setOnMousePressed((event) -> {
            if (event.isSecondaryButtonDown()) {
                ContextMenu menu = new ContextMenu();
                MenuItem prvni = new MenuItem("Mrazák(1000)");
                prvni.setOnAction((udalost) -> {
                    if (Mapa.zaplat(1000)) {
                        this.barvaVybuchu = Color.SKYBLUE;
                        this.mrazak = true;
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
        this.barvaVybuchu = Color.ORANGE;
        this.vystrel = new Timeline();
        this.vybuch = new Timeline();
        this.prebijeni = new Timeline();
        this.raketa = new ImageView();
        this.raketa.setStyle("-fx-image: url('file:soubory/veze/raketa.png')");
    }
    
    @Override
    public void detekuj() {
        if (!this.pauza) {
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
        vystrel();
    }

    @Override
    public void uklid() {
        list.remove(this.turret);
        list.remove(this.raketa);
        list.remove(raketa);
        list.remove(this.bum);
        list.remove(bum);
        this.prebijeni.stop();
        this.vystrel.stop();
    }

    public void reset() {
        this.turret.setRotate(0);
    }

    public void zvukVystrelu() {
        klipRaketa.play(0.1);
    }

    public void zvukVybuchu() {
        klipVybuch.play(0.1);
    }

    public void vystrel() {
        if (this.vybranyBrouk != null && this.vybranyBrouk.jeNazivu()) {
            if (!this.vystrelil) {
                this.vystrelil = true;
                if (this.raketa != null) {
                    list.remove(this.raketa);
                }
                this.raketa = new ImageView();
                this.raketa.setStyle("-fx-image: url('file:soubory/veze/raketa.png')");
                this.raketa.setFitHeight(this.turret.getFitHeight());
                this.raketa.setFitWidth(this.turret.getFitWidth() / 2);
                this.raketa.setLayoutX(this.turret.getLayoutX());
                this.raketa.setLayoutY(this.turret.getLayoutY());
                double x2 = this.vybranyBrouk.vratStredX() - this.raketa.getLayoutX() - this.raketa.getFitWidth() / 2;
                double y2 = this.vybranyBrouk.vratStredY() - this.raketa.getLayoutY() - this.raketa.getFitHeight() / 2;
                list.add(this.raketa);
                this.turret.setStyle("-fx-image: url('file:soubory/veze/raketometUnloaded.png')");
                KeyValue x3 = new KeyValue(this.raketa.xProperty(), x2);
                KeyValue y3 = new KeyValue(this.raketa.yProperty(), y2);
                KeyFrame keyframe = new KeyFrame(Duration.millis(Math.sqrt(x2 * x2 + y2 * y2)), x3, y3);
                double vybuchX = this.vybranyBrouk.vratStredX();
                double vybuchY = this.vybranyBrouk.vratStredY();
                this.raketa.setRotate((float) (90 + Math.toDegrees(Math.atan2(y2, x2))));
                this.bum = new Circle(vybuchX, vybuchY, 5, this.barvaVybuchu);
                KeyFrame buch = new KeyFrame(keyframe.getTime(), (event) -> {
                    list.add(this.bum);
                    KeyValue prvni = new KeyValue(this.bum.radiusProperty(), this.radiusExploze);
                    KeyFrame keyframedalsi = new KeyFrame(Duration.millis(200), prvni);
                    this.vybuch.getKeyFrames().add(keyframedalsi);
                    this.turret.setStyle("-fx-image: url('file:soubory/veze/raketometLoaded.png')");
                    this.vybuch.setOnFinished((event2) -> {
                        list.remove(this.bum);
                        this.vybuch.getKeyFrames().clear();
                    });
                    this.vybuch.play();
                    list.remove(this.raketa);
                    this.vybuchl = false;
                });
                KeyFrame detekce = new KeyFrame(keyframe.getTime(), (event) -> {
                    if (!this.vybuchl) {
                        detekceVybuchu();
                        zvukRaketometVybuch();
                    }
                    kontrolaZivych();
                    KeyFrame prodleva = new KeyFrame(Duration.seconds(3), (event2) -> {
                        this.vystrelil = false;
                    });
                    this.prebijeni.getKeyFrames().add(prodleva);
                    this.prebijeni.setOnFinished((event3) -> {
                        this.prebijeni.getKeyFrames().clear();
                    });
                    this.prebijeni.play();
                });
                this.vystrel.setOnFinished((event) -> {
                    this.vystrel.getKeyFrames().clear();
                });
                this.vystrel.getKeyFrames().addAll(keyframe, buch, detekce);
                zvukRaketometStrela();
                this.vystrel.play();
            }
        }
    }

    public void detekceVybuchu() {
        for (NepritelInterface zasazenej : Nepritel.vratListBrouku()) {
            if ((Math.abs(zasazenej.vratStredX() - this.bum.getCenterX()) < this.radiusExploze) && (Math.abs(zasazenej.vratStredY() - this.bum.getCenterY()) < this.radiusExploze)) {
                if (this.mrazak) {
                    seznamZmrazenejch.add(zasazenej);
                    zasazenej.pauza(true);
                    Timeline mraz;
                    mraz = new Timeline(new KeyFrame(Duration.seconds(3), (ActionEvent event) -> {
                        if (!this.pauza) {
                            zasazenej.pauza(false);
                            seznamZmrazenejch.remove(zasazenej);
                        }
                    }));
                    KeyFrame kontrola = new KeyFrame(Duration.millis(50), (event) -> {
                        if (this.pauza) {
                            seznamZmrazenejch.clear();
                        }
                    });
                    mraz.getKeyFrames().add(kontrola);
                    mraz.play();
                    mraz.setOnFinished((event) -> {
                        mraz.stop();
                    });
                } else {
                    zasazenej.downHP(this.dmg);
                }
            }
        }
        this.vybuchl = true;
    }

    public void kontrolaZivych() {
        Iterator<NepritelInterface> it = Nepritel.vratListBrouku().iterator();
        while (it.hasNext()) {
            NepritelInterface pomocny = it.next();
            if (!pomocny.jeNazivu()) {
                it.remove();
                Nepritel.vratListBrouku().remove(pomocny);
                this.listBrouku.remove(pomocny);
                pomocny.zabit();
                if (this.vybranyBrouk == pomocny) {
                    this.vybranyBrouk = null;
                }
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

    @Override
    public void zmenaVelikosti() {
        this.turret.setFitHeight(POMER_VYSKA * Hra.vratVysku());
        this.turret.setFitWidth(POMER_SIRKA * Hra.vratSirku());
        this.radius = (int) (POMER_RADIUS * Math.sqrt(Hra.vratSirku() * Hra.vratVysku()));
        this.radiusExploze = (int) (POMER_VYBUCH * Math.sqrt(Hra.vratVysku() * Hra.vratSirku()));
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
            this.pauza = true;
        } else if (!ano && this.pauza) {
            if (this.vystrel.getStatus().equals(Timeline.Status.PAUSED)) {
                this.vystrel.play();
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
        return this.radius;
    }

    static public ArrayList<NepritelInterface> vratSeznamZmrazenejch() {
        return seznamZmrazenejch;
    }

    static public void nastavRadiusRaketomet(int kolik) {
        radiusPocatecni = kolik;
    }

    static public int vratCenu() {
        return cena;
    }

    static public int navratRadius() {
        return (int) (POMER_RADIUS * Math.sqrt(Hra.vratSirku() * Hra.vratVysku()));
    }

}
