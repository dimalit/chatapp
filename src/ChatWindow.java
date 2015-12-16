import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.LinkedList;
import java.text.SimpleDateFormat;

public class ChatWindow extends JFrame implements Observer {
	private CallListenerThread callt;
	private CommandListenerThread comt;
	public static Observer observer;
	private Connection connection;

	final JPanel panel = new JPanel();
	final JPanel field1 = new JPanel();
	final JPanel field2 = new JPanel();
	final JPanel field3 = new JPanel();
	final JPanel messages = new JPanel();// new
	final JPanel friendsfield = new JPanel();// new
	final JPanel bigfield = new JPanel();// new
	final JPanel fieldmess = new JPanel();
	final JPanel field = new JPanel();
	final JPanel messArea = new JPanel();
	final JButton apply = new JButton("Apply");
	final JButton connect = new JButton("Connect");
	final JButton disconnect = new JButton("Disconnect");
	final JButton sendb = new JButton("Send");

	final JLabel locallogin = new JLabel("local login");
	final JLabel remotelogin = new JLabel("remote login");
	final JLabel txt3 = new JLabel("remote addr");

	final JTextField text1 = new JTextField();
	final JTextField text2 = new JTextField();
	final JTextField text3 = new JTextField();
	final JTextArea textmess = new JTextArea();
	final JTextArea mess = new JTextArea();

	final JLabel txtfriends = new JLabel("Your friends:");// new

	public ChatWindow() throws IOException {
		observer = this;
		this.setSize(650, 500);
		this.setTitle("This is CHAT ");
		ImageIcon image = new ImageIcon("F:\\chaticon.jpg");
		this.setIconImage(image.getImage());
		callt = new CallListenerThread();
		callt.start();

		// import contacts to linked list "listfriends"
		LinkedList<Friend> listfriends = new LinkedList();// linked list of
															// friends
		DefaultListModel<String> listModel = new DefaultListModel();// list of
																	// nicks of
																	// friends
																	// for form
		Protocol.serverConnection
				.setServerAddress("jdbc:mysql://files.litvinov.in.ua/chatapp_server?characterEncoding=utf-8&useUnicode=true");
		Protocol.serverConnection.connect();
		if (Protocol.serverConnection.isConnected() == false) {
			mess.append("[System] Could not connect to the server" + "\n");
		} else {
			mess.append("[System] You connected to the server" + "\n");
		}

		Protocol.serverConnection.setLocalNick(Protocol.localNick);
		Protocol.serverConnection.goOnline();

		JList<String> list = new JList(listModel);
		Friend fr = new Friend("masha", "10.1.1.4");
		Friend fr1 = new Friend("unnamed", "5555");
		listfriends.add(fr);
		listfriends.add(fr1);
		listModel.addElement(fr.getNick());
		listModel.addElement(fr1.getNick());

		list.setMaximumSize(new Dimension(150, 2500));
		list.setPreferredSize(new Dimension(150, 2500));
		list.setLayoutOrientation(JList.VERTICAL);

		JButton addButton = new JButton("Add");
		addButton.setFocusable(false);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Friend newfr = new Friend(Protocol.remoteNick, Protocol.IP);
				listfriends.add(newfr);

				listModel.addElement(newfr.getNick());
				int index = listModel.size() - 1;
				list.setSelectedIndex(index);
				list.ensureIndexIsVisible(index);
			}
		});

		JButton connectButton = new JButton("Connect");
		connectButton.setFocusable(false);
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = list.getSelectedIndex();
				for (int i = 0; i <= listfriends.size(); i++) {
					if (listfriends.get(i).getNick() == listModel
							.getElementAt(index)) {
						String Ip = listfriends.get(i).getIp();// ip for
																// connection
						break;
					}
				}
				text2.setText(listModel.getElementAt(index));
				// need connect to Ip
				text3.setText(listfriends.get(index).getIp());

			}
		});

		final JButton removeButton = new JButton("Remove");
		removeButton.setFocusable(false);
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = list.getSelectedIndex();
				for (int i = 0; i <= listfriends.size(); i++) {
					if (listfriends.get(i).getNick() == listModel
							.getElementAt(index)) {
						listfriends.remove(i);// remove from list of friends
						break;
					}
				}
				listModel.remove(list.getSelectedIndex());// remove from list of
															// nicks
			}
		});

		removeButton.setMaximumSize(new Dimension(100, 25));
		addButton.setMaximumSize(new Dimension(100, 25));
		connectButton.setMaximumSize(new Dimension(100, 25));

		friendsfield.setLayout(new BoxLayout(friendsfield, BoxLayout.Y_AXIS));
		bigfield.setLayout(new BoxLayout(bigfield, BoxLayout.X_AXIS));
		messages.setLayout(new BoxLayout(messages, BoxLayout.Y_AXIS));
		messArea.setLayout(new BoxLayout(messArea, BoxLayout.X_AXIS));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		field.setLayout(new BoxLayout(field, BoxLayout.X_AXIS));
		fieldmess.setLayout(new BoxLayout(fieldmess, BoxLayout.X_AXIS));
		field1.setLayout(new BoxLayout(field1, BoxLayout.Y_AXIS));
		field2.setLayout(new BoxLayout(field2, BoxLayout.Y_AXIS));
		field3.setLayout(new BoxLayout(field3, BoxLayout.Y_AXIS));
		txtfriends.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		txtfriends.setAlignmentY(JComponent.BOTTOM_ALIGNMENT);
		list.setAlignmentY(JComponent.TOP_ALIGNMENT);
		connectButton.setAlignmentY(JComponent.BOTTOM_ALIGNMENT);
		addButton.setAlignmentY(JComponent.BOTTOM_ALIGNMENT);
		removeButton.setAlignmentY(JComponent.BOTTOM_ALIGNMENT);
		connectButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		addButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		removeButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		textmess.setLineWrap(true);
		mess.setCaretPosition(0);
		mess.setLineWrap(true);
		mess.setEditable(false);

		textmess.setMaximumSize(new Dimension(1500, 50));
		textmess.setPreferredSize(new Dimension(70, 50));
		text1.setMaximumSize(new Dimension(100, 25));
		text2.setMaximumSize(new Dimension(100, 25));
		text3.setMaximumSize(new Dimension(100, 25));
		text2.setEnabled(false);

		apply.setPreferredSize(new Dimension(100, 25));
		apply.setMaximumSize(new Dimension(100, 25));
		connect.setPreferredSize(new Dimension(100, 25));
		connect.setMaximumSize(new Dimension(100, 25));
		disconnect.setPreferredSize(new Dimension(100, 25));
		disconnect.setMaximumSize(new Dimension(100, 25));
		sendb.setMaximumSize(new Dimension(70, 50));
		sendb.setPreferredSize(new Dimension(70, 50));
		disconnect.setEnabled(false);


		apply.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		text1.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		locallogin.setAlignmentX(JComponent.CENTER_ALIGNMENT);

		connect.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		text2.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		remotelogin.setAlignmentX(JComponent.CENTER_ALIGNMENT);

		disconnect.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		text3.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		txt3.setAlignmentX(JComponent.CENTER_ALIGNMENT);

		field1.add(locallogin);
		field2.add(txt3);
		field3.add(remotelogin);
		field1.add(text1);
		field2.add(text3);
		field3.add(text2);
		field1.add(apply);
		field2.add(connect);
		field3.add(disconnect);
		messArea.add(textmess);
		messArea.add(sendb);

		friendsfield.setBackground(new Color(220, 243, 246));
		bigfield.setBackground(new Color(220, 243, 246));
		panel.setBackground(new Color(220, 243, 246));
		field1.setBackground(new Color(220, 243, 246));
		field2.setBackground(new Color(220, 243, 246));
		field3.setBackground(new Color(220, 243, 246));
		messArea.setBackground(new Color(220, 243, 246));
		mess.setBackground(new Color(237, 245, 246));

		connectButton.setBackground(new Color(116, 199, 209));
		addButton.setBackground(new Color(116, 199, 209));
		removeButton.setBackground(new Color(116, 199, 209));
		apply.setBackground(new Color(116, 199, 209));
		connect.setBackground(new Color(116, 199, 209));
		disconnect.setBackground(new Color(116, 199, 209));
		sendb.setBackground(new Color(116, 199, 209));

		class SendAction implements ActionListener {
			private String message;

			public void actionPerformed(ActionEvent event) {
				message = textmess.getText();
				long currentTimeMillis = System.currentTimeMillis();

				try {
					if (connection != null) {
						if (comt != null) {
							String time = new SimpleDateFormat("HH:mm:ss")
									.format(currentTimeMillis);
							mess.append(Protocol.localNick + " (" + time + "):" + "\n"
									+ "   " + message + "\n");
							comt.getConnection().sendMessage(message);
						} else {
							String time = new SimpleDateFormat("HH:mm:ss")
									.format(currentTimeMillis);
							mess.append(Protocol.localNick + " (" + time + "):" + "\n"
									+ "   " + message + "\n");
							callt.getConnection().sendMessage(message);
						}
					}
					}catch(IOException e){
						e.printStackTrace();
					}

				textmess.setText("");
			}
		}

		class ApplyAction implements ActionListener {
			public void actionPerformed(ActionEvent event) {
				Protocol.localNick = text1.getText();
				Protocol.serverConnection.setLocalNick(Protocol.localNick);
				mess.append("[System] Nickname changed to: "
						+ Protocol.localNick + "\n");
				apply.setEnabled(false);
			}
		}

		class ConnectAction implements ActionListener {
			public void actionPerformed(ActionEvent event) {
				mess.setText("");
				Protocol.IP = text3.getText();
				connect.setEnabled(false);
				disconnect.setEnabled(true);
				textmess.setEnabled(true);
				try {
					Caller caller = new Caller();
					connection = caller.call();
					if (connection != null) {
						comt = new CommandListenerThread(connection);
						comt.addObserver(ChatWindow.this);
						mess.append("Connected to " + Protocol.IP + "\n");
						comt.start();

					} else {
						mess.append("IP: " + Protocol.IP + " inaccessible" + "\n");
						connect.setEnabled(true);
						disconnect.setEnabled(false);
						sendb.setEnabled(false);
						apply.setEnabled(true);
					}
					text2.setText(Protocol.remoteNick);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		class DisconnectAction implements ActionListener {
			public void actionPerformed(ActionEvent event) {
				if (comt != null) {
					try {
						comt.getConnection().disconnect();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				comt.stop();
				comt = null;
				sendb.setEnabled(false);
				disconnect.setEnabled(false);
				connect.setEnabled(true);
				apply.setEnabled(true);
				text2.setEnabled(false);
				mess.append("-> Disconnected\n");
			}
		}

		SendAction send = new SendAction();
		sendb.addActionListener(send);
		ApplyAction applyact = new ApplyAction();
		apply.addActionListener(applyact);
		ConnectAction connectact = new ConnectAction();
		connect.addActionListener(connectact);
		DisconnectAction disconnectact = new DisconnectAction();
		disconnect.addActionListener(disconnectact);

		friendsfield.add(txtfriends);
		friendsfield.add(list);
		friendsfield.add(connectButton);
		friendsfield.add(addButton);
		friendsfield.add(removeButton);
		fieldmess.add(mess);
		final JScrollPane scrollPane = new JScrollPane(mess);
		fieldmess.add(scrollPane);

		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		field.add(field1);
		field.add(field2);
		field.add(field3);
		panel.add(field);
		messages.add(fieldmess);
		messages.add(messArea);
		bigfield.add(messages);
		bigfield.add(friendsfield);
		panel.add(bigfield);

		this.add(panel);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					new ChatWindow();
				} catch (IOException e) {
				}
			}
		});
	}

	@Override
	public void update(Observable o, Object arg) {

		NickCommand c;
		MessageCommand mescom;
		Command com;

		sendb.setEnabled(true);
		connect.setEnabled(false);
		if (arg instanceof NickCommand) {
			c = (NickCommand) arg;
			Protocol.remoteNick = c.getNick();
			mess.append(c.intoString() + "\n");
		}
		if (arg instanceof MessageCommand) {
			mescom = (MessageCommand) arg;

			long currentTimeMillis = System.currentTimeMillis();
			String time = new SimpleDateFormat("HH:mm:ss")
					.format(currentTimeMillis);
			mess.append(Protocol.remoteNick + " (" + time + "):" + "\n" + "   "
					+ mescom.getMessagetext() + "\n");
		}
		if (arg instanceof Command) {
			com = (Command) arg;
			if (com.getType() == Command.CommandTypes.accept) {
				connect.setEnabled(false);
				disconnect.setEnabled(true);
				sendb.setEnabled(true);
				apply.setEnabled(false);
			}
			if (com.getType() == Command.CommandTypes.reject) {
				mess.append("It seems like " + Protocol.remoteNick + " doesn't want to talk to you :c" + "\n");
				connect.setEnabled(true);
				disconnect.setEnabled(false);
				sendb.setEnabled(false);
				apply.setEnabled(true);
			}
			if (com.getType() == Command.CommandTypes.disconnect) {
				mess.append("Looks like we lost him :c" + "\n");
				connect.setEnabled(true);
				disconnect.setEnabled(false);
				sendb.setEnabled(false);
				apply.setEnabled(true);
			}
			if (com.getType() == Command.CommandTypes.busy) {
				mess.append(com.toString());
				connect.setEnabled(true);
				disconnect.setEnabled(false);
				sendb.setEnabled(false);
				apply.setEnabled(true);
			}
		}
	}
}

class Friend {
	public String nick;
	public String ip;

	public Friend(String n, String i) {
		this.nick = n;
		this.ip = i;
	}

	void set(String n, String i) {
		this.nick = n;
		this.ip = i;
	}

	String getNick() {
		return nick;
	}

	String getIp() {
		return ip;
	}
}
