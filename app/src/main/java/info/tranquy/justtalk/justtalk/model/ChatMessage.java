package info.tranquy.justtalk.justtalk.model;

/**
 * Created by quyquy on 1/27/2016.
 */
public class ChatMessage {

    private int profileID;
    private String message;
    private String time;
    private boolean mine;

    public int getProfileID() {
        return profileID;
    }

    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean getMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }
}
