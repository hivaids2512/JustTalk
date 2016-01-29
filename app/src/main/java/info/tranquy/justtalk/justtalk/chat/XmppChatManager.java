package info.tranquy.justtalk.justtalk.chat;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import info.tranquy.justtalk.justtalk.configuration.Configuration;

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
    private ArrayList<Chat> activateChatList = null;
    private boolean onlineMode = true;

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getSecurePort() {
        return securePort;
    }

    public void setSecurePort(int securePort) {
        this.securePort = securePort;
    }

    public int getNonSecurePort() {
        return nonSecurePort;
    }

    public void setNonSecurePort(int nonSecurePort) {
        this.nonSecurePort = nonSecurePort;
    }

    public int getMaxMessageLength() {
        return maxMessageLength;
    }

    public void setMaxMessageLength(int maxMessageLength) {
        this.maxMessageLength = maxMessageLength;
    }

    public AbstractXMPPConnection getConnection() {
        return connection;
    }

    public void setConnection(AbstractXMPPConnection connection) {
        this.connection = connection;
    }

    public boolean isSetUsernameAndPassword() {
        return isSetUsernameAndPassword;
    }

    public void setIsSetUsernameAndPassword(boolean isSetUsernameAndPassword) {
        this.isSetUsernameAndPassword = isSetUsernameAndPassword;
    }

    public XMPPTCPConnectionConfiguration.Builder getConfigBuilder() {
        return configBuilder;
    }

    public void setConfigBuilder(XMPPTCPConnectionConfiguration.Builder configBuilder) {
        this.configBuilder = configBuilder;
    }

    public boolean isOnlineMode() {
        return onlineMode;
    }

    public void setOnlineMode(boolean onlineMode) {
        this.onlineMode = onlineMode;
    }

    private XmppChatManager() {
        configBuilder = XMPPTCPConnectionConfiguration.builder();
        configBuilder.setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled);
        configBuilder.setServiceName(serverIP);
        configBuilder.setHost(serverIP);
        configBuilder.setPort(nonSecurePort);
        configBuilder.setDebuggerEnabled(true);
        activateChatList = new ArrayList<Chat>();
    }

    public void setUsernameAndPassword(String username, String password) {
        configBuilder.setUsernameAndPassword(username, password);
        isSetUsernameAndPassword = true;
    }

    public void newConnection(){
        connection = new XMPPTCPConnection(configBuilder.build());
    }

    public void openConnection(){
        try {
            connection.connect();
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection (){
        if(connection.isConnected()){
            connection.disconnect();
        }
    }

    public void login(){

        try {
            newConnection();
            openConnection();
            connection.login();
        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void logout(){
        if(connection.isConnected()){
            closeConnection();
        }
    }

    public Chat chatWith(String username){
        Chat chat = null;
        if(connection.isConnected()) {
            chat = ChatManager.getInstanceFor(connection).createChat(username);
            if(getChatInstance(username)==null){
                activateChatList.add(chat);
            }
        }
        return chat;
    }

    public ArrayList<Chat> getActivateChatList(){
        return this.activateChatList;
    }

    public int getActivateChatListSize(){
        return this.activateChatList.size();
    }

    public boolean getConnectionState(){
        return connection.isConnected();
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
                chat.getParticipant();
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

    public static XmppChatManager getInstance() {
        return ourInstance;
    }

}
