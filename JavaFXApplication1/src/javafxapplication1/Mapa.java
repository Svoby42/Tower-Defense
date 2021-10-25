package javafxapplication1;

import javafxapplication1.veze.HarpunaVez;
import javafxapplication1.veze.Jednohlavnova;
import javafxapplication1.veze.KulometVez;
import javafxapplication1.veze.LaserVez;
import javafxapplication1.veze.RaketometVez;
import javafxapplication1.veze.Vez;
import javafxapplication1.brouci.Nepritel;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Mapa {

    static ObservableList list;
    static Rectangle[][] pole = new Rectangle[30][50];
    static ImageView[][] cesty = new ImageView[30][50];
    static Rectangle posledniBod;
    static Rectangle zacatek;
    static Rectangle vybranyObdelnik;
    static ArrayList<Rectangle> poziceVezi = new ArrayList<>();
    static ArrayList<Rectangle> zakazano = new ArrayList<>();
    static ArrayList<Rectangle> zabrano = new ArrayList<>();
    static ArrayList<Rectangle> kopiePole = new ArrayList<>();
    static ArrayList poziceVeze = new ArrayList();
    static CheckBox volbaDosah;
    static boolean dosahPovolen = true;
    static Button kulometButton;
    static Button jednohlavnovaButton;
    static Button raketometButton;
    static Button laserButton;
    static Button harpunaButton;
    static Button pauzaButton;
    static Button restartButton;
    static Button zvukButton;
    static ImageView ikona;
    static ImageView ikona2;
    static ImageView ikona3;
    static ImageView ikona4;
    static ImageView ikona5;
    static ImageView ikona6;
    static ImageView ikona7;
    static ImageView ikona8;
    static int jakaVez = 1;
    static Label zustatekLabel;
    static Label zivotyLabel;
    static Label infoVezLabel;
    static Label casLabel;
    static Label sekundyLabel;
    static ArrayList<Bod> seznamBodObjkt = new ArrayList<>();
    static int sirkaButtonu = 100;
    static int vyskaButtonu = 50;
    static int penize = 1000;
    static int posledniPenize;
    static int zbyvajiciZivoty = 1000;
    static boolean prohral;
    static int kolikataNactena = 1;
    static ImageView pozadi = new ImageView();
    static int posledniX = 0;
    static int posledniY = 0;
    static int posledniX2 = 0;
    static int posledniY2 = 0;
    static int pocetCest;
    static ArrayList<Button> seznamButtonuVezi = new ArrayList<>();
    static ImageView radius = new ImageView();
    static HashSet<ImageView> listCesty = new HashSet<>();
    static ArrayList<String> seznamTextur = new ArrayList<>();
    static ArrayList<String> seznamPozadi = new ArrayList<>();
    static Timeline vyherni;
    static Runnable aktualniMapa;
    static Runnable dalsiMapa;
    static final double POMER_POZADI_SIRKA = 0.67;
    static final double POMER_POZADI_VYSKA = 1.1;
    static final double POMER_CTVERCU = 0.025;

    static public void vykresliPrvniMapu() {
        vykreslitMapu(1);
    }

    static public void vykreslitMapu(int jakou) {
        vygenerujPlochu();
        Hra.zmenaVelikosti();
        switch (jakou) {
            case 1:
                prvniMapa();
                break;
            case 2:
                druhaMapa();
                break;
            case 3:
                tretiMapa();
                break;
            case 4:
                ctvrtaMapa();
                break;
            case 5:
                pataMapa();
                break;
            case 6:
                sestaMapa();
                break;
            case 7:
                sedmaMapa();
                break;
            case 8:
                osmaMapa();
                break;
            case 9:
                devataMapa();
                break;
            default:
                break;
        }
        nastavStylCesty();
    }

    static public void nastav(Runnable dalsiAkce) {
        vygenerujPlochu();
        dalsiAkce.run();
        nastavStylCesty();
        Hra.zmenaVelikosti();
    }

    static private void prvniMapa() {
        nastavPozadi();
        cestaVpravo(1, 40, 3);
        cestaDolu(3, 10, 40);
        cestaVlevo(40, 5, 10);
        cestaDolu(10, 20, 5);
        cestaVpravo(5, 40, 20);
        cestaDolu(20, 28, 40);
        pocetCest = 3;
        kolikataNactena = 1;
        aktualniMapa = () -> prvniMapa();
        dalsiMapa = () -> druhaMapa();
    }

    static private void druhaMapa() {
        nastavPozadi();
        cestaVpravo(1, 9, 12);
        cestaNahoru(12, 5, 9);
        cestaVpravo(9, 17, 5);
        cestaDolu(5, 17, 17);
        cestaVpravo(17, 27, 17);
        cestaNahoru(17, 11, 27);
        cestaVpravo(27, 48, 11);
        pocetCest = 3;
        kolikataNactena = 2;
        aktualniMapa = () -> druhaMapa();
        dalsiMapa = () -> tretiMapa();
    }

    static private void tretiMapa() {
        nastavPozadi();
        cestaDolu(1, 3, 20);
        cestaVlevo(20, 4, 3);
        cestaDolu(3, 25, 4);
        cestaVpravo(4, 30, 25);
        cestaNahoru(25, 15, 30);
        cestaVlevo(30, 23, 15);
        cestaDolu(15, 19, 23);
        cestaVlevo(23, 12, 19);
        cestaNahoru(19, 9, 12);
        cestaVpravo(12, 40, 9);
        cestaDolu(9, 28, 40);
        pocetCest = 3;
        kolikataNactena = 3;
        aktualniMapa = () -> tretiMapa();
        dalsiMapa = () -> ctvrtaMapa();
    }

    static private void ctvrtaMapa() {
        nastavPozadi();
        cestaDolu(1, 4, 5);
        cestaVpravo(5, 40, 4);
        cestaDolu(4, 11, 40);
        cestaVlevo(40, 5, 11);
        cestaDolu(11, 25, 5);
        cestaVpravo(5, 48, 25);
        pocetCest = 3;
        kolikataNactena = 4;
        aktualniMapa = () -> ctvrtaMapa();
        dalsiMapa = () -> pataMapa();
    }

    static private void pataMapa() {
        nastavPozadi();
        cestaVpravo(1, 48, 15);
        cestaVpravo2(1, 48, 10);
        pocetCest = 5;
        kolikataNactena = 5;
        aktualniMapa = () -> pataMapa();
        dalsiMapa = () -> sestaMapa();
    }

    static private void sestaMapa() {
        nastavPozadi();
        cestaNahoru(28, 8, 25);
        cestaVlevo(25, 10, 8);
        cestaDolu(8, 28, 10);
        cestaNahoru2(28, 8, 25);
        cestaVpravo2(25, 40, 8);
        cestaDolu2(8, 28, 40);
        pocetCest = 5;
        kolikataNactena = 6;
        aktualniMapa = () -> sestaMapa();
        dalsiMapa = () -> sedmaMapa();
    }

    static private void sedmaMapa() {
        nastavPozadi();
        cestaNahoru(28, 8, 25);
        cestaVlevo(25, 10, 8);
        cestaDolu(8, 24, 10);
        cestaVlevo(10, 2, 24);
        cestaNahoru(24, 1, 2);
        cestaNahoru2(28, 8, 25);
        cestaVpravo2(25, 40, 8);
        cestaDolu2(8, 24, 40);
        cestaVpravo2(40, 48, 24);
        cestaNahoru2(24, 1, 48);
        pocetCest = 5;
        kolikataNactena = 7;
        aktualniMapa = () -> sedmaMapa();
        dalsiMapa = () -> osmaMapa();
    }

    static private void osmaMapa() {
        nastavPozadi();
        cestaNahoru(28, 6, 5);
        cestaVpravo(5, 44, 6);
        cestaDolu(6, 28, 44);
        cestaNahoru2(28, 12, 38);
        cestaVlevo2(38, 11, 12);
        cestaDolu2(12, 28, 11);
        pocetCest = 5;
        kolikataNactena = 8;
        aktualniMapa = () -> osmaMapa();
        dalsiMapa = () -> devataMapa();
    }

    static private void devataMapa() {
        nastavPozadi();
        cestaNahoru(28, 5, 5);
        cestaVpravo(5, 44, 5);
        cestaDolu(5, 12, 44);
        cestaVlevo(44, 10, 12);
        cestaDolu(12, 19, 10);
        cestaVpravo(10, 40, 19);
        cestaDolu(19, 28, 40);
        cestaNahoru2(28, 19, 40);
        cestaVlevo2(40, 10, 19);
        cestaNahoru2(19, 12, 10);
        cestaVpravo2(10, 44, 12);
        cestaNahoru2(12, 5, 44);
        cestaVlevo2(44, 5, 5);
        cestaDolu2(5, 28, 5);
        pocetCest = 5;
        kolikataNactena = 9;
        aktualniMapa = () -> devataMapa();
        dalsiMapa = () -> prvniMapa();
    }

    static public void dalsiUroven() {
        Vez.uklidVeze();
        Bod.uklid();
        Vez.zastavVeze(false);
        uklid();
        nastav(() -> {
            dalsiMapa.run();
        });
        Nepritel.spustDalsiLevel();
    }

    static public void restartujUroven() {
        if (vyherni != null) {
            vyherni.pause();
        }
        prohral = false;
        zbyvajiciZivoty = 1000;
        zivotyLabel.setText(String.valueOf(zbyvajiciZivoty) + " HP");
        nastavPenize(posledniPenize);
        Nepritel.uklidBrouky();
        Vez.uklidVeze();
        Nepritel.restartujLevel();
        Vez.zastavVeze(false);
        zabrano.clear();
        poziceVezi.clear();
    }

    static public void uklid() {
        for (int e = 0; e < pole.length; e++) {
            for (int i = 0; i < pole[0].length; i++) {
                list.remove(pole[e][i]);
                list.remove(cesty[e][i]);
            }
        }
        list.forEach((t) -> {
            if (t instanceof Rectangle) {
                list.remove(t);
            }
        });
        list.remove(pozadi);
        list.remove(radius);
        listCesty.clear();
        zakazano.clear();
        zabrano.clear();
        posledniX = 0;
        posledniY = 0;
        posledniX2 = 0;
        posledniY2 = 0;
    }

    static private void nastavPozadi() {
        File f = new File("soubory/pozadi");
        String[] cestyTextury = f.list();
        for (String cesta : cestyTextury) {
            seznamPozadi.add(String.format("-fx-image: url('file:soubory/pozadi/%s')", cesta));
        }
        int random = (int) (Math.random() * seznamPozadi.size());
        pozadi.setStyle(seznamPozadi.get(random));
        pozadi.setX(0);
        pozadi.setY(0);
    }

    static private void nastavStylCesty() {
        File f = new File("soubory/textury");
        String[] cestyTextury = f.list();
        for (String cesta : cestyTextury) {
            seznamTextur.add(String.format("-fx-image: url('file:soubory/textury/%s')", cesta));
        }
        int random = (int) (Math.random() * seznamTextur.size());
        listCesty.forEach((t) -> {
            t.setStyle(seznamTextur.get(random));
        });

        zakazano.forEach((t) -> {
            t.setVisible(false);
        });
    }

    static private void vygenerujPlochu() {
        list.add(pozadi);
        for (int e = 0; e < pole.length; e++) {
            for (int i = 0; i < pole[0].length; i++) {
                Rectangle r = new Rectangle();
                ImageView pepa = new ImageView();
                list.add(pepa);
                list.add(r);
                r.setFill(Color.TRANSPARENT);
                r.setX(20 * (i));
                r.setY(20 * (e));
                pepa.setX(r.getX());
                pepa.setY(r.getY());
                r.setWidth(20);
                r.setHeight(20);
                pepa.setFitWidth(r.getWidth());
                pepa.setFitHeight(r.getHeight());
                r.setOnMouseEntered((event) -> {
                    if (!list.contains(radius) && dosahPovolen) {
                        radius.setStyle("-fx-image: url('file:radius.png')");
                        list.add(radius);
                    }
                    int vybranaHodnota = 0;
                    switch (jakaVez) {
                        case 1:
                            vybranaHodnota = Vez.vratRadiusKulomet();
                            break;
                        case 2:
                            vybranaHodnota = Vez.vratRadiusJednohlavnova();
                            break;
                        case 3:
                            vybranaHodnota = Vez.vratRadiusRaketomet();
                            break;
                        case 4:
                            vybranaHodnota = Vez.vratRadiusLaser();
                            break;
                        case 5:
                            vybranaHodnota = Vez.vratRadiusHarpuna();
                            break;
                    }
                    radius.setFitHeight(vybranaHodnota * 2);
                    radius.setFitWidth(vybranaHodnota * 2);
                    radius.setX(r.getX() - vybranaHodnota);
                    radius.setY(r.getY() - vybranaHodnota);
                    if (!zabrano.contains(r)) {
                        r.setFill(Color.BLACK);
                    }
                    if (zakazano.contains(r) && !zabrano.contains(r)) {
                        r.setFill(Color.RED);
                    }
                });
                r.setOnMouseExited((event) -> {
                    if (!zabrano.contains(r)) {
                        r.setFill(Color.TRANSPARENT);
                    }
                });
                r.setOnMousePressed((event) -> {
                    if (!zakazano.contains(r) && !zabrano.contains(r) && !Vez.vratPauzu()) {
                        vybranyObdelnik = r;
                        switch (jakaVez) {
                            case 1:
                                if (zaplat(KulometVez.vratCenu())) {
                                    Vez.spawniKulomet();
                                }
                                break;
                            case 2:
                                if (zaplat(Jednohlavnova.vratCenu())) {
                                    Vez.spawniJednohlavnovou();
                                }
                                break;
                            case 3:
                                if (zaplat(RaketometVez.vratCenu())) {
                                    Vez.spawniRaketomet();
                                }
                                break;
                            case 4:
                                if (zaplat(LaserVez.vratCenu())) {
                                    Vez.spawniLaser();
                                }
                                break;
                            case 5:
                                if (zaplat(HarpunaVez.vratCenu())) {
                                    Vez.spawniHarpunu();
                                }
                                break;
                            default:
                                break;
                        }
                        zabrano.add(r);
                        poziceVezi.add(r);
                        r.setFill(Color.TRANSPARENT);
                    }
                });
                kopiePole.add(r);
                pole[e][i] = r;
                cesty[e][i] = pepa;
                pepa.setVisible(true);
                r.setVisible(true);
            }
        }
    }

    static int yLayoutButtony = 120;

    static public void buttony() {
        int xLayout = 1000;
        kulometButton = new Button();
        kulometButton.setLayoutX(xLayout);
        kulometButton.setLayoutY(yLayoutButtony);
        kulometButton.setMinSize(sirkaButtonu, vyskaButtonu);
        kulometButton.setMaxSize(sirkaButtonu, vyskaButtonu);

        jednohlavnovaButton = new Button();
        jednohlavnovaButton.setLayoutX(xLayout);
        jednohlavnovaButton.setLayoutY(yLayoutButtony + 50);
        jednohlavnovaButton.setMinSize(sirkaButtonu, vyskaButtonu);
        jednohlavnovaButton.setMaxSize(sirkaButtonu, vyskaButtonu);

        raketometButton = new Button();
        raketometButton.setLayoutX(xLayout);
        raketometButton.setLayoutY(yLayoutButtony + 100);
        raketometButton.setMinSize(sirkaButtonu, vyskaButtonu);
        raketometButton.setMaxSize(sirkaButtonu, vyskaButtonu);

        laserButton = new Button();
        laserButton.setLayoutX(xLayout);
        laserButton.setLayoutY(yLayoutButtony + 150);
        laserButton.setMinSize(sirkaButtonu, vyskaButtonu);
        laserButton.setMaxSize(sirkaButtonu, vyskaButtonu);

        harpunaButton = new Button();
        harpunaButton.setLayoutX(xLayout);
        harpunaButton.setLayoutY(yLayoutButtony + 200);
        harpunaButton.setMinSize(sirkaButtonu, vyskaButtonu);
        harpunaButton.setMaxSize(sirkaButtonu, vyskaButtonu);

        ikona = new ImageView();
        ikona2 = new ImageView();
        ikona3 = new ImageView();
        ikona4 = new ImageView();
        ikona5 = new ImageView();
        ikona6 = new ImageView();
        ikona7 = new ImageView();
        ikona8 = new ImageView();

        ikona8.setFitHeight(50);
        ikona8.setFitWidth(50);
        ikona7.setFitHeight(50);
        ikona7.setFitWidth(50);
        ikona6.setFitWidth(50);
        ikona6.setFitHeight(50);
        ikona5.setFitWidth(sirkaButtonu);
        ikona5.setFitHeight(vyskaButtonu);
        ikona4.setFitWidth(sirkaButtonu);
        ikona4.setFitHeight(vyskaButtonu);
        ikona3.setFitWidth(sirkaButtonu);
        ikona3.setFitHeight(vyskaButtonu);
        ikona2.setFitWidth(sirkaButtonu);
        ikona2.setFitHeight(vyskaButtonu);
        ikona.setFitWidth(sirkaButtonu);
        ikona.setFitHeight(vyskaButtonu);

        ikona.setStyle("-fx-image: url('file:soubory/buttony/jednohlavnova_ikona.png')");
        ikona2.setStyle("-fx-image: url('file:soubory/buttony/kulomet_ikona.png')");
        ikona3.setStyle("-fx-image: url('file:soubory/buttony/raketomet_ikona.png')");
        ikona4.setStyle("-fx-image: url('file:soubory/buttony/laser_ikona.png')");
        ikona5.setStyle("-fx-image: url('file:soubory/buttony/harpuna_ikona.png')");
        ikona6.setStyle("-fx-image: url('file:soubory/buttony/pauza.png')");
        ikona7.setStyle("-fx-image: url('file:soubory/buttony/restart.png')");
        ikona8.setStyle("-fx-image: url('file:soubory/buttony/zvukOn.png')");

        kulometButton.setGraphic(ikona2);
        jednohlavnovaButton.setGraphic(ikona);
        raketometButton.setGraphic(ikona3);
        laserButton.setGraphic(ikona4);
        harpunaButton.setGraphic(ikona5);

        pauzaButton = new Button();
        pauzaButton.setLayoutX(xLayout);
        pauzaButton.setLayoutY(370);
        pauzaButton.setMinSize(50, 50);
        pauzaButton.setMaxSize(50, 50);
        pauzaButton.setGraphic(ikona6);
        pauzaButton.setOnAction((event)
                -> {
            if (!Vez.vratPauzu()) {
                Vez.zastavVeze(true);
                Nepritel.zastavBroukyAVlnu(true);
                ikona6.setStyle("-fx-image: url('file:soubory/buttony/play.png')");
            } else if (Vez.vratPauzu()) {
                Vez.zastavVeze(false);
                Nepritel.zastavBroukyAVlnu(false);
                ikona6.setStyle("-fx-image: url('file:soubory/buttony/pauza.png')");
            }

            Iterator it = list.iterator();
            while (it.hasNext()) {
                if (it.next() instanceof Circle) {
                    it.remove();
                }
            }
        });

        restartButton = new Button();
        restartButton.setLayoutX(xLayout + 100);
        restartButton.setLayoutY(370);
        restartButton.setMinSize(50, 50);
        restartButton.setMaxSize(50, 50);
        restartButton.setGraphic(ikona7);

        restartButton.setOnAction((event)
                -> {
            Mapa.restartujUroven();
        });

        zvukButton = new Button();
        zvukButton.setLayoutX(xLayout + 150);
        zvukButton.setLayoutY(370);
        zvukButton.setMinSize(50, 50);
        zvukButton.setMaxSize(50, 50);
        zvukButton.setGraphic(ikona8);

        zvukButton.setOnAction((ActionEvent event)
                -> {
            if (Hra.jeTicho()) {
                ikona8.setStyle("-fx-image: url('file:soubory/buttony/zvukOn.png')");
            } else {
                ikona8.setStyle("-fx-image: url('file:soubory/buttony/zvukOff.png')");
            }
            Hra.ztlumit(!Hra.jeTicho());
        });

        kulometButton.setOnAction((event)
                -> {
            zvyrazni((Button) event.getSource());
            jakaVez = 1;
        });
        jednohlavnovaButton.setOnAction((event)
                -> {
            zvyrazni((Button) event.getSource());
            jakaVez = 2;
        });
        raketometButton.setOnAction((event)
                -> {
            zvyrazni((Button) event.getSource());
            jakaVez = 3;
        });
        laserButton.setOnAction((event)
                -> {
            zvyrazni((Button) event.getSource());
            jakaVez = 4;
        });

        harpunaButton.setOnAction((event)
                -> {
            zvyrazni((Button) event.getSource());
            jakaVez = 5;
        });

        zustatekLabel = new Label();
        zustatekLabel.setMinSize(100, 40);
        zustatekLabel.setLayoutX(xLayout);
        zustatekLabel.setLayoutY(50);
        zustatekLabel.setStyle("-fx-font-weight: bold");
        zustatekLabel.setText(String.valueOf(penize) + "$");

        zivotyLabel = new Label();
        zivotyLabel.setMinSize(100, 40);
        zivotyLabel.setLayoutX(xLayout + zustatekLabel.getWidth());
        zivotyLabel.setLayoutY(10);
        zivotyLabel.setStyle("-fx-font-weight: bold");
        zivotyLabel.setText(String.valueOf(zbyvajiciZivoty) + " HP");

        infoVezLabel = new Label();
        infoVezLabel.setMinSize(100, 40);
        infoVezLabel.setLayoutX(xLayout);
        infoVezLabel.setLayoutY(30);
        infoVezLabel.setStyle("-fx-font-weight: bold");

        casLabel = new Label();
        casLabel.setMinSize(100, 40);
        casLabel.setLayoutX(xLayout);
        casLabel.setLayoutY(450);
        casLabel.setStyle("-fx-font-weight: bold");
        casLabel.setText("Další vlna: ");

        sekundyLabel = new Label();
        sekundyLabel.setMinSize(100, 40);
        sekundyLabel.setLayoutX(xLayout + 60);
        sekundyLabel.setLayoutY(450);
        sekundyLabel.setStyle("-fx-font-weight: bold");

        volbaDosah = new CheckBox("Dosah");
        volbaDosah.setLayoutX(30);
        volbaDosah.setLayoutX(xLayout);
        volbaDosah.setStyle("-fx-font-weight: bold");
        volbaDosah.setSelected(dosahPovolen);
        volbaDosah.setOnMouseClicked((event)
                -> {
            if (volbaDosah.isSelected()) {
                dosahPovolen = true;
            } else {
                volbaDosah.selectedProperty().set(false);
                dosahPovolen = false;
                list.remove(radius);
            }

        });
        seznamButtonuVezi.add(kulometButton);
        seznamButtonuVezi.add(jednohlavnovaButton);
        seznamButtonuVezi.add(raketometButton);
        seznamButtonuVezi.add(laserButton);
        seznamButtonuVezi.add(harpunaButton);
        list.addAll(restartButton, pauzaButton, zvukButton, volbaDosah, infoVezLabel, zustatekLabel, zivotyLabel, casLabel, sekundyLabel, kulometButton, jednohlavnovaButton, raketometButton, laserButton, harpunaButton);
        kulometButton.setTooltip(new Tooltip("Kulometné hnízdo " + KulometVez.vratCenu() + "$"));
        jednohlavnovaButton.setTooltip(new Tooltip("Dělo " + Jednohlavnova.vratCenu() + "$"));
        raketometButton.setTooltip(new Tooltip("Raketomet " + RaketometVez.vratCenu() + "$"));
        laserButton.setTooltip(new Tooltip("Plazma gun " + LaserVez.vratCenu() + "$"));
        harpunaButton.setTooltip(new Tooltip("Harpuna " + HarpunaVez.vratCenu() + "$"));
        seznamButtonuVezi.forEach((t) -> {
            t.setOnMouseEntered((event) -> {
                infoVezLabel.setText(t.getTooltip().getText());
                if (list.contains(radius)) {
                    list.remove(radius);
                }
            });
        });

    }

    static public void zvyrazni(Button button) {
        seznamButtonuVezi.forEach((t) -> {
            if (!t.equals(button)) {
                t.setStyle("-fx-border-color:transparent;");
            } else if (t.equals(button)) {
                t.setStyle("-fx-border-color:black;");
            }
        });
    }

    static void presunButtonu(int x) {
        kulometButton.setLayoutX(x);
        jednohlavnovaButton.setLayoutX(x);
        raketometButton.setLayoutX(x);
        laserButton.setLayoutX(x);
        harpunaButton.setLayoutX(x);
        zustatekLabel.setLayoutX(x);
        zivotyLabel.setLayoutX(x);
        casLabel.setLayoutX(x);
        sekundyLabel.setLayoutX(x + 60);
        infoVezLabel.setLayoutX(x);
        volbaDosah.setLayoutX(x);
        pauzaButton.setLayoutX(x);
        restartButton.setLayoutX(x + 50);
        zvukButton.setLayoutX(x + 100);
    }

    int volba = 0;

    static public void zmenaVelikosti() {
        for (int i = 0; i < pole.length; i++) {
            for (int j = 0; j < pole[0].length; j++) {
                kopiePole.get(kopiePole.indexOf(pole[i][j])).setWidth((Hra.vratSirku() * POMER_POZADI_SIRKA) * POMER_CTVERCU);
                kopiePole.get(kopiePole.indexOf(pole[i][j])).setHeight((Hra.vratVysku() * POMER_POZADI_VYSKA) * POMER_CTVERCU);
                kopiePole.get(kopiePole.indexOf(pole[i][j])).setX(((Hra.vratSirku() * POMER_POZADI_SIRKA) * POMER_CTVERCU) * (j));
                kopiePole.get(kopiePole.indexOf(pole[i][j])).setY(((Hra.vratVysku() * POMER_POZADI_VYSKA) * POMER_CTVERCU) * (i));
                pole[i][j].setWidth((Hra.vratSirku() * POMER_POZADI_SIRKA) * POMER_CTVERCU);
                pole[i][j].setHeight((Hra.vratVysku() * POMER_POZADI_VYSKA) * POMER_CTVERCU);
                cesty[i][j].setFitWidth((Hra.vratSirku() * POMER_POZADI_SIRKA) * POMER_CTVERCU);
                cesty[i][j].setFitHeight((Hra.vratVysku() * POMER_POZADI_VYSKA) * POMER_CTVERCU);
                pole[i][j].setX(((Hra.vratSirku() * POMER_POZADI_SIRKA) * POMER_CTVERCU) * (j));
                pole[i][j].setY(((Hra.vratVysku() * POMER_POZADI_VYSKA) * POMER_CTVERCU) * (i));
                cesty[i][j].setX(pole[i][j].getX());
                cesty[i][j].setY(pole[i][j].getY());
            }
        }
        pozadi.setFitWidth(pole[29][49].getX() + pole[29][49].getWidth());
        pozadi.setFitHeight(pole[29][49].getY() + pole[29][49].getHeight());
        Vez.vratSeznam().forEach((t) -> {
            t.presun(kopiePole.get(kopiePole.indexOf(poziceVezi.get(Vez.vratSeznam().indexOf(t)))).getX(), kopiePole.get(kopiePole.indexOf(poziceVezi.get(Vez.vratSeznam().indexOf(t)))).getY());
        });

        presunButtonu((int) (Hra.vratSirku() - 175));
        posunBody();
    }

    static private void posunBody() {
        seznamBodObjkt.forEach((t) -> {
            t.presun(pole[t.vratX()][t.vratY()].getX(), pole[t.vratX()][t.vratY()].getY());
        });
    }

    static private void cestaDolu(int odkudY, int kamY, int odkudX) {
        for (int y = odkudY; y <= kamY; y++) {
            Bod(odkudX, y);
        }
    }

    static private void cestaVpravo(int odkudX, int kamX, int odkudY) {
        for (int x = odkudX; x <= kamX; x++) {
            Bod(x, odkudY);
        }
    }

    static private void cestaVlevo(int odkudX, int kamX, int odkudY) {
        for (int x = odkudX; x >= kamX; x--) {
            Bod(x, odkudY);
        }
    }

    static private void cestaNahoru(int odkudY, int kamY, int odkudX) {
        for (int y = odkudY; y >= kamY; y--) {
            Bod(odkudX, y);
        }
    }

    static private void Bod(int X, int Y) {
        Bod bod = new Bod(pole, list);
        bod.Spawn(Y, X);
        if (posledniX > 0) {
            for (int i = posledniX; i <= X; i++) {
                Kolem(i, Y);
            }
            for (int i = X; i <= posledniX; i++) {
                Kolem(i, Y);
            }
        }
        if (posledniY > 0) {
            for (int i = posledniY; i <= Y; i++) {
                Kolem(X, i);
            }
            for (int i = Y; i <= posledniY; i++) {
                Kolem(X, i);
            }
        }
        posledniX = X;
        posledniY = Y;
        seznamBodObjkt.add(bod);

    }

    static private void cestaDolu2(int odkudY, int kamY, int odkudX) {
        for (int y = odkudY; y <= kamY; y++) {
            udelejBod(odkudX, y);
        }
    }

    static private void cestaVpravo2(int odkudX, int kamX, int odkudY) {
        for (int x = odkudX; x <= kamX; x++) {
            udelejBod(x, odkudY);
        }
    }

    static private void cestaVlevo2(int odkudX, int kamX, int odkudY) {
        for (int x = odkudX; x >= kamX; x--) {
            udelejBod(x, odkudY);
        }
    }

    static private void cestaNahoru2(int odkudY, int kamY, int odkudX) {
        for (int y = odkudY; y >= kamY; y--) {
            udelejBod(odkudX, y);
        }
    }

    static private void udelejBod(int X, int Y) {
        Bod bod = new Bod(pole, list);
        bod.Spawn11(Y, X);
        if (posledniX2 > 0) {
            for (int i = posledniX2; i <= X; i++) {
                Kolem(i, Y);
            }
            for (int i = X; i <= posledniX2; i++) {
                Kolem(i, Y);
            }
        }
        if (posledniY2 > 0) {
            for (int i = posledniY2; i <= Y; i++) {
                Kolem(X, i);
            }
            for (int i = Y; i <= posledniY2; i++) {
                Kolem(X, i);
            }
        }
        posledniX2 = X;
        posledniY2 = Y;
        seznamBodObjkt.add(bod);
    }

    static private void Kolem(int X, int Y) {
        cesty[Y][X - 1].setStyle("-fx-image: url('file:soubory/textury/hlina.png')");
        cesty[Y][X].setStyle("-fx-image: url('file:soubory/textury/hlina.png')");
        cesty[Y][X + 1].setStyle("-fx-image: url('file:soubory/textury/hlina.png')");
        cesty[Y - 1][X - 1].setStyle("-fx-image: url('file:soubory/textury/hlina.png')");
        cesty[Y - 1][X].setStyle("-fx-image: url('file:soubory/textury/hlina.png')");
        cesty[Y + 1][X - 1].setStyle("-fx-image: url('file:soubory/textury/hlina.png')");
        cesty[Y + 1][X].setStyle("-fx-image: url('file:soubory/textury/hlina.png')");
        cesty[Y + 1][X + 1].setStyle("-fx-image: url('file:soubory/textury/hlina.png')");
        cesty[Y - 1][X + 1].setStyle("-fx-image: url('file:soubory/textury/hlina.png')");
        zakazano.add(pole[Y][X - 1]);
        zakazano.add(pole[Y][X - 1]);
        zakazano.add(pole[Y][X - 1]);
        zakazano.add(pole[Y - 1][X - 1]);
        zakazano.add(pole[Y - 1][X + 1]);
        zakazano.add(pole[Y - 1][X]);
        zakazano.add(pole[Y + 1][X - 1]);
        zakazano.add(pole[Y + 1][X + 1]);
        zakazano.add(pole[Y + 1][X]);
        list.forEach((t) -> {
            if (t instanceof ImageView && ((ImageView) t).getStyle().equals("-fx-image: url('file:soubory/textury/hlina.png')")) {
                listCesty.add((ImageView) t);
            }
        });
    }

    static public void prictiPenize(double kolik) {
        penize += kolik;
        zustatekLabel.setText(String.valueOf(penize) + "$");
    }

    static public void ocekavejVyhru() {
        vyherni = new Timeline();
        KeyFrame keyframe = new KeyFrame(Duration.seconds(1), (event) -> {
            if (Nepritel.vratListBrouku().isEmpty() && !prohral && zbyvajiciZivoty > 0) {
                System.out.println("Vyhrál jsi");
                FXMLDocumentController.showVyhraButtony(true);
                Vez.zastavVeze(true);
                vyherni.stop();
            }
        });
        vyherni.getKeyFrames().addAll(keyframe);
        vyherni.setCycleCount(Timeline.INDEFINITE);
        vyherni.play();
    }

    static public void odectiZivoty(int kolik) {
        zbyvajiciZivoty -= kolik;
        zivotyLabel.setText(String.valueOf(zbyvajiciZivoty) + " HP");
        if (zbyvajiciZivoty <= 0) {
            System.out.println("prohrál jsi");
            FXMLDocumentController.showProhraButtony(true);
            Vez.zastavVeze(true);
            Nepritel.zastavBroukyAVlnu(true);
            prohral = true;
        }
    }

    static public boolean zaplat(int kolik) {
        if (penize - kolik >= 0) {
            prictiPenize(-kolik);
            return true;
        } else {
            return false;
        }
    }

    static public void nastavPenize(int kolik) {
        penize = kolik;
        zustatekLabel.setText(String.valueOf(penize) + "$");
        posledniPenize = penize;
    }

    static public void nastavList(ObservableList zadanyList) {
        list = zadanyList;
    }

    static public void nastavCas(String kolik) {
        sekundyLabel.setText(kolik);
    }

    static public int vratMapu() {
        return kolikataNactena + 1;
    }

    static public int vratPocetCest() {
        return pocetCest;
    }

    static public double vratSirkuMapy() {
        return pozadi.getFitWidth();
    }

    static public double vratVyskuMapy() {
        return pozadi.getFitHeight();
    }

    static public ArrayList<Rectangle> vratObsazene() {
        return zabrano;
    }

    static public Rectangle[][] vratPole() {
        return pole;
    }

    static public Rectangle vratZacatek() {
        return zacatek;
    }

    static public Rectangle vratVybranyObdelnik() {
        return vybranyObdelnik;
    }

}
