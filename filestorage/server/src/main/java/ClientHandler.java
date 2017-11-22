import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.function.Consumer;


public class ClientHandler {
    private String nick;
    private Server server;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientHandler(Server server, Socket socket){
        this.server = server;
        this.socket = socket;

        try {
            in = new ObjectInputStream((socket.getInputStream()));
            out = new ObjectOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        while (true) {
                            Message msg = (Message) in.readObject();

                            if (msg instanceof AuthMessage) {
                                AuthMessage authMessage = (AuthMessage) msg;
                                String nickFromDb = SQLHandler.getNickByLoginPass(authMessage.getLogin(),authMessage.getPassword());
                                if (nickFromDb!= null){
                                    nick = nickFromDb;
                                    sendMsg(new CommandMessage(CommandMessage.AUTH_OK, nick));
                                    sendMsg(getFileStructureMessage());
                                    break;
                                }
                            }
                        }
                        while (true){
                            Message msg = (Message) in.readObject();
                            if(msg instanceof FileDataMessage){
                                FileDataMessage fileDataMessage = (FileDataMessage) msg;
                                Files.write(Paths.get("server/storage/" + nick + "/" + fileDataMessage.getFilename()), fileDataMessage.getData(), StandardOpenOption.CREATE);
                            }
                            if(msg instanceof CommandMessage){
                                CommandMessage cmdM = (CommandMessage) msg;
                                if(cmdM.getCmd() == CommandMessage.DOWNLOAD_FILE){
                                    FileDataMessage fdm = new FileDataMessage("server/storage/" + nick + "/" + cmdM.getAttachment()[0]);
                                    sendMsg(fdm);
                                }
                                if(cmdM.getCmd() == CommandMessage.FILES_LIST_REQUEST){
                                    sendMsg(getFileStructureMessage());
                                }
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public FileListMessage getFileStructureMessage(){
        return new FileListMessage(getFilesList());
    }
    public List<String> getFilesList() {
        List<String> files = null;
        try {
            Files.newDirectoryStream(Paths.get("server/storage/" + nick)).forEach(path -> files.add(path.getFileName().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    public void sendMsg(Message message) {

        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
