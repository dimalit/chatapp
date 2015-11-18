import java.io.IOException;
import java.util.Observable;

public class CommandListenerThread extends Observable implements Runnable {
	private boolean stopFlag;
	private boolean disconnected;
	private Connection con;
	private Command lastCommand;

	public CommandListenerThread() {
		// TODO Auto-generated constructor stub
	}

	public CommandListenerThread(Connection connection) {
		// TODO Auto-generated constructor stub
	}

	void setConnection(Connection con) {
		disconnected = false;
		this.con = con;
	}

	Command getLastCommand() {
		return lastCommand;
	}

	boolean isDisconnected() {
		return disconnected;
	}

	public void run() {
		while (!disconnected) {
			try {
				synchronized (this) {
					this.lastCommand = con.receive();
					if ((lastCommand.type == (Command.CommandType.DISCONNECT)
							|| (lastCommand.type == (Command.CommandType.REJECT))))
						disconnected=true;
					setChanged();
					notifyObservers();
				}
			} catch (IOException e) {

				e.printStackTrace();
			}

		}

	}

	void start() {
		Thread t = new Thread();
		t.start();
	}

	void stop() {
		disconnected = true;

	}

}