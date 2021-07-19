package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class Main extends Application  {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("3DES_DUDCZAK");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();



    }

    public static void main(String[] args) {
        launch(args);

        Shutdown shutDownTask = new Shutdown();

        Runtime.getRuntime().addShutdownHook(shutDownTask);

        System.exit(0);


    }
}
