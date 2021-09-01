package main;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;




public class Controller implements Initializable {

    public static final String IP = "localhost";
    public static final int PORT = 9898;


    @FXML
    TextField textField;
    @FXML
    Text ammountText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ammountText.setText("0.0");
        refreshAmmount();
    }

    public void refreshAmmount() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {

                try {

                    Socket socket = new Socket(IP, PORT);

                    String command = "get" + "=";

                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(command);

                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    String ammount = (String) objectInputStream.readObject();

                    objectOutputStream.close();
                    objectInputStream.close();
                    socket.close();

                    Platform.runLater(() -> ammountText.setText(ammount));


                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }, 1000, 1000);
    }

    public void setSubmit() {
        int ammount = Integer.parseInt(textField.getText());
        ammountText.setText(Integer.toString(ammount));

        try {

            Socket socket = new Socket(IP, PORT);

            String command = "set" + "=" + ammount;

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(command);
            objectOutputStream.close();
            socket.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


}
