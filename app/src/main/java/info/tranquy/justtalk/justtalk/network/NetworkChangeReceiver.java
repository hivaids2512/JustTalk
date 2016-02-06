package info.tranquy.justtalk.justtalk.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.jivesoftware.smack.chat.ChatManager;

import info.tranquy.justtalk.justtalk.chat.XmppChatManager;

/**
 * Created by quyquy on 1/31/2016.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Bundle extras = intent.getExtras();
        Intent i = new Intent("broadcastMode");
        XmppChatManager chatManager = XmppChatManager.getInstance();

        if(checkInternet(context))
        {
            chatManager.setOnlineMode(true);
            Log.e("online", String.valueOf(chatManager.isOnlineMode()));
            i.putExtra("mode", "online");
            context.sendBroadcast(i);
        }else{
            chatManager.setOnlineMode(false);
            Log.e("online", String.valueOf(chatManager.isOnlineMode()));
            i.putExtra("mode", "offline");
            context.sendBroadcast(i);
        }
    }

    private boolean checkInternet(Context context) {
        NetworkManager networkManager = new NetworkManager();
        if(networkManager.isNetworkAvailable(context)){
            return true;
        }
        return false;
    }

}
