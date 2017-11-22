import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    private ServerSocket serverSocket;

    public Server() {
        try (ServerSocket socket = new ServerSocket(8189)){
            SQLHandler.connect();
            this.serverSocket = socket;
            ConsoleHelper.writeMessage("Сервер запущен");
            while (true){
                Socket client = serverSocket.accept();
                ConsoleHelper.writeMessage("Клиент подключился");
                new ClientHandler(this, client);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SQLHandler.disconnect();
    }
}
