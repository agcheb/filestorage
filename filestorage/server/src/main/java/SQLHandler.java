import java.sql.*;

public class SQLHandler {
    private static Connection connection;
    private  static Statement stmt;
    private static PreparedStatement psFindNickByLogin;

    public static void connect() throws Exception {
        Class.forName("org.sqlite.JDBC");
        DriverManager.getConnection("jdbc:sqlite:users.db");
        stmt = connection.createStatement();
        //prepare();
        psFindNickByLogin = connection.prepareStatement("SELECT nick FROM clients WHERE login = ? AND password = ?;");
    }
    public static String getNickByLoginPass(String login, String pass){
        try {
            psFindNickByLogin.setString(1,login);
            psFindNickByLogin.setString(2,pass);
            ResultSet rs = psFindNickByLogin.executeQuery();
            if(rs.next()){
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static void prepare() {
        try {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS clients (id INTEGER, login TEXT,password TEXT,nick TEXT);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
