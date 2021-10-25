package javafxapplication1.brouci;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.*;
import javafx.util.Duration;
import javafxapplication1.Hra;
import javafxapplication1.Mapa;
import javafxapplication1.veze.RaketometVez;
import javafxapplication1.veze.Vez;

public class Nepritel implements Runnable {

    ObservableList list;
    private int HP;
    public ImageView broukImage;
    static public Rectangle zacatek;
    int cislo = 0;
    static public int velikost;
    static int sekundy;
    int pocet = 1;
    static Brouk brouk;
    static Chrestovnik chrestovnik;
    static Drepcik drepcik;
    static Rohac rohac;
    static Vejirnik vejirnik;
    ArrayList<Rectangle> Body;
    static ArrayList<NepritelInterface> seznamZivych = new ArrayList<>();
    static Rectangle[][] pole;
    static ArrayList<NepritelInterface> listBrouku = new ArrayList<>();
    File file = new File("soubory/zvuky/brouk_hit.mp3");
    private final AudioClip klip;
    static boolean dospawnovano;
    static Timeline timelinePohybAll = new Timeline();
    static Iterator<NepritelInterface> it;
    static ArrayList<Timeline> seznamTimeline = new ArrayList<>();
    static Timeline timeline;
    static Timeline aktualniVlna;
    static Timeline pauzaLine;
    static Timeline odpocitavani;
    static KeyFrame pauzaFrame;
    static int level = 1;
    static KeyFrame jdiKeyframe = new KeyFrame(Duration.millis(17), (event) -> {
        pochodFunkce();
    });
    static boolean jePauza = false;
    static double hlasitost = 0.1;
    static boolean ztlumeno = false;

    public Nepritel(ObservableList list) {
        this.klip = new AudioClip(file.toURI().toString());
        this.list = list;
    }

    static public void udelejChrestovnika() {
        chrestovnik = new Chrestovnik(Hra.vratList());
        chrestovnik.objevSe();
        seznamZivych.add(chrestovnik);
    }

    static public void udelejBrouka() {
        brouk = new Brouk(Hra.vratList());
        brouk.objevSe();
        seznamZivych.add(brouk);

    }

    static public void udelejDrepcika() {
        drepcik = new Drepcik(Hra.vratList());
        drepcik.objevSe();
        seznamZivych.add(drepcik);
    }

    static public void udelejVejirnika() {
        vejirnik = new Vejirnik(Hra.vratList());
        vejirnik.objevSe();
        seznamZivych.add(vejirnik);
    }

    static public void udelejRohace() {
        rohac = new Rohac(Hra.vratList());
        rohac.objevSe();
        seznamZivych.add(rohac);
    }

    public void nastavZacatek(Rectangle zacatek) {
        broukImage.setX(zacatek.getX());
        broukImage.setY(zacatek.getY());
    }

    public void prehrajZvukHit() {
        klip.play(hlasitost);
    }

    static KeyFrame rohacFrame = new KeyFrame(Duration.millis(100), e -> udelejRohace());
    static KeyFrame drepcikFrame = new KeyFrame(Duration.millis(400), e -> udelejDrepcika());
    static KeyFrame broukFrame = new KeyFrame(Duration.millis(200), e -> udelejBrouka());
    static KeyFrame vejirnikFrame = new KeyFrame(Duration.millis(200), e -> udelejVejirnika());
    static KeyFrame chrestovnikFrame = new KeyFrame(Duration.millis(100), e -> udelejChrestovnika());
    static Runnable aktualniLevel;
    static Runnable dalsiLevel;

    static private void odpocet(int cykly) {
        sekundy = cykly;
        odpocitavani = new Timeline(new KeyFrame(Duration.seconds(1), (event)
                -> {
            if (sekundy == 1) {
                Mapa.nastavCas("Probíhá");
            } else {
                Mapa.nastavCas(String.valueOf(sekundy));
                sekundy--;
            }
        }));
        odpocitavani.setCycleCount(cykly);
        odpocitavani.play();
        seznamTimeline.add(odpocitavani);
    }

    static private void resetujOdpocet() {
        Mapa.nastavCas("");
        odpocitavani.stop();
    }

    static private void nBrouku(int n, int pauza, KeyFrame jakych, Runnable dalsiAkce) {
        timeline = new Timeline();
        timeline.getKeyFrames().add(jakych);
        timeline.setCycleCount(n);
        timeline.setOnFinished((event) -> {
            pauzaLine = new Timeline();
            pauzaFrame = new KeyFrame(Duration.seconds(pauza), (event2) -> {
                dalsiAkce.run();
            });
            pauzaLine.getKeyFrames().add(pauzaFrame);
            seznamTimeline.add(pauzaLine);
            pauzaLine.play();
            odpocet(pauza);
        });
        seznamTimeline.add(timeline);
        timeline.play();
    }

    static private void nBroukuVic(int n, int pauza, Runnable dalsiAkce, KeyFrame... jakych) {
        timeline = new Timeline();
        timeline.getKeyFrames().addAll(Arrays.asList(jakych));
        timeline.setCycleCount(n);
        timeline.setOnFinished((event) -> {
            pauzaLine = new Timeline();
            pauzaFrame = new KeyFrame(Duration.seconds(pauza), (event2) -> {
                dalsiAkce.run();
            });
            pauzaLine.getKeyFrames().add(pauzaFrame);
            seznamTimeline.add(pauzaLine);
            pauzaLine.play();
            odpocet(pauza);
        });
        seznamTimeline.add(timeline);
        timeline.play();
    }

    static private void ecoRound(Runnable dalsiAkce) {
        nBroukuVic(30, 10, ()
                -> {
            dalsiAkce.run();
        }, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame);
    }

    static private void ecoRoundMax(Runnable dalsiAkce) {
        nBroukuVic(300, 40, () -> {
            dalsiAkce.run();
        }, rohacFrame, rohacFrame, rohacFrame, rohacFrame, rohacFrame, rohacFrame, rohacFrame, rohacFrame, rohacFrame, rohacFrame, rohacFrame, rohacFrame);
    }

    static private void hodneTezkaHodneDlouhaVlna(Runnable dalsiAkce) {
        Rohac.nastavRychlost(2);
        Drepcik.nastavRychlost(3);
        Chrestovnik.nastavRychlost(4);
        Vejirnik.nastavRychlost(5);
        Brouk.nastavRychlost(6);
        ecoRoundMax(() -> {
            nBroukuVic(30, 20, () -> {
                nBroukuVic(30, 30, () -> {
                    nBroukuVic(30, 50, () -> {
                        nBroukuVic(30, 60, () -> {
                            nBroukuVic(30, 60, () -> {
                                dalsiAkce.run();
                            }, broukFrame, broukFrame, broukFrame, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame, vejirnikFrame, vejirnikFrame, vejirnikFrame, drepcikFrame, drepcikFrame, drepcikFrame, rohacFrame, rohacFrame, rohacFrame);
                        }, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame, vejirnikFrame, vejirnikFrame, vejirnikFrame, drepcikFrame, drepcikFrame, drepcikFrame, rohacFrame, rohacFrame, rohacFrame);
                    }, vejirnikFrame, vejirnikFrame, vejirnikFrame, drepcikFrame, drepcikFrame, drepcikFrame, rohacFrame, rohacFrame, rohacFrame);
                }, drepcikFrame, drepcikFrame, drepcikFrame, rohacFrame, rohacFrame, rohacFrame);
            }, rohacFrame, rohacFrame, rohacFrame);
        });
    }

    static private void sprinterRound(Runnable dalsiAkce) {
        Chrestovnik.nastavRychlost(5);
        nBroukuVic(100, 20, () -> {
            nBroukuVic(100, 20, () -> {
                nBroukuVic(100, 20, () -> {
                    dalsiAkce.run();
                }, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame);
            }, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame);
        }, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame);
    }

    static private void tankRound(Runnable dalsiAkce) {
        Drepcik.nastavRychlost(2);
        Vejirnik.nastavRychlost(1);
        nBroukuVic(100, 30, () -> {
            nBroukuVic(50, 40, () -> {
                nBroukuVic(25, 50, () -> {
                    dalsiAkce.run();
                }, drepcikFrame, drepcikFrame, vejirnikFrame, drepcikFrame, drepcikFrame, vejirnikFrame);
            }, drepcikFrame, drepcikFrame, vejirnikFrame);
        }, drepcikFrame, drepcikFrame);
    }

    static private void prvniVlna1(Runnable dalsiAkce) {
        Rohac.nastavRychlost(3);
        Drepcik.nastavRychlost(3);
        nBrouku(90, 20, rohacFrame, () -> {
            nBroukuVic(90, 20, () -> {
                nBroukuVic(180, 20, () -> {
                    nBrouku(5, 30, vejirnikFrame, () -> {
                        nBrouku(40, 20, drepcikFrame, () -> {
                            dalsiAkce.run();
                        });
                    });
                }, rohacFrame, rohacFrame);
            }, rohacFrame, broukFrame);
        });
    }

    static private void druhaVlna1(Runnable dalsiAkce) {
        Drepcik.nastavRychlost(3);
        Chrestovnik.nastavRychlost(5);
        Rohac.nastavRychlost(4);
        Vejirnik.nastavRychlost(4);
        nBroukuVic(130, 20, () -> {
            nBroukuVic(90, 30, () -> {
                nBroukuVic(180, 40, () -> {
                    nBrouku(70, 10, rohacFrame, () -> {
                        nBrouku(40, 30, vejirnikFrame, () -> {
                            dalsiAkce.run();
                        });
                    });
                }, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame);
            }, drepcikFrame, drepcikFrame);
        }, rohacFrame, rohacFrame, rohacFrame);
    }

    static private void tretiVlna1(Runnable dalsiAkce) {
        Vejirnik.nastavRychlost(5);
        nBrouku(10, 30, vejirnikFrame, () -> {
            nBrouku(30, 40, vejirnikFrame, () -> {
                dalsiAkce.run();
            });
        });
    }

    static private void ctvrtaVlna1(Runnable dalsiAkce) {
        Drepcik.nastavRychlost(4);
        Chrestovnik.nastavRychlost(5);
        Rohac.nastavRychlost(4);
        Vejirnik.nastavRychlost(3);
        nBroukuVic(70, 40, () -> {
            dalsiAkce.run();
        }, drepcikFrame, drepcikFrame, rohacFrame, rohacFrame, chrestovnikFrame, vejirnikFrame, vejirnikFrame);
    }

    static private void prvniVlna2(Runnable dalsiAkce) {
        nBroukuVic(30, 10, () -> {
            nBrouku(100, 20, rohacFrame, () -> {
                Vejirnik.nastavRychlost(1);
                nBroukuVic(30, 20, () -> {
                    Rohac.nastavRychlost(5);
                    nBroukuVic(200, 20, () -> {
                        nBroukuVic(30, 20, () -> {
                            dalsiAkce.run();
                        }, drepcikFrame, drepcikFrame, drepcikFrame, drepcikFrame);
                    }, rohacFrame, rohacFrame, rohacFrame, rohacFrame);
                }, chrestovnikFrame, chrestovnikFrame, drepcikFrame, drepcikFrame);
            });
        }, rohacFrame, rohacFrame, rohacFrame, rohacFrame, rohacFrame, rohacFrame, rohacFrame, rohacFrame, rohacFrame);
    }

    static private void druhaVlna2(Runnable dalsiAkce) {
        Vejirnik.nastavRychlost(2);
        nBroukuVic(100, 40, () -> {
            Rohac.nastavRychlost(5);
            nBroukuVic(200, 5, () -> {
                nBroukuVic(50, 20, () -> {
                    dalsiAkce.run();
                }, drepcikFrame, drepcikFrame, drepcikFrame, drepcikFrame);
            }, rohacFrame, rohacFrame, rohacFrame, rohacFrame);
        }, chrestovnikFrame, chrestovnikFrame, drepcikFrame, drepcikFrame, drepcikFrame);
    }

    static private void tretiVlna2(Runnable dalsiAkce) {
        nBroukuVic(25, 30, () -> {
            nBroukuVic(20, 10, () -> {
                dalsiAkce.run();
            }, vejirnikFrame, vejirnikFrame);
        }, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame, rohacFrame, rohacFrame, rohacFrame);
    }

    static private void ctvrtaVlna2(Runnable dalsiAkce) {
        nBroukuVic(40, 40, () -> {
            nBroukuVic(30, 20, () -> {
                nBroukuVic(100, 60, () -> {
                    nBroukuVic(50, 10, () -> {
                        Mapa.ocekavejVyhru();
                    }, vejirnikFrame, vejirnikFrame);
                }, drepcikFrame, drepcikFrame);
            }, vejirnikFrame, vejirnikFrame);
        }, drepcikFrame, drepcikFrame, drepcikFrame);
    }

    static private void prvniVlna3(Runnable dalsiAkce) {
        Drepcik.nastavRychlost(5);
        Vejirnik.nastavRychlost(3);
        Rohac.nastavRychlost(8);
        nBroukuVic(30, 30, () -> {
            nBrouku(10, 30, vejirnikFrame, () -> {
                nBroukuVic(50, 15, () -> {
                    dalsiAkce.run();
                }, rohacFrame, rohacFrame);
            });
        }, drepcikFrame, drepcikFrame);
    }

    static private void druhaVlna3(Runnable dalsiAkce) {
        Chrestovnik.nastavRychlost(5);
        Rohac.nastavRychlost(6);
        Drepcik.nastavRychlost(2);
        nBroukuVic(30, 30, () -> {
            nBroukuVic(40, 10, () -> {
                nBroukuVic(10, 30, () -> {
                    nBroukuVic(30, 20, () -> {
                        dalsiAkce.run();
                    }, broukFrame, broukFrame);
                }, drepcikFrame, drepcikFrame, drepcikFrame, drepcikFrame);
            }, rohacFrame, rohacFrame);
        }, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame);
    }

    static private void tretiVlna3(Runnable dalsiAkce) {
        Drepcik.nastavRychlost(3);
        Chrestovnik.nastavRychlost(5);
        Rohac.nastavRychlost(4);
        Vejirnik.nastavRychlost(4);
        nBroukuVic(130, 20, () -> {
            nBroukuVic(90, 30, () -> {
                nBroukuVic(180, 40, () -> {
                    nBrouku(70, 10, rohacFrame, () -> {
                        nBrouku(40, 30, vejirnikFrame, () -> {
                            dalsiAkce.run();
                        });
                    });
                }, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame);
            }, drepcikFrame, drepcikFrame);
        }, drepcikFrame, drepcikFrame, drepcikFrame);
    }

    static private void ctvrtaVlna3(Runnable dalsiAkce) {
        Vejirnik.nastavRychlost(1);
        Drepcik.nastavRychlost(4);
        nBroukuVic(20, 30, () -> {
            nBroukuVic(100, 10, () -> {
                Rohac.nastavRychlost(6);
                nBroukuVic(100, 10, () -> {
                    ctvrtaVlna1(() -> {
                        prvniVlna3(() -> {
                            ecoRoundMax(() -> {
                                Vejirnik.nastavRychlost(1);
                                Drepcik.nastavRychlost(3);
                                nBroukuVic(100, 30, () -> {
                                    dalsiAkce.run();
                                }, vejirnikFrame, vejirnikFrame, vejirnikFrame, drepcikFrame, drepcikFrame, drepcikFrame);
                            });
                        });
                    });
                }, rohacFrame, rohacFrame, rohacFrame, rohacFrame, rohacFrame, rohacFrame, rohacFrame, rohacFrame);
            }, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame);
        }, vejirnikFrame, vejirnikFrame, drepcikFrame, drepcikFrame);
    }

    static private void prvniVlna4(Runnable dalsiAkce) {
        Rohac.nastavRychlost(1);
        nBroukuVic(100, 30, () -> {
            ecoRoundMax(() -> {
                nBroukuVic(30, 30, () -> {
                    nBroukuVic(10, 20, () -> {
                        nBroukuVic(40, 20, () -> {
                            Rohac.nastavRychlost(4);
                            Brouk.nastavRychlost(4);
                            Drepcik.nastavRychlost(2);
                            nBroukuVic(50, 20, () -> {
                                dalsiAkce.run();
                            }, drepcikFrame, drepcikFrame, rohacFrame, rohacFrame, broukFrame, broukFrame);
                        }, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame);
                    }, vejirnikFrame, vejirnikFrame);
                }, drepcikFrame, drepcikFrame, drepcikFrame, drepcikFrame);
            });
        }, rohacFrame, rohacFrame, rohacFrame, rohacFrame, rohacFrame, rohacFrame, rohacFrame, rohacFrame);
    }

    static private void druhaVlna4(Runnable dalsiAkce) {
        Drepcik.nastavRychlost(3);
        Chrestovnik.nastavRychlost(5);
        Rohac.nastavRychlost(4);
        Vejirnik.nastavRychlost(4);
        nBroukuVic(130, 20, () -> {
            nBroukuVic(90, 30, () -> {
                nBroukuVic(180, 40, () -> {
                    nBrouku(70, 10, rohacFrame, () -> {
                        nBrouku(40, 30, vejirnikFrame, () -> {
                            dalsiAkce.run();
                        });
                    });
                }, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame);
            }, drepcikFrame, drepcikFrame);
        }, drepcikFrame, drepcikFrame, drepcikFrame);
    }

    static private void prvniVlna5(Runnable dalsiAkce) {
        Vejirnik.nastavRychlost(2);
        Drepcik.nastavRychlost(3);
        Chrestovnik.nastavRychlost(6);
        ecoRoundMax(() -> {
            nBroukuVic(20, 30, () -> {
                nBroukuVic(90, 20, () -> {
                    nBroukuVic(90, 30, () -> {
                        nBroukuVic(180, 40, () -> {
                            nBrouku(70, 10, rohacFrame, () -> {
                                nBrouku(40, 30, vejirnikFrame, () -> {
                                    dalsiAkce.run();
                                });
                            });
                        }, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame, chrestovnikFrame);
                    }, drepcikFrame, drepcikFrame);
                }, drepcikFrame, drepcikFrame, drepcikFrame);
            }, vejirnikFrame, vejirnikFrame, vejirnikFrame, vejirnikFrame, vejirnikFrame);
        });
    }

    static private void druhaVlna5(Runnable dalsiAkce) {
        Rohac.nastavRychlost(5);
        Chrestovnik.nastavRychlost(5);
        nBroukuVic(100, 20, () -> {
            nBroukuVic(100, 20, () -> {
                nBroukuVic(100, 20, () -> {
                    nBroukuVic(100, 20, () -> {
                        dalsiAkce.run();
                    }, rohacFrame, chrestovnikFrame, rohacFrame, chrestovnikFrame, rohacFrame, chrestovnikFrame, rohacFrame, chrestovnikFrame, rohacFrame, chrestovnikFrame, rohacFrame, chrestovnikFrame);
                }, rohacFrame, chrestovnikFrame, rohacFrame, chrestovnikFrame, rohacFrame, chrestovnikFrame, rohacFrame, chrestovnikFrame, rohacFrame, chrestovnikFrame, rohacFrame, chrestovnikFrame);
            }, rohacFrame, chrestovnikFrame, rohacFrame, chrestovnikFrame, rohacFrame, chrestovnikFrame, rohacFrame, chrestovnikFrame, rohacFrame, chrestovnikFrame, rohacFrame, chrestovnikFrame);
        }, rohacFrame, chrestovnikFrame, rohacFrame, chrestovnikFrame, rohacFrame, chrestovnikFrame, rohacFrame, chrestovnikFrame, rohacFrame, chrestovnikFrame, rohacFrame, chrestovnikFrame);
    }

    static private void prvniVlna6(Runnable dalsiAkce) {
        Vejirnik.nastavRychlost(2);
        nBroukuVic(10, 30, () -> {

        }, vejirnikFrame, vejirnikFrame);
    }

    static private void prvniLevelVlny() {
        ecoRound(() -> {
            prvniVlna1(() -> {
                druhaVlna1(() -> {
                    tretiVlna1(() -> {
                        ctvrtaVlna1(() -> {
                            Mapa.ocekavejVyhru();
                        });
                    });
                });
            });
        });
    }

    static private void druhyLevelVlny() {
        ecoRound(() -> {
            prvniVlna2(() -> {
                druhaVlna2(() -> {
                    tretiVlna2(() -> {
                        ecoRoundMax(() -> {
                            ctvrtaVlna2(() -> {
                                Mapa.ocekavejVyhru();
                            });
                        });
                    });
                });
            });
        });
    }

    static private void tretiLevelVlny() {
        prvniVlna3(() -> {
            druhaVlna3(() -> {
                tretiVlna3(() -> {
                    ctvrtaVlna3(() -> {
                        Mapa.ocekavejVyhru();
                    });
                });
            });
        });
    }

    static private void ctvrtyLevelVlny() {
        prvniVlna4(() -> {
            druhaVlna4(() -> {
                ctvrtaVlna3(() -> {
                    Mapa.ocekavejVyhru();
                });
            });
        });
    }

    static private void patyLevelVlny() {
        prvniVlna5(() -> {
            ecoRoundMax(() -> {
                druhaVlna5(() -> {
                    ecoRoundMax(() -> {
                    });
                    ecoRoundMax(() -> {
                        Mapa.ocekavejVyhru();
                    });
                });
            });
        });
    }

    static private void sestyLevelVlny() {
        prvniVlna6(() -> {
            prvniVlna1(() -> {
                druhaVlna4(() -> {
                    tretiVlna3(() -> {
                        ctvrtaVlna3(() -> {
                            Mapa.ocekavejVyhru();
                        });
                    });
                });
            });
        });
    }

    static private void sedmyLevelVlny() {
        ecoRoundMax(() -> {
            hodneTezkaHodneDlouhaVlna(() -> {
                patyLevelVlny();
            });
        });

    }

    static private void osmyLevelVlny() {
        prvniVlna1(() -> {
            prvniVlna2(() -> {
                prvniVlna3(() -> {
                    prvniVlna4(() -> {
                        prvniVlna5(() -> {
                            prvniVlna6(() -> {
                                Mapa.ocekavejVyhru();
                            });
                        });
                    });
                });
            });
        });
    }

    static private void devatyLevelVlny() {
        druhaVlna1(() -> {
            druhaVlna2(() -> {
                druhaVlna3(() -> {
                    druhaVlna4(() -> {
                        druhaVlna5(() -> {
                            Mapa.ocekavejVyhru();
                        });
                    });
                });
            });
        });
    }

    static private void desatyLevelVlny() {
        tretiVlna1(() -> {
            tretiVlna2(() -> {
                tretiVlna3(() -> {
                    osmyLevelVlny();
                });
            });
        });
    }

    static private void jedenactyLevelVlny() {
        ecoRoundMax(() -> {
            ctvrtaVlna3(() -> {
                hodneTezkaHodneDlouhaVlna(() -> {
                    sestyLevelVlny();
                });
            });
        });
    }

    static private void dvanactyLevelVlny() {
        ecoRoundMax(() -> {
            sprinterRound(() -> {
                tankRound(() -> {
                    ctvrtaVlna3(() -> {
                        ctvrtaVlna1(() -> {
                            Mapa.ocekavejVyhru();
                        });
                    });
                });
            });
        });
    }

    static private void trinactyLevelVlny() {
        ecoRoundMax(() -> {
            desatyLevelVlny();
        });
    }

    static private void ctrnactyLevelVlny() {
        ecoRoundMax(() -> {
            sprinterRound(() -> {
                hodneTezkaHodneDlouhaVlna(() -> {
                    tankRound(() -> {
                        Mapa.ocekavejVyhru();
                    });
                });
            });
        });
    }

    static private void patnactyLevelVlny() {
        ecoRoundMax(() -> {
            ctvrtaVlna3(() -> {
                tankRound(() -> {
                    ecoRoundMax(() -> {
                        hodneTezkaHodneDlouhaVlna(() -> {
                            ctvrtyLevelVlny();
                        });
                    });
                });
            });
        });
    }

    static private void sestnactyLevelVlny() {
        tretiVlna3(() -> {
            prvniVlna6(() -> {
                prvniVlna5(() -> {
                    prvniVlna4(() -> {
                        druhaVlna5(() -> {
                            druhaVlna4(() -> {
                                druhaVlna3(() -> {
                                    Mapa.ocekavejVyhru();
                                });
                            });
                        });
                    });
                });
            });
        });
    }

    static private void sedmnactyLevelVlny() {
        Vejirnik.nastavRychlost(2);
        Drepcik.nastavRychlost(2);
        tretiVlna2(() -> {
            prvniVlna5(() -> {
                prvniVlna3(() -> {
                    prvniVlna2(() -> {
                        druhaVlna3(() -> {
                            druhaVlna2(() -> {
                                druhaVlna1(() -> {
                                    Mapa.ocekavejVyhru();
                                });
                            });
                        });
                    });
                });
            });
        });
    }

    static private void osmnactyLevelVlny() {
        Vejirnik.nastavRychlost(1);
        Drepcik.nastavRychlost(1);
        hodneTezkaHodneDlouhaVlna(() -> {
            sprinterRound(() -> {
                sprinterRound(() -> {
                    Vejirnik.nastavRychlost(1);
                    Drepcik.nastavRychlost(1);
                    tankRound(() -> {
                        tankRound(() -> {
                            Mapa.ocekavejVyhru();
                        });
                    });
                });
            });
        });
    }

    static private void prvniLevel() {
        puvodniRychlost();
        Vez.spustVeze();
        Mapa.nastavPenize(5000);
        prvniLevelVlny();
        aktualniLevel = () -> prvniLevel();
        dalsiLevel = () -> druhyLevel();
        level = 2;
    }

    static private void druhyLevel() {
        puvodniRychlost();
        Vez.spustVeze();
        Mapa.nastavPenize(8000);
        druhyLevelVlny();
        aktualniLevel = () -> druhyLevel();
        dalsiLevel = () -> tretiLevel();
        level = 3;
    }

    static private void tretiLevel() {
        puvodniRychlost();
        Vez.spustVeze();
        Mapa.nastavPenize(20000);
        tretiLevelVlny();
        aktualniLevel = () -> tretiLevel();
        dalsiLevel = () -> ctvrtyLevel();
        level = 4;
    }

    static private void ctvrtyLevel() {
        puvodniRychlost();
        Vez.spustVeze();
        Mapa.nastavPenize(20000);
        ctvrtyLevelVlny();
        aktualniLevel = () -> tretiLevel();
        dalsiLevel = () -> patyLevel();
        level = 5;
        System.out.println("ctvrty");
    }

    static private void patyLevel() {
        puvodniRychlost();
        Vez.spustVeze();
        Mapa.nastavPenize(20000);
        patyLevelVlny();
        aktualniLevel = () -> patyLevel();
        dalsiLevel = () -> sestyLevel();
        level = 6;
    }

    static private void sestyLevel() {
        puvodniRychlost();
        Vez.spustVeze();
        Mapa.nastavPenize(20000);
        sestyLevelVlny();
        aktualniLevel = () -> sestyLevel();
        dalsiLevel = () -> sedmyLevel();
        level = 7;
    }

    static private void sedmyLevel() {
        puvodniRychlost();
        Vez.spustVeze();
        Mapa.nastavPenize(20000);
        sedmyLevelVlny();
        aktualniLevel = () -> sedmyLevel();
        dalsiLevel = () -> osmyLevel();
        level = 8;
    }

    static private void osmyLevel() {
        puvodniRychlost();
        Vez.spustVeze();
        Mapa.nastavPenize(20000);
        osmyLevelVlny();
        aktualniLevel = () -> osmyLevel();
        dalsiLevel = () -> devatyLevel();
        level = 9;
    }

    static private void devatyLevel() {
        puvodniRychlost();
        Vez.spustVeze();
        Mapa.nastavPenize(20000);
        devatyLevelVlny();
        aktualniLevel = () -> devatyLevel();
        dalsiLevel = () -> desatyLevel();
        level = 10;
    }

    static private void desatyLevel() {
        puvodniRychlost();
        Vez.spustVeze();
        Mapa.nastavPenize(20000);
        desatyLevelVlny();
        aktualniLevel = () -> desatyLevel();
        dalsiLevel = () -> jedenactyLevel();
        level = 11;
    }

    static private void jedenactyLevel() {
        puvodniRychlost();
        Vez.spustVeze();
        Mapa.nastavPenize(20000);
        jedenactyLevelVlny();
        aktualniLevel = () -> jedenactyLevel();
        dalsiLevel = () -> dvanactyLevel();
        level = 12;
    }

    static private void dvanactyLevel() {
        puvodniRychlost();
        Vez.spustVeze();
        Mapa.nastavPenize(20000);
        dvanactyLevelVlny();
        aktualniLevel = () -> dvanactyLevel();
        dalsiLevel = () -> trinactyLevel();
        level = 13;
    }

    static private void trinactyLevel() {
        puvodniRychlost();
        Vez.spustVeze();
        Mapa.nastavPenize(20000);
        trinactyLevelVlny();
        aktualniLevel = () -> trinactyLevel();
        dalsiLevel = () -> ctrnactyLevel();
        level = 14;
    }

    static private void ctrnactyLevel() {
        puvodniRychlost();
        Vez.spustVeze();
        Mapa.nastavPenize(20000);
        ctrnactyLevelVlny();
        aktualniLevel = () -> ctrnactyLevel();
        dalsiLevel = () -> patnactyLevel();
        level = 15;
    }

    static private void patnactyLevel() {
        puvodniRychlost();
        Vez.spustVeze();
        Mapa.nastavPenize(20000);
        patnactyLevelVlny();
        aktualniLevel = () -> patnactyLevel();
        dalsiLevel = () -> sestnactyLevel();
        level = 16;
    }

    static private void sestnactyLevel() {
        puvodniRychlost();
        Vez.spustVeze();
        Mapa.nastavPenize(20000);
        sestnactyLevelVlny();
        aktualniLevel = () -> sestnactyLevel();
        dalsiLevel = () -> sedmnactyLevel();
        level = 17;
    }

    static private void sedmnactyLevel() {
        puvodniRychlost();
        Vez.spustVeze();
        Mapa.nastavPenize(20000);
        sedmnactyLevelVlny();
        aktualniLevel = () -> sedmnactyLevel();
        dalsiLevel = () -> osmnactyLevel();
        level = 18;
    }

    static private void osmnactyLevel() {
        puvodniRychlost();
        Vez.spustVeze();
        Mapa.nastavPenize(20000);
        osmnactyLevelVlny();
        aktualniLevel = () -> osmnactyLevel();
        dalsiLevel = () -> vyhra();
    }

    static private void vyhra() {
        System.out.println("Výhra");
    }

    static public void spustLevel(int jaky) {
        switch (jaky) {
            case 1:
                prvniLevel();
                break;
            case 2:
                druhyLevel();
                break;
            case 3:
                tretiLevel();
                break;
            case 4:
                ctvrtyLevel();
                break;
            case 5:
                patyLevel();
                break;
            case 6:
                sestyLevel();
                break;
            case 7:
                sedmyLevel();
                break;
            case 8:
                osmyLevel();
                break;
            case 9:
                devatyLevel();
                break;
            case 10:
                desatyLevel();
                break;
            case 11:
                jedenactyLevel();
                break;
            case 12:
                dvanactyLevel();
                break;
            case 13:
                trinactyLevel();
                break;
            case 14:
                ctrnactyLevel();
                break;
            case 15:
                patnactyLevel();
                break;
            case 16:
                sestnactyLevel();
                break;
            case 17:
                sedmnactyLevel();
                break;
            case 18:
                osmnactyLevel();
                break;
        }
    }

    static public void spustPrvniLevel() {
        Mapa.nastavPenize(10000);
        spustBrouky();
        prvniLevel();
    }

    static public void spustDalsiLevel() {
        dalsiLevel.run();
    }

    static public void restartujLevel() {
        zastavBroukyAVlnu(true);
        uklidBrouky();
        seznamTimeline.forEach((t) -> {
            t.stop();
        });
        aktualniLevel.run();
    }

    static public void ztlum(boolean ano) {
        if (ano) {
            hlasitost = 0;
        } else {
            hlasitost = 0.1;
        }
        ztlumeno = ano;
    }

    static public void nastavVelikost() {
        listBrouku.forEach((t) -> {
            t.zmenaVelikosti();
        });
    }

    static public void pochodFunkce() {
        Iterator<NepritelInterface> iter = listBrouku.iterator();
        while (iter.hasNext()) {
            NepritelInterface broucek = iter.next();
            broucek.jdi();
            if (broucek.dosel()) {
                broucek.uklid();
                iter.remove();
            }
        }
    }

    static public void spustBrouky() {
        if (!timelinePohybAll.getKeyFrames().contains(jdiKeyframe)) {
            timelinePohybAll.getKeyFrames().add(jdiKeyframe);
        }
        timelinePohybAll.setCycleCount(Timeline.INDEFINITE);
        timelinePohybAll.play();
    }

    static public void zastavBroukyAVlnu(boolean ano) {
        listBrouku.forEach((t) -> {
            if (!RaketometVez.vratSeznamZmrazenejch().contains(t)) {
                t.pauza(ano);
            }
        });
        zastavAktualniVlnu(ano);
    }

    static public void uklidBrouky() {
        Iterator<NepritelInterface> iter = listBrouku.iterator();
        while (iter.hasNext()) {
            NepritelInterface broucek = iter.next();
            broucek.uklid();
            iter.remove();
        }
    }

    static public void zastavAktualniVlnu(boolean ano) {
        if (ano) {
            seznamTimeline.forEach((t) -> {
                if (t.getStatus().equals(Timeline.Status.RUNNING)) {
                    t.pause();
                }
            });
        } else if (!ano) {
            seznamTimeline.forEach((t) -> {
                if (t.getStatus().equals(Timeline.Status.PAUSED)) {
                    t.play();
                }
            });
        }
        jePauza = ano;
    }

    static private void puvodniRychlost() {
        Chrestovnik.resetujRychlost();
        Drepcik.resetujRychlost();
        Brouk.resetujRychlost();
        Vejirnik.resetujRychlost();
        Rohac.resetujRychlost();
    }

    static public void nastavPole(Rectangle[][] vybranePole) {
        pole = vybranePole;
    }

    static public void nastavList(ObservableList list) {
        list = Hra.vratList();
    }

    static public boolean jeTicho() {
        return ztlumeno;
    }

    static public int vratDokoncenyLevel() {
        return level;
    }

    static public ArrayList<NepritelInterface> vratListZivych() {
        return seznamZivych;
    }

    static public ArrayList<NepritelInterface> vratListBrouku() {
        return listBrouku;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
