package javafxapplication1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class JavaFXApplication1 extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest((WindowEvent e) -> {
            Platform.exit();
            System.exit(0);
        });
        stage.setTitle("Tower Defense");

    }

    public static void main(String[] args) {
        launch(args);
    }

}
