package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    // socket attributes - >
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;

    // JAVAFX ELEMENTS

    public TextArea chatBox;
    public TextField usernameInput;
    public TextField chatMessage;

    public void sendMessage(String message) throws IOException {
        toServer.writeUTF(message);
        toServer.flush();
    }

    public String readMessage() throws IOException {
        return fromServer.readUTF();
    }

    public void joinTheServer(ActionEvent actionEvent) throws IOException {
        //CHANGING SCENE TO THE SCENE2.FXML WITH THE FOLLOWING CODE
        Parent scene2Parent = FXMLLoader.load(getClass().getResource("scene2.fxml"));
        Scene scene2 = new Scene(scene2Parent);
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene2);
        stage.show();

        // SENDING THE USERNAME TO THE SERVER USING THE SENDMESSAGE FUNCTION
        sendMessage(usernameInput.getText());
    }

    public void sendMessage(ActionEvent actionEvent) throws IOException {
        sendMessage(chatMessage.getText());
        chatMessage.clear();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
        // Create a socket to connect to the server
        Socket socket = new Socket("localhost", 8000);
        // Create an input stream to receive data from the server
        fromServer = new DataInputStream(socket.getInputStream());
        // Create an output stream to send data to the server
        toServer = new DataOutputStream(socket.getOutputStream());

        // read thread
        new Thread(() -> {
            // LOBBY LOOP - >
            while(true){
                try {
                    // READING A NEXT MESSAGE FROM THE SERVER AND APPENDING IT TO CHATBOX IN SCENE2
                    chatBox.appendText(readMessage());

                    if(readMessage().equalsIgnoreCase("START GAME")){
                        break;
                    }

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }



        }).start();


        } catch (IOException e) {
            e.printStackTrace();
        }






    }


}
