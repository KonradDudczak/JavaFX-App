package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.awt.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;


public class Controller implements Initializable  {


    @FXML
    private TextField txt1;

    @FXML
    private Button button;

    @FXML Button kryptogram;

    @FXML
    private Button klucz;


    @FXML
    private Button wiadomosc;

    @FXML
    private ScrollPane scroll;

    @FXML
    private Button decrypting;

    @FXML
    private Button clean;

    static String key;
    static String key_read;

    private static final String desktop = System.getProperty("user.home") + "\\Desktop";


    @FXML
    void buttonPressed(ActionEvent event) {
        String preparation = DESCiphering.prepare();
        if (preparation.equals("err")) {
            JOptionPane.showMessageDialog(null, "Nieznany błąd");
            txt1.clear();
        } else if (!preparation.equals("err")) {
            key = preparation;
            if (DESCiphering.encryptmessage(txt1.getText()) == 0) {
                JOptionPane.showMessageDialog(null, "Nie odnaleziono takiego pliku");
                txt1.clear();
            } else if (DESCiphering.encryptmessage(txt1.getText()) != 0) {
                JOptionPane.showMessageDialog(null, "Przechwycono wiadomość");
                txt1.clear();
            }
        }
    }

    @FXML
    void kryptogramPressed(ActionEvent event) {
        try{

            DataInputStream dis =
                    new DataInputStream (
                            new FileInputStream(desktop +"\\encrypt.txt"));

            byte[] datainBytes = new byte[dis.available()];
            dis.readFully(datainBytes);
            dis.close();

            String content = new String(datainBytes, 0, datainBytes.length);

            Text text = new Text(content);
            text.wrappingWidthProperty().bind(scroll.widthProperty());
            scroll.setFitToWidth(true);
            scroll.setContent(text);

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    void kluczPressed(ActionEvent event) {
        Text text = new Text(key);
        text.wrappingWidthProperty().bind(scroll.widthProperty());
        scroll.setFitToWidth(true);
        scroll.setContent(text);
    }


    @FXML
    void wiadomoscPressed(ActionEvent event) {
        DESCiphering.decryptmessage();

        try{

            DataInputStream dis =
                    new DataInputStream (
                            new FileInputStream(desktop+"\\deliver.txt"));

            byte[] datainBytes = new byte[dis.available()];
            dis.readFully(datainBytes);
            dis.close();

            String content = new String(datainBytes, 0, datainBytes.length);

            Text text = new Text(content);
            text.wrappingWidthProperty().bind(scroll.widthProperty());
            scroll.setFitToWidth(true);
            scroll.setContent(text);

        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    @FXML
    void deszyfrujPressed(ActionEvent event) {

        try{
            DataInputStream dis =
                    new DataInputStream (
                            new FileInputStream(desktop+ "\\key.txt"));
            byte[] datainBytes = new byte[dis.available()];
            dis.readFully(datainBytes);
            dis.close();
            String content = new String(datainBytes, 0, datainBytes.length);
            byte[] decodedKey = Base64.getDecoder().decode(content);
            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "DESede");
            int check = DESCiphering.decryptmessagewithkey(txt1.getText(), originalKey);

            if(check <1){
                JOptionPane.showMessageDialog(null, "Błąd! Brak kryptogramu");
                txt1.clear();}

                DataInputStream dis1 =
                        new DataInputStream (
                                new FileInputStream(desktop + "\\deliver.txt"));

                byte[] datainBytes1 = new byte[dis1.available()];
                dis1.readFully(datainBytes1);
                dis1.close();

                String content1 = new String(datainBytes1, 0, datainBytes1.length);

                Text text1 = new Text(content1);
                text1.wrappingWidthProperty().bind(scroll.widthProperty());
                scroll.setFitToWidth(true);
                scroll.setContent(text1);

        }catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Nie można pobrać klucza");
            txt1.clear();

        }


    }

    @FXML
    void cleanPressed(ActionEvent event) {

        File file1 = new File(desktop+"\\encrypt.txt");
        File file2 = new File(desktop+"\\deliver.txt");
        File file3 = new File(desktop+"\\key.txt");

        file1.delete();
        file2.delete();
        file3.delete();

        txt1.clear();
        scroll.setContent(null);

    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}