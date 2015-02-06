package ttstudios.com.short_torries.authentication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ttstudios.com.short_torries.R;

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
    String passwordRepeat = "";

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
                passwordRepeat = passwordRepeatText.getText().toString();
                if(username.matches("[a-zA-Z0-9]*") && password.matches("[a-zA-Z0-9]{6,}")) {
                    if(password.equals(passwordRepeat)) {
                        register();
                    }
                    else {
                        new AlertDialog.Builder(RegisterActivity.this)
                                .setMessage("Passwords must match")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
                else {
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("Invalid username/password")
                            .setMessage("Your username and/or password should meet the  following requirements:\n" +
                                        "-Username and password must be alphanumeric \n" +
                                        "-Password should be at least 6 characters long")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
    }

    private void register() {
        RegisterHandler register = new RegisterHandler();
        register.executeRegistration(username, password, this);
    }
}

