package ttstudios.com.short_torries;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoginActivity extends Activity {

    EditText userText;
    EditText passwordText;
    Button loginButton;
    Button registerButton;
    ProgressDialog progress;
    private final String USER_AGENT = "Mozilla/5.0";
    String username = "";
    String password = "";
    long loginResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initialize();
    }

    public void initialize() {
        userText = (EditText) findViewById(R.id.editUser);
        passwordText = (EditText) findViewById(R.id.editPassword);
        loginButton = (Button) findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
               username = userText.getText().toString();
               password = passwordText.getText().toString();
               login();
            }
        });
        registerButton = (Button) findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

            }
        });
    }

    private void login() {
        Login login = new Login(this);
        login.execute();
    }

    public void loginResponse(Long loginResponse) {
        int responseCode = Integer.valueOf(loginResponse.intValue());
        switch (responseCode) {
            case -2:
                Toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_LONG).show();
                break;
            case 0:
                Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(menuActivity);
                finish();
                break;
            case 4:
                Toast.makeText(getApplicationContext(), "Invalid username/password", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(getApplicationContext(), "An error occurred: Could not retrieve data", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private class Login extends AsyncTask <Void, Void, Void> {
        private ProgressDialog dialog;

        public Login(LoginActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Logging in, please wait");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            loginResponse(loginResponse);
        }

        @Override
        protected Void doInBackground(Void... params) {
            loginResponse = Authentication.login(username, password, getApplicationContext());
            return null;
        }

    }

}
