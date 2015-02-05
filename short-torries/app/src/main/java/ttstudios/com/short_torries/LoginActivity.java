package ttstudios.com.short_torries;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


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
                Intent menuActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(menuActivity);
            }
        });
    }

    private void login() {
        LoginHandler loginHandler = new LoginHandler();
        loginHandler.executeLogin(username, password, this);
    }
}
