import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by agcheb on 14.11.17.
 */
public class ConsoleHelper {
    private static BufferedReader consoleHelper = new BufferedReader(new InputStreamReader(System.in));


    public static void writeMessage(String msg){
        System.out.println(msg);
    }
    public static String readString(){
        String msg = null;
        try {
            msg = consoleHelper.readLine();
        } catch (IOException e) {
            System.out.println("Произошла ошибка при попытке ввода текста. Попробуйте еще раз.");
            msg = readString();

        }
        return msg;
    }

    public static int readInt(){
        int res  = 0;
        try {
            res = Integer.parseInt(readString());
        }
        catch (NumberFormatException e){
            System.out.println("Произошла ошибка при попытке ввода числа. Попробуйте еще раз.");
            res = Integer.parseInt(readString());
        }
        return res;
    }
}
