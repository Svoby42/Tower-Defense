package javafxapplication1.veze;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafxapplication1.Hra;
import javafxapplication1.Mapa;
import javafxapplication1.brouci.NepritelInterface;

public class Vez {

    ObservableList list;
    public double poziceX;
    public double poziceY;
    public Rectangle r;
    double x;
    double y;
    ImageView turret;
    double stredX;
    double stredY;
    double prepona;
    static Rectangle[][] pole;
    static ArrayList<Rectangle> obsazeno = new ArrayList<>();
    KeyValue souradniceX;
    KeyValue souradniceY;
    KeyFrame kamStrilej;
    KeyFrame odhal;
    Circle kruh;
    int superpomoc;
    static public int velikost;
    static public Jednohlavnova jednohlavnovaVez;
    static public KulometVez kulometnaVez;
    static public RaketometVez raketometVez;
    static public LaserVez laserovaVez;
    static public HarpunaVez harpunaVez;
    static Timeline timeline;
    static boolean jePauza = false;
    public ArrayList<NepritelInterface> listBrouku;
    static ArrayList<VezInterface> seznamVezi = new ArrayList<>();
    NepritelInterface vybranyBrouk;
    Rectangle vybranej;
    File harpunaVystrel = new File("soubory/zvuky/harpuna_vystrel.mp3");
    File kulometVystrel = new File("soubory/zvuky/kulomet_davka.mp3");
    File vypaleniRakety = new File("soubory/zvuky/raketomet_vystrel.mp3");
    File vybuchRakety = new File("soubory/zvuky/raketomet_exploze.mp3");
    File laserVystrel = new File("soubory/zvuky/laser_vystrel_lepsi.mp3");
    File jednohlavnovaVystrel = new File("soubory/zvuky/jednohlavnova_vystrel_tichy.mp3");
    AudioClip klipHarpuna = new AudioClip(harpunaVystrel.toURI().toString());
    AudioClip klipKulomet = new AudioClip(kulometVystrel.toURI().toString());
    AudioClip klipRaketa = new AudioClip(vypaleniRakety.toURI().toString());
    AudioClip klipVybuch = new AudioClip(vybuchRakety.toURI().toString());
    AudioClip klipLaser = new AudioClip(laserVystrel.toURI().toString());
    AudioClip klipJednohlavnova = new AudioClip(jednohlavnovaVystrel.toURI().toString());
    static public double hlasitost = 0.1;

    public Vez(Rectangle r, ObservableList list) {
        this.r = r;
        this.list = list;
    }

    public void nastavPozici(double x, double y) {
        this.vybranej = Mapa.vratVybranyObdelnik();
        this.poziceX = x;
        this.poziceY = y;
    }

    public void zvukJednohlavnova() {
        klipJednohlavnova.play(hlasitost);
    }

    public void zvukRaketometStrela() {
        klipRaketa.play(hlasitost);
    }

    public void zvukRaketometVybuch() {
        klipVybuch.play(hlasitost);
    }

    public void zvukLaserVystrel() {
        klipLaser.play(hlasitost);
    }

    public void zvukKulometVystrel() {
        klipKulomet.play(hlasitost);
    }

    public void zvukHarpunaVystrel() {
        klipHarpuna.play(hlasitost);
    }

    static public void spawniJednohlavnovou() {
        jednohlavnovaVez = new Jednohlavnova(Mapa.vratVybranyObdelnik(), Hra.vratList());
        jednohlavnovaVez.spawnVez();
    }

    static public void spawniKulomet() {
        kulometnaVez = new KulometVez(Mapa.vratVybranyObdelnik(), Hra.vratList());
        kulometnaVez.spawnVez();
    }

    static public void spawniRaketomet() {
        raketometVez = new RaketometVez(Mapa.vratVybranyObdelnik(), Hra.vratList());
        raketometVez.spawnVez();
    }

    static public void spawniLaser() {
        laserovaVez = new LaserVez(Mapa.vratVybranyObdelnik(), Hra.vratList());
        laserovaVez.spawnVez();
    }

    static public void spawniHarpunu() {
        harpunaVez = new HarpunaVez(Mapa.vratVybranyObdelnik(), Hra.vratList());
        harpunaVez.spawnVez();
    }
    
    static public void spustVeze() {
        if (timeline == null) {
            timeline = new Timeline();
            KeyFrame keyFrame = new KeyFrame(Duration.millis(50), (event) -> {
                Vez.vratSeznam().forEach((t) -> {
                    t.detekuj();
                });
            });
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
        }
    }

    static public void zastavVeze(boolean volba) {
        Vez.vratSeznam().forEach((t) -> {
            t.pauza(volba);
        });
        jePauza = volba;
    }

    static public void uklidVeze() {
        Iterator<VezInterface> iter = Vez.vratSeznam().iterator();
        while (iter.hasNext()) {
            iter.next().uklid();
            iter.remove();
        }
    }

    static public void nastavPole(Rectangle[][] vybranePole) {
        pole = vybranePole;
    }

    static public void nastavList(ObservableList list) {
        list = Hra.vratList();
    }

    static public void nastavVelikost() {
        seznamVezi.forEach((t) -> {
            t.zmenaVelikosti();
        });
    }

    static public void ztlum(boolean ano) {
        if (ano) {
            hlasitost = 0.0;
        } else {
            hlasitost = 0.1;
        }
    }

    static public ArrayList<VezInterface> vratSeznam() {
        return seznamVezi;
    }

    static public boolean vratPauzu() {
        return jePauza;
    }

    static public int vratRadiusKulomet() {
        return KulometVez.navratRadius();
    }

    static public int vratRadiusJednohlavnova() {
        return Jednohlavnova.navratRadius();
    }

    static public int vratRadiusRaketomet() {
        return RaketometVez.navratRadius();
    }

    static public int vratRadiusLaser() {
        return LaserVez.navratRadius();
    }

    static public int vratRadiusHarpuna() {
        return HarpunaVez.navratRadius();
    }

}
