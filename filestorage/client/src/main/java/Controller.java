import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

public class Controller implements  Initializable{
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String nick;

    @FXML
    TextField loginField;

    @FXML
    PasswordField passField;


    @FXML
    HBox authPanel;

    @FXML
    HBox cmdPanel;


    @FXML
    ListView<String> mainList;

    public void setNick(String nick){
        if(nick == null){
            authPanel.setVisible(true);
            authPanel.setManaged(true);
            cmdPanel.setVisible(false);
            cmdPanel.setManaged(false);
        }
        else {
            authPanel.setVisible(false);
            authPanel.setManaged(false);
            cmdPanel.setVisible(true);
            cmdPanel.setManaged(true);

        }
    }

    public void connect(){
        try {
            socket = new Socket("localhost",8189);
             out = new ObjectOutputStream(socket.getOutputStream());
             in = new ObjectInputStream(socket.getInputStream());

             Thread t = new Thread(new Runnable() {
                 public void run() {
                     try {
                         while (true) {
                             Message msg = (Message)in.readObject();
                             if(msg instanceof CommandMessage){
                                 CommandMessage cm = (CommandMessage)msg;
                                 if(cm.getCmd() == CommandMessage.AUTH_OK){
                                     setNick((String)cm.getAttachment()[0]);
                                     break;
                                 }
                             }
                         }
                         while (true){
                             Message msg = (Message)in.readObject();
                             if(msg instanceof FileListMessage){
                                 FileListMessage fm = (FileListMessage)msg;

                                 Platform.runLater(() -> {
                                     mainList.getItems().clear();
                                     for (int i = 0; i < fm.getFiles().size(); i++) {
                                         mainList.getItems().add(fm.getFiles().get(i));
                                     }
                                 });

                             }
                             if(msg instanceof FileDataMessage){
                                 FileDataMessage fileDataMessage = (FileDataMessage) msg;
                                 Files.write(Paths.get("client/local_storage/"  + fileDataMessage.getFilename()), fileDataMessage.getData(), StandardOpenOption.CREATE_NEW);
                             }

                         }
                     }
                     catch (Exception e){
                         e.printStackTrace();
                     } finally {
                         try {
                             socket.close();
                         } catch (IOException e) {
                             e.printStackTrace();
                         }
                     }
                 }
             });
             t.setDaemon(true);
             t.start();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void downloadFile(){
        String filename = mainList.getSelectionModel().getSelectedItem();
        CommandMessage cmdm = new CommandMessage(CommandMessage.DOWNLOAD_FILE,filename);
        try {
            out.writeObject(cmdm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendAuthMsg(String login,String pass){
        if(socket == null || socket.isClosed()){
            connect();
        }
        AuthMessage am = new AuthMessage(loginField.getText(),passField.getText());
        loginField.clear();
        passField.clear();
        try {
            out.writeObject(am);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
