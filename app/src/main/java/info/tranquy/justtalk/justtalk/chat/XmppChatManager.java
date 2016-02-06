package info.tranquy.justtalk.justtalk.chat;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.chatstates.ChatStateManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import info.tranquy.justtalk.justtalk.configuration.Configuration;
import info.tranquy.justtalk.justtalk.network.NetworkManager;

/**
 * Created by quyquy on 1/25/2016.
 */
public class XmppChatManager {
    private static XmppChatManager ourInstance = new XmppChatManager();
    private XMPPTCPConnectionConfiguration.Builder configBuilder;
    private String serverIP = Configuration.serverIP;
    private int securePort = Configuration.securePort;
    private int nonSecurePort = Configuration.nonSecurePort;
    private int maxMessageLength = Configuration.maxMessageLength;
    private AbstractXMPPConnection connection;
    private boolean isSetUsernameAndPassword = false;
    //private NetworkManager networkManager;
    //private ArrayList<Chat> activateChatList = null;
    private boolean onlineMode;



    private XmppChatManager() {
        configBuilder = XMPPTCPConnectionConfiguration.builder();
        configBuilder.setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled);
        configBuilder.setServiceName(serverIP);
        configBuilder.setHost(serverIP);
        configBuilder.setPort(nonSecurePort);
        configBuilder.setDebuggerEnabled(true);
        onlineMode = true;
    }

    public void setUsernameAndPassword(String username, String password) {
        configBuilder.setUsernameAndPassword(username, password);
        isSetUsernameAndPassword = true;
    }

    private boolean newConnection(){
        connection = new XMPPTCPConnection(configBuilder.build());
        connection.addConnectionListener(new XMPPConnectionListener());
        ReconnectionManager manager = ReconnectionManager.getInstanceFor(connection);
        manager.enableAutomaticReconnection();
        manager.setReconnectionPolicy(ReconnectionManager.ReconnectionPolicy.RANDOM_INCREASING_DELAY);
        return true;
    }

    public boolean openConnection(){
        if(onlineMode) {
            try {
                connection.connect();
                return true;
            } catch (SmackException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (XMPPException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean login(){
        if(onlineMode && isSetUsernameAndPassword) {
            try {
                if(newConnection() && openConnection()){
                    connection.login();
                    return true;
                }

            } catch (XMPPException e) {
                e.printStackTrace();
                return false;
            } catch (SmackException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public void logout(){
        if(connection.isConnected() && onlineMode){
            connection.disconnect();
            onlineMode = false;
        }
    }

    public boolean isConnected(){
        return connection.isConnected();
    }

    /*
    public Chat chatWith(String username){
        Chat chat = null;
        if(connection.isConnected()) {
            chat = ChatManager.getInstanceFor(connection).createChat(username);
            if(getChatInstance(username)==null){
                //activateChatList.add(chat);
            }
        }
        return chat;
    }

    public Chat getChatInstance(String participant){
        Chat chat = null;
        int index = 0;
        for(Chat chatInstance : activateChatList){
            if(chatInstance.getParticipant().equals(participant)){
                chat = activateChatList.get(index);
                break;
            }
            index++;
        }
        return chat;
    }

    public void sendMessage( Chat chat , String message){
        if(message.length() < maxMessageLength){
            try {
                chat.sendMessage(message);
                //chat.getParticipant();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }
    }

    public Collection<RosterEntry> getRosterEntries(){
        Roster roster = Roster.getInstanceFor(connection);
        Collection<RosterEntry> entries = roster.getEntries();

        return entries;
    }

    public Presence getPresence(String user){
        Roster roster = Roster.getInstanceFor(connection);
        return roster.getPresence(user);
    }*/



    public static XmppChatManager getInstance() {
        return ourInstance;
    }

    public XMPPTCPConnectionConfiguration.Builder getConfigBuilder() {
        return configBuilder;
    }

    public void setConfigBuilder(XMPPTCPConnectionConfiguration.Builder configBuilder) {
        this.configBuilder = configBuilder;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getNonSecurePort() {
        return nonSecurePort;
    }

    public void setNonSecurePort(int nonSecurePort) {
        this.nonSecurePort = nonSecurePort;
    }

    public int getSecurePort() {
        return securePort;
    }

    public void setSecurePort(int securePort) {
        this.securePort = securePort;
    }

    public int getMaxMessageLength() {
        return maxMessageLength;
    }

    public void setMaxMessageLength(int maxMessageLength) {
        this.maxMessageLength = maxMessageLength;
    }

    public boolean isSetUsernameAndPassword() {
        return isSetUsernameAndPassword;
    }

    public void setIsSetUsernameAndPassword(boolean isSetUsernameAndPassword) {
        this.isSetUsernameAndPassword = isSetUsernameAndPassword;
    }

    public boolean isOnlineMode() {
        return onlineMode;
    }

    public void setOnlineMode(boolean onlineMode) {
        this.onlineMode = onlineMode;
    }

    public AbstractXMPPConnection getConnection() {
        return connection;
    }

    public void setConnection(AbstractXMPPConnection connection) {
        this.connection = connection;
    }
}
