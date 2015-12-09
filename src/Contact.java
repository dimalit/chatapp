public class Contact {
    private boolean isFav,isOnline;
    private String nick,IP;
    private ContactsViewModel contactsViewModel;

    public Contact(String nick,String IP){
        this.nick=nick;
        this.IP=IP;
        isFav=false;
        isOnline=false;
    }

    public Contact(ContactsViewModel cvm,String nick,String IP){
        contactsViewModel=cvm;
        this.nick=nick;
        this.IP=IP;
        isFav=false;
        isOnline=false;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean isFav) {
        this.isFav = isFav;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void changeFav(){
        if (isFav) isFav=false;
        else isFav=true;
        contactsViewModel.updateView();
    }

    public void call(){
        contactsViewModel.call(this);
    }

    public void remove(ContactPanel tmp){
        contactsViewModel.removeContact(this,tmp);

    }

}
