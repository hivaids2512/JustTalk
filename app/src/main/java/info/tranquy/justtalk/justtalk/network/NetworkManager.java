package info.tranquy.justtalk.justtalk.network;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by quyquy on 1/28/2016.
 */
public class NetworkManager {
    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
