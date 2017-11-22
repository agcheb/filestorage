import java.net.ServerSocket;
import java.net.Socket;

public class MainClassServer {
    public static void main(String[] args) throws Exception {

        SQLHandler.connect();

        new Server();
    }
}
