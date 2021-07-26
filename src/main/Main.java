package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage popUp;
    private static Stage primaryStage;


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/main/view/root.fxml"));
        primaryStage.setTitle("Gato");
        primaryStage.setScene(new Scene(root, 870, 600));
        primaryStage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/"+ fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void popUp(String fxml, String title) {
        try {
            Parent node = loadFXML(fxml);
            popUp = new Stage();
            Scene scene = new Scene(node);
            popUp.setScene(scene);
            popUp.setTitle(title);
            popUp.initOwner(primaryStage);
            popUp.initModality(Modality.WINDOW_MODAL);
            popUp.centerOnScreen();
            popUp.show();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}

