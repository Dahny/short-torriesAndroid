package ttstudios.com.short_torries;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Gerard on 5-2-2015.
 */


public class RegisterActivity extends Activity {

    EditText userText;
    EditText passwordText;
    EditText passwordRepeatText;
    Button registerButton;
    String username = "";
    String password = "";
    long registerResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        initialize();
    }

    public void initialize() {
        userText = (EditText) findViewById(R.id.editUser);
        passwordText = (EditText) findViewById(R.id.editPassword);
        passwordRepeatText = (EditText) findViewById(R.id.editPassword2);
        registerButton = (Button) findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                username = userText.getText().toString();
                password = passwordText.getText().toString();
                register();
                if(username.matches("^\\S+\\w{8,32}\\S{1,}") && password.matches("^\\S+\\w{8,32}\\S{1,}")) {

                }
            }
        });
    }

    private void register() {
        RegisterHandler register = new RegisterHandler();
        register.executeRegistration(username, password, this);
    }
}

