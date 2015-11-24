import java.io.IOException;
import java.sql.*;

public class CallListenerThread implements Runnable {

    private String localNick;
    private CallListener callListener;
    private boolean isBusy;
    private String remoteIP;
    private String lastAction;
    private boolean stop;
    private String remoteNick;
    private Connection remoteConnection;
    private AccorDis form;
    Logic logic;



    public CallListenerThread(String localNick,Boolean isBusy,Logic logic){
        callListener = new CallListener(localNick, false);
        this.logic = logic;
    }

    public void run() {
        try {
            while (!stop) {

                remoteConnection = callListener.getConnection();
                if (remoteConnection == null) continue;
                remoteNick=callListener.getRemoteNick();
                form = new AccorDis(remoteConnection,logic,remoteNick);
                System.out.println("AZAZA");
                logic.setRemoteNick(remoteNick);
            }
        }
            catch(IOException e){
                e.printStackTrace();
            }


    }


    public Connection getRemoteConnection(){
        return remoteConnection;
    }

    public void setBusy(Boolean isBusy){
        this.isBusy=isBusy;
        callListener.setBusy(isBusy);
    }
    
    public void setNick(String nick){
    	localNick=nick;
    	callListener.setNick(localNick);
    }


    public void kill() {
        stop = true;
    }
}