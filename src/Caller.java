import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Caller {
    private String localNick;
    private String remoteIP;
    private String remoteNick;
    private Command lastCommand;
    private NickCommand nickCommand;
    private String lastError;
    private Logic logic;

    public Caller(String localNick,String remoteIP, Logic logic){
        this.localNick=localNick;
        this.remoteIP=remoteIP;
        this.logic=logic;
    }

    public Connection call() throws IOException {
        Connection connection = new Connection(new Socket(remoteIP,Protocol.PORT_NUMBER));
        //Проверка, к тому ли мы подключились.
        lastCommand = connection.recieve();
        if (lastCommand.type==CommandType.NICK) {
            nickCommand = (NickCommand) lastCommand;
            logic.getMainGui().setConnected(true);
        }
        else{
            connection.disconnect();
            lastError="Wrong IP";
            UltimateGUI ultimateGUI = new UltimateGUI("Failed to connect");
            return null;
        }

        //Проверка, занят ли тот пользователь
        remoteNick = nickCommand.getNick();
        if (nickCommand.isBusy()){
            lastCommand = connection.recieve();
            connection.disconnect();
            UltimateGUI busyGUI = new UltimateGUI(remoteNick+" is busy");
            lastError="User is busy";
            remoteNick=null;
            logic.getMainGui().setConnected(false);
            return null;
        }

        //Если не занят, то отсылаем ник и ждём подтверждения
        connection.sendNickHello(localNick);
        lastCommand = connection.recieve();

        //Если Accept - вернуть connection
        if (lastCommand.type==CommandType.ACCEPT) return connection;
        else{
            UltimateGUI ultimateGUI = new UltimateGUI(remoteNick+" rejected your call");
            lastError="User rejected your call";
            logic.getMainGui().setConnected(false);
            connection.disconnect();
            return null;
        }

    }
    public String getRemoteNick(){
        return remoteNick;
    }
}
