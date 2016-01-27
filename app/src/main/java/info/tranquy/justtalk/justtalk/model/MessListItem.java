package info.tranquy.justtalk.justtalk.model;

/**
 * Created by quyquy on 1/25/2016.
 */
public class MessListItem {

    private int profileID;
    private String name;
    private String status;
    private int stateID;

    public MessListItem(){

    }

    public MessListItem(int profileID, String name, String status, int stateID) {
        this.profileID = profileID;
        this.name = name;
        this.status = status;
        this.stateID = stateID;
    }

    public int getProfileID() {
        return profileID;
    }

    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStateID() {
        return stateID;
    }

    public void setStateID(int stateID) {
        this.stateID = stateID;
    }
}
