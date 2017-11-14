import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Map;

/**
 * Created by agcheb on 14.11.17.
 */
public class FilesThreadServer extends Thread{
    private Socket socket;
    FilesThreadServer(Socket socket){
        this.socket = socket;
    }



    //Рукопожатие с пользователя с сервером
//    private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException{
//        String nonAuthUserName=null;
//        connection.send(new Message(MessageType.NAME_REQUEST));
//
//        Message ans = connection.receive();
//        while (!ans.getType().equals(MessageType.USER_NAME)) {
//            connection.send(new Message(MessageType.NAME_REQUEST));
//            ans = connection.receive();
//        }
//
//        nonAuthUserName = ans.getData();
//        if(nonAuthUserName.isEmpty()||connectionMap.containsKey(nonAuthUserName)) nonAuthUserName=serverHandshake(connection);
//        else {
//            connectionMap.put(nonAuthUserName, connection);
//            connection.send(new Message(MessageType.NAME_ACCEPTED));
//            return nonAuthUserName;
//        }
//        return nonAuthUserName;
//    }
//
//    //Отправка списка пользователей пользователю
//    private void sendListOfUsers(Connection connection, String userName) throws IOException{
//        for(Map.Entry<String, Connection> entry : connectionMap.entrySet()) {
//            String key = entry.getKey();
//            if(key.equals(userName))continue;
//            Message msg = new Message(MessageType.USER_ADDED,key);
//            connection.send(msg);
//        }
//
//    }
//
    private void serverMainLoop(ConnectionClientServer connection, String userName) throws IOException, ClassNotFoundException {

        while (!Thread.currentThread().isInterrupted()) {
            Message msg = connection.receive();
//            if (msg.getType() == MessageType.TEXT) {
//                String newmsg = userName + ": " + msg.getData();
//                sendBroadcastMessage(new Message(MessageType.TEXT, newmsg));
//            } else ConsoleHelper.writeMessage("Error!");
        }
    }
//
    public void run(){
        SocketAddress sadr = socket.getRemoteSocketAddress();
        ConsoleHelper.writeMessage("соединение устанавливается " + sadr);

        String uName = new String();
        try (ConnectionClientServer connection = new ConnectionClientServer(socket);){

//            uName = serverHandshake(connection);
//            sendBroadcastMessage(new Message(MessageType.USER_ADDED,uName));
//            sendListOfUsers(connection,uName);
//            serverMainLoop(connection,uName);

        } catch (IOException e) {
            ConsoleHelper.writeMessage("Произошла ошибка соединения с удаленным адресом");
        } finally {
            if (!uName.isEmpty()){
//                connectionMap.remove(uName);
//                sendBroadcastMessage(new Message(MessageType.USER_REMOVED,uName));
            }
        }

        ConsoleHelper.writeMessage("соединение с удаленным адресом закрыто");
    }
}
