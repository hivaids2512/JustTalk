package info.tranquy.justtalk.justtalk.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jivesoftware.smack.chat.Chat;

import java.util.ArrayList;
import java.util.List;

import info.tranquy.justtalk.R;
import info.tranquy.justtalk.justtalk.adapter.MessageListAdapter;
import info.tranquy.justtalk.justtalk.chat.XmppChatManager;
import info.tranquy.justtalk.justtalk.configuration.Configuration;
import info.tranquy.justtalk.justtalk.model.MessListItem;
import info.tranquy.justtalk.justtalk.security.*;
import info.tranquy.justtalk.justtalk.security.SecurityManager;


public class LoginActivity extends AppCompatActivity {

    private EditText input_username;
    private EditText input_password;
    private Button btn_login;
    private ProgressDialog progress;

    private boolean autoLogin = Configuration.autoLogin;
    private String username;
    private String password;
    private SecurityManager securityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        securityManager = new SecurityManager(getApplicationContext(),getResources().getString(R.string.securityLogin));
        //securityManager.removeInfo();
        String mode = securityManager.getInfo("Mode");
        if(mode != null){
            username = securityManager.getInfo("Username");
            password = securityManager.getInfo("Password");
            new Connection().execute();
        }

        setContentView(R.layout.activity_login);
        input_username = (EditText) findViewById(R.id.input_username);
        input_password = (EditText) findViewById(R.id.input_password);
        btn_login = (Button) findViewById(R.id.btn_login);



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    username = input_username.getText().toString();
                    password = input_password.getText().toString();
                    new Connection().execute();
                }
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String email = input_username.getText().toString();
        String password = input_password.getText().toString();

        if (email.isEmpty()) {
            input_username.setError("Username cannot be empty");
            valid = false;
        } else {
            input_username.setError(null);
        }

        if (password.isEmpty() || password.length() < 2 || password.length() > 20) {
            input_password.setError("between 4 and 20 alphanumeric characters");
            valid = false;
        } else {
            input_password.setError(null);
        }

        return valid;
    }

    public class Connection extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(LoginActivity.this, null,
                    "Authenticating...", true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean isAuthenticated = false;
            XmppChatManager chatManager = XmppChatManager.getInstance();
            chatManager.setUsernameAndPassword(username, password);
            if(chatManager.login()){
                isAuthenticated = true;
                if(autoLogin){
                    securityManager = new SecurityManager(getApplicationContext(), getResources().getString(R.string.securityLogin));
                    securityManager.saveInfo(username, password, "autologin");
                }
            }else{
                isAuthenticated = false;
            }
            return isAuthenticated;
        }

        @Override
        protected void onPostExecute(Boolean isAuthenticated) {
            progress.dismiss();
            Log.e("authenticated", String.valueOf(isAuthenticated));
            if(isAuthenticated){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(intent);
            }else{
                Context context = getApplicationContext();
                CharSequence text = "Login error!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.setGravity(Gravity.TOP| Gravity.CENTER, 0, 30);
                toast.show();
            }
        }
    }

}
