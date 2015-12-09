import java.util.HashMap;


public class CommandListenerThread  implements Runnable {
    private Command lastCommand;
    private Connection connection;
    private boolean stop;
    Logic logic;

    public CommandListenerThread(Connection connection, Logic logic){
        this.connection=connection;
        this.logic=logic;
    }

    public void run() {
        while (!stop) {
            lastCommand = connection.recieve();
            if (lastCommand==null) continue;
            if (lastCommand.type==CommandType.MESSAGE){
                MessageCommand mc = (MessageCommand) lastCommand;
                logic.addMessage(mc.message);
            }

            if (lastCommand.type==CommandType.DISCONNECT){
                logic.disconnect();
            }
        }
    }




    public void kill(){
        stop = true;
    }
}
