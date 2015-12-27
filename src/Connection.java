import java.io.*;
import java.net.Socket;
import java.util.Scanner;

class Connection{
    private Socket socket;
    private Scanner in;
    private DataOutputStream out;
    private CommandType lastCommand;

    public Connection(Socket socket){
        this.socket=socket;
        try {
            in = new Scanner(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            //ошибка при попытке создать Connection, как-то показать
        }
    }

    public void sendServerHello(){
        try {
            out.write(new StringBuilder(Protocol.HELLO_SERVER).append("\n").toString().getBytes("UTF-8"));
            lastCommand=CommandType.HELLO_CLIENT;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCall(String nick){
        try {
            out.write(new StringBuilder(Protocol.CALL).append(" ").append(nick).append("\n").toString().getBytes("UTF-8"));
            lastCommand=CommandType.CALL;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendLogin(String nick,String password){
        try {
            out.write(new StringBuilder(Protocol.LOGIN).append(" ").append(nick).append(" ").append(password.hashCode()).append("\n").toString().getBytes("UTF-8"));
            lastCommand=CommandType.LOGIN;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendSignUp(String nick,String password){
        try {
            out.write(new StringBuilder(Protocol.SIGNUP).append(" ").append(nick).append(" ").append(password.hashCode()).append("\n").toString().getBytes("UTF-8"));
            lastCommand=CommandType.SIGNUP;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void accept(){
        try {
            out.write(new StringBuilder(Protocol.ACCEPTED).append("\n").toString().getBytes("UTF-8"));
            lastCommand=CommandType.ACCEPT;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reject(){
        try {

            out.write(new StringBuilder(Protocol.REJECTED).append("\n").toString().getBytes("UTF-8"));
            lastCommand=CommandType.REJECT;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        try {
            out.write(new StringBuilder(Protocol.MESSAGE).append("\n").append(message).append("\n").toString().getBytes("UTF-8"));
            lastCommand=CommandType.MESSAGE;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        try{
            out.write(new StringBuilder(Protocol.DISCONNECT).append("\n").toString().getBytes("UTF-8"));
            socket.close();
            in.close();
            out.close();
            lastCommand=CommandType.DISCONNECT;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void logout(){
        try {
            out.write(new StringBuilder(Protocol.LOGOUT).append("\n").toString().getBytes("UTF-8"));
            lastCommand=CommandType.LOGOUT;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnectFromUser(){
        try{
            out.write(new StringBuilder(Protocol.DISCONNECT_FROM_USER).append("\n").toString().getBytes("UTF-8"));
            lastCommand=CommandType.DISCONNECT_FROM_USER;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getContacts(String regex){
        try {
            out.write(new StringBuilder(Protocol.GET_CONTACTS).append(" ").append(regex).append("\n").toString().getBytes("UTF-8"));
            lastCommand=CommandType.GET_CONTACTS;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getMyContacts(){
        try {
            out.write(new StringBuilder(Protocol.GET_MY_CONTACTS).append("\n").toString().getBytes("UTF-8"));
            lastCommand=CommandType.GET_MY_CONTACTS;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendContacts(String contacts){
        try {
            out.write(new StringBuilder(Protocol.CONTACTS).append(contacts).append("\n").toString().getBytes("UTF-8"));
            lastCommand=CommandType.CONTACTS;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getOnline(String s){
        try {
            out.write(new StringBuilder(Protocol.ONLINE_CONTACTS).append(s).append("\n").toString().getBytes("UTF-8"));
            lastCommand=CommandType.ONLINE_CONTACTS;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public Command recieve(){
        return  Command.getCommand(in);
    }
}
