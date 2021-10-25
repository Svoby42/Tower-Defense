package javafxapplication1;

import javafxapplication1.veze.VezInterface;
import javafxapplication1.brouci.Nepritel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable {

    @FXML
    private AnchorPane kontejner;

    @FXML
    private Button newGameButton;

    @FXML
    private Button loadGameButton;

    @FXML
    private Button prvkyButton;

    @FXML
    private Button konecButton;

    @FXML
    private Button infoButton;

    @FXML
    private Button zpetButton;

    @FXML
    private TextArea infoText;

    ArrayList<Button> listButtonu = new ArrayList<>();

    @FXML
    private BorderPane lepsikontejner;

    static ObservableList list;
    static Button uloz;
    static Button restart;
    static Button konec;
    static Button dalsiLevel;
    static ListView<String> seznam;
    String harpunaText = "harpuna, pomalé nabíjení, nejsilnější střely, upřednostňuje nejsilnější jednotky" + "\n" + "vylepšení: rychlejší přebíjení";
    String raketometText = "raketomet, výbuch zasáhne všechny brouky v dosahu" + "\n" + "vylepšení: zmražení (dočasně zmrazí zasažené, vhodné proti skupinám)";
    String deloText = "bezzákluzová puška, nic od ní nečekejte, upřednostňuje nejslabší jednotky";
    String kulometText = ".50, střílí v dávkách" + "\n" + "vylepšení: dvojnásobné poškození, dvojnásobné dávky";
    String plazmaText = "plazmové dělo, trochu slabší než harpuna, rychlejší než .50, zbraň budoucnosti" + "\n" + "vylepšení: nepotřebuje";
    String broukText = "pěšák, nic neobvyklého, průměrná jednotka";
    String chrestovnikText = "sprinter, zpravidla ve skupinkách, hodně najednou jich může proklouznout pomalou obranou";
    String rohacText = "slabý, ale rychlý, umírá na 1 zásah ze všeho";
    String drepcikText = "rychlejší obrněnec, doporučuje se zmražení v kombinaci s harpunou";
    String vejirnikText = "tank, nejsilnější jednotka ve hře, nic jiného než několik harpun vás nezachrání";
    int pocet = 0;
    Stage scena;
    int otevreneOkno = 0;
    int volba;
    int sirka;
    int vyska;
    static transient VezInterface vez;
    int probehlo = 0;

    @FXML
    void nactiInfo(ActionEvent event) {
        schovejButtony();
        infoText.setVisible(true);
        zpetButton.setVisible(true);
        otevreneOkno = 1;
    }

    @FXML
    void nactiPrvky(ActionEvent event) {
        schovejButtony();
        zpetButton.setVisible(true);
        seznam = new ListView();
        seznam.setMinSize(((Stage) kontejner.getScene().getWindow()).getWidth() * 0.8, ((Stage) kontejner.getScene().getWindow()).getHeight());
        ObservableList<String> popisky = FXCollections.observableArrayList(raketometText, harpunaText, deloText, kulometText, plazmaText, broukText, chrestovnikText, rohacText, drepcikText, vejirnikText);
        seznam.setItems(popisky);
        seznam.setCellFactory(param -> new ListCell<String>() {
            private ImageView obrazek = new ImageView();

            @Override
            public void updateItem(String jmeno, boolean prazdny) {
                super.updateItem(jmeno, prazdny);
                if (!prazdny) {
                    if (jmeno.equals(raketometText)) {
                        obrazek.setStyle("-fx-image: url('file:soubory/buttony/raketomet_ikona.png')");
                    } else if (jmeno.equals(deloText)) {
                        obrazek.setStyle("-fx-image: url('file:soubory/buttony/jednohlavnova_ikona.png')");
                    } else if (jmeno.equals(kulometText)) {
                        obrazek.setStyle("-fx-image: url('file:soubory/buttony/kulomet_ikona.png')");
                    } else if (jmeno.equals(plazmaText)) {
                        obrazek.setStyle("-fx-image: url('file:soubory/buttony/laser_ikona.png')");
                    } else if (jmeno.equals(harpunaText)) {
                        obrazek.setStyle("-fx-image: url('file:soubory/buttony/harpuna_ikona.png')");
                    } else if (jmeno.equals(broukText)) {
                        obrazek.setStyle("-fx-image: url('file:soubory/brouci/brouk.png')");
                    } else if (jmeno.equals(chrestovnikText)) {
                        obrazek.setStyle("-fx-image: url('file:soubory/brouci/chrestovnik.png')");
                    } else if (jmeno.equals(rohacText)) {
                        obrazek.setStyle("-fx-image: url('file:soubory/brouci/rohac.png')");
                    } else if (jmeno.equals(drepcikText)) {
                        obrazek.setStyle("-fx-image: url('file:soubory/brouci/drepcik.png')");
                    } else if (jmeno.equals(vejirnikText)) {
                        obrazek.setStyle("-fx-image: url('file:soubory/brouci/vejirnik.png')");
                    }
                }
                obrazek.setFitHeight(50);
                obrazek.setFitWidth(70);
                setText(jmeno);
                setGraphic(obrazek);
            }
        });
        list.add(seznam);
        otevreneOkno = 2;
    }

    @FXML
    void nactiPozici(ActionEvent event) throws FileNotFoundException, IOException, ClassNotFoundException {
        Rectangle2D obraz = Screen.getPrimary().getBounds();
        int cislo;
        int level;
        try (FileReader vypis = new FileReader("uloz.txt")) {
            cislo = vypis.read();
            level = vypis.read();
            System.out.println(cislo);
            System.out.println(level);
            vypis.close();
        }
        probehlo = 3;
        scena = (Stage) kontejner.getScene().getWindow();
        scena.setWidth(obraz.getMaxX());
        scena.setHeight(obraz.getMaxY());
        scena = (Stage) kontejner.getScene().getWindow();
        scena.widthProperty().addListener((observable) -> {
            Hra.nastavSirku(scena.getWidth());
            Hra.nastavVysku(scena.getHeight());
            Hra.zmenaVelikosti();
        });
        scena.heightProperty().addListener(((observable) -> {
            Hra.nastavSirku(scena.getWidth());
            Hra.nastavVysku(scena.getHeight());
            Hra.zmenaVelikosti();
        }));
        scena.setX(0);
        scena.setY(0);
        Hra.nastavZaklad();
        Mapa.vykreslitMapu(cislo);
        Nepritel.spustLevel(level);
        Nepritel.spustBrouky();
        scena.setMinHeight(700);
        scena.setMinWidth(1070);
        schovejButtony();
        Hra.nastavSirku(scena.getWidth());
        Hra.nastavVysku(scena.getHeight());
        Hra.zmenaVelikosti();
    }

    @FXML
    void newGame(ActionEvent event) {
        Rectangle2D obraz = Screen.getPrimary().getBounds();
        probehlo = 3;
        scena = (Stage) kontejner.getScene().getWindow();
        scena.setX(0);
        scena.setY(0);
        scena.setMinHeight(700);
        scena.setMinWidth(1070);
        scena.setWidth(obraz.getMaxX());
        scena.setHeight(obraz.getMaxY());
        scena = (Stage) kontejner.getScene().getWindow();
        scena.widthProperty().addListener((observable) -> {
            Hra.nastavSirku(scena.getWidth());
            Hra.nastavVysku(scena.getHeight());
            Hra.zmenaVelikosti();
        });
        scena.heightProperty().addListener(((observable) -> {
            Hra.nastavSirku(scena.getWidth());
            Hra.nastavVysku(scena.getHeight());
            Hra.zmenaVelikosti();
        }));
        Hra.nastavZaklad();
        Hra.spustPrvniLevel();
        schovejButtony();
        Hra.nastavSirku(scena.getWidth());
        Hra.nastavVysku(scena.getHeight());
        Hra.zmenaVelikosti();
    }

    @FXML
    void zpetButtonAkce(ActionEvent event) {
        switch (otevreneOkno) {
            case 1:
                odhalButtony();
                infoText.setVisible(false);
                zpetButton.setVisible(false);
                break;
            case 2:
                odhalButtony();
                seznam.setVisible(false);
                zpetButton.setVisible(false);
        }
    }

    @FXML
    void ukoncit(ActionEvent event) {
        Platform.runLater(() -> {
            Platform.exit();
            System.exit(0);
        });
    }

    @FXML
    void pauzaHry(KeyEvent event) {
//        if (event.getCode().toString().equals("ESCAPE")) {
//            System.out.println(event.getCode());
//        }
    }

    static int posledniVolba;

    static int vratPosledniVolbu() {
        if (Hra.vratVelikost() == 720) {
            posledniVolba = 1;
        } else if (Hra.vratVelikost() == 600) {
            posledniVolba = 3;
        }
        return posledniVolba;
    }

    void schovejButtony() {
        listButtonu.forEach((t) -> {
            t.setVisible(false);
        });
    }

    void odhalButtony() {
        listButtonu.forEach((t) -> {
            t.setVisible(true);
        });
    }

    /**
     * v případě výhry jsou viditelná všechna tlačítka
     *
     * @param volba
     */
    static public void showVyhraButtony(boolean volba) {
        if (volba) {
            restart.toFront();
            konec.toFront();
            dalsiLevel.toFront();
            uloz.toFront();
        } else {
            restart.toBack();
            konec.toBack();
            dalsiLevel.toBack();
            uloz.toBack();
        }
        restart.setVisible(volba);
        konec.setVisible(volba);
        dalsiLevel.setVisible(volba);
        uloz.setVisible(volba);
    }

    /**
     * v připadě prohry jsou viditelná tlačítka pro konec a restart
     *
     * @param volba
     */
    static public void showProhraButtony(boolean volba) {
        if (volba) {
            restart.toFront();
            konec.toFront();
        } else {
            restart.toBack();
            konec.toBack();
        }
        restart.toFront();
        konec.toFront();
        restart.setVisible(volba);
        konec.setVisible(volba);
    }

    /**
     * nastaví velikosti a funkce tlačítek po výhře/prohře
     */
    static public void nastavButtony() {
        uloz = new Button("Ulozit");
        restart = new Button("Restart");
        konec = new Button("Konec");
        dalsiLevel = new Button("Dalsi Level");
        uloz.setLayoutX(300);
        uloz.setLayoutY(300);
        uloz.setMinSize(100, 30);
        uloz.setMaxSize(100, 30);
        restart.setLayoutX(400);
        restart.setLayoutY(300);
        restart.setMinSize(100, 30);
        restart.setMaxSize(100, 30);
        konec.setLayoutX(500);
        konec.setLayoutY(300);
        konec.setMinSize(100, 30);
        konec.setMaxSize(100, 30);
        dalsiLevel.setLayoutX(600);
        dalsiLevel.setLayoutY(300);
        dalsiLevel.setMinSize(100, 30);
        dalsiLevel.setMaxSize(100, 30);

        restart.setOnAction((event) -> {
            Mapa.restartujUroven();
            showVyhraButtony(false);
        });
        konec.setOnAction((event) -> {
            System.exit(0);
        });
        dalsiLevel.setOnAction((event) -> {
            Mapa.dalsiUroven();
            showVyhraButtony(false);
        });
        uloz.setOnAction((ActionEvent event) -> {
            File file = new File("uloz.txt");
            try {
                try (FileWriter zapis = new FileWriter("uloz.txt")) {
                    zapis.write(Mapa.vratMapu());
                    zapis.write(Nepritel.vratDokoncenyLevel());
                }
            } catch (IOException e) {
            }
        });
        list.addAll(restart, konec, dalsiLevel, uloz);
        showVyhraButtony(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        list = kontejner.getChildren();
        list.forEach((t) -> {
            if (t instanceof Button) {
                listButtonu.add((Button) t);
            }
        });
        nastavButtony();
        Hra.nastavList(list);
    }
}
