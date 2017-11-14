import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by agcheb on 14.11.17.
 */
public class CoreServer {
    private static Map<String, ConnectionClientServer> connectionMap = new ConcurrentHashMap<String, ConnectionClientServer>();
    private final int port = 9999;


    public CoreServer(){
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            while (true){
                Socket socket = serverSocket.accept();
                new FilesThreadServer(socket).start();
            }
        } catch (Exception e) {
            System.out.println("Возникла ошибка");
            e.printStackTrace();
        }
    }
}

