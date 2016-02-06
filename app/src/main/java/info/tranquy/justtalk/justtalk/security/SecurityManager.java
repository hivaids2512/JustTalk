package info.tranquy.justtalk.justtalk.security;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by quyquy on 1/29/2016.
 */
public class SecurityManager {

    public SharedPreferences sharedpreferences;

    public SecurityManager(Context context, String preName){
        sharedpreferences = context.getSharedPreferences( preName , Context.MODE_PRIVATE);
    }

    public void saveInfo(String username, String password, String mode){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("Username", username);
        editor.putString("Password", password);
        editor.putString("Mode", mode);
        editor.commit();
    }

    public String getInfo(String infoName){
        return sharedpreferences.getString(infoName, null);
    }

    public void removeInfo(){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear().commit();
    }

}
