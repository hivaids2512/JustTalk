package info.tranquy.justtalk.justtalk.chat;

import android.util.Log;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

/**
 * Created by quyquy on 2/5/2016.
 */

public class XMPPConnectionListener implements ConnectionListener {

    @Override
    public void connected(XMPPConnection connection) {
        Log.e("connect", "Connect");
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {

    }

    @Override
    public void connectionClosed() {
        Log.e("connect", "Close Connect");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.e("connect", e.toString());
    }

    @Override
    public void reconnectionSuccessful() {
        Log.e("connect", "Reconnect ok");
    }

    @Override
    public void reconnectingIn(int seconds) {
        Log.e("connect", "Reconnect in" + seconds);
    }

    @Override
    public void reconnectionFailed(Exception e) {
        Log.e("connect", "Reconnect fails");
    }
}
