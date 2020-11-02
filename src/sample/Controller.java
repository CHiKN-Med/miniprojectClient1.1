package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    public TextArea quizAnswerOptions;
    public TextArea correctAnswer;
    public Button answerButtonOne;
    public Button answerButtonTwo;
    public Button answerButtonThree;
    public Button answerButtonFour;
    public Button sendMessageButton;
    public Button startGame;
    public TextArea winnerNameBox;
    public TextArea scoreBoardBox;

    // socket attributes - >
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;

    // JAVAFX ELEMENTS

    public TextArea chatBox = new TextArea();
    public TextField usernameInput = new TextField();
    public TextField chatMessage = new TextField();
    public TextArea quizBox;

    public AnchorPane scene1;
    public AnchorPane scene2;
    public AnchorPane scene3;
    public AnchorPane scene4;
    public AnchorPane scene5;


    public String ip = "192.168.1.146";
    public boolean iPressed = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scene1.setVisible(true);
        scene2.setVisible(false);
        scene3.setVisible(false);
        scene4.setVisible(false);
        scene5.setVisible(false);

        Socket socket = null;

        try {
        socket = new Socket("localhost", 8000);
        toServer = new DataOutputStream(socket.getOutputStream());
        fromServer = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

            // read thread
            new Thread(() -> {
                // LOBBY LOOP - >
                try {

                while (true) {
                        // READING A NEXT MESSAGE FROM THE SERVER AND APPENDING IT TO CHATBOX IN SCENE2
                        String message = fromServer.readUTF();
                        chatBox.appendText(message);
                        if (message.equalsIgnoreCase("STARTTHEGAME")) {
                            if(!iPressed){
                            sendMessage("");}
                            scene2.setVisible(false);
                            scene3.setVisible(true);
                            break;
                        }
                    }

                // QUIZ LOOP -->
                while (true) {
                        // READ FIRST MESSAGE FROM THE SERVER (THE QUESTION)
                        String message = fromServer.readUTF();
                        // WHEN THE FIRST MESSAGE IS READ CLEAR ALL TEXTAREAS
                        quizBox.clear(); quizAnswerOptions.clear(); correctAnswer.clear();

                        if(message.equalsIgnoreCase("STOPTHEGAME")){
                            scene3.setVisible(false);
                            scene4.setVisible(true);
                            break;
                        }

                        // APPEND QUESTION TO QUIZ BOX
                        quizBox.appendText(message);
                        // READ
                        message = fromServer.readUTF();
                        quizAnswerOptions.appendText(message);
                        message = fromServer.readUTF();
                        correctAnswer.appendText(message);
                        }

                // WAITING LOOP
                while(true){
                    String message = fromServer.readUTF();
                    if(message.equalsIgnoreCase("SHOWTHESCORE"))
                        scene4.setVisible(false);
                        scene5.setVisible(true);
                        break;
                }

                // SCORE
                String messageFromServer  = fromServer.readUTF();
               winnerNameBox.appendText(messageFromServer);
                String messageFromServer2  = fromServer.readUTF();
               scoreBoardBox.appendText(messageFromServer2);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
    }

    public void joinTheServer(ActionEvent actionEvent) throws IOException {
        // SENDING THE USERNAME TO THE SERVER USING THE SENDMESSAGE FUNCTION
        sendMessage(usernameInput.getText());
        scene1.setVisible(false);
        scene2.setVisible(true);
    }


    public void sendMessageButton(ActionEvent actionEvent) {
        sendMessage(chatMessage.getText());
        chatMessage.clear();
    }

    public void sendMessage(String message) {
        try {
            toServer.writeUTF(message);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendInt(int answer) throws IOException{
        toServer.writeInt(answer);
        toServer.flush();
    }

    public int readInt() throws IOException {
        return fromServer.readInt();
    }



    public void startGame(ActionEvent actionEvent) throws IOException {
        // IF A USER CLICKS THE START GAME BUTTON A MESSAGE GETS SEND THAT TELLS THE SERVER TO START THE GAME
        sendMessage("STARTTHEGAME");
        iPressed=true;
    }

    public void answerQOne(ActionEvent actionEvent) throws IOException {
        sendInt(1);/*
        answerButtonOne.setDisable(true);
        answerButtonTwo.setDisable(true);
        answerButtonThree.setDisable(true);
        answerButtonFour.setDisable(true);
        */
    }

    public void answerQTwo(ActionEvent actionEvent) throws IOException {
        sendInt(2);/*
        answerButtonOne.setDisable(true);
        answerButtonTwo.setDisable(true);
        answerButtonThree.setDisable(true);
        answerButtonFour.setDisable(true);
        */
    }

    public void answerQThree(ActionEvent actionEvent) throws IOException {
        sendInt(3);/*
        answerButtonOne.setDisable(true);
        answerButtonTwo.setDisable(true);
        answerButtonThree.setDisable(true);
        answerButtonFour.setDisable(true);
        */
    }

    public void answerQFour(ActionEvent actionEvent) throws IOException {
        sendInt(4);
        /*
        answerButtonOne.setDisable(true);
        answerButtonTwo.setDisable(true);
        answerButtonThree.setDisable(true);
        answerButtonFour.setDisable(true);
        */
    }

}
