package ttstudios.com.short_torries.authentication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ttstudios.com.short_torries.R;

/**
 * Created by Gerard on 6-2-2015.
 */
public class RegisterHandler extends Authentication {

    static String registerResponse;
    static long statusCode;
    static long response;
    private String username;
    private String password;

    public void executeRegistration(String username, String password, Context context) {
        Register register = new Register(username, password, context);
        register.execute();
    }

    public class Register extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;
        private Context context;

        public Register(String usr, String pass, Context context) {
            this.context = context;
            dialog = new ProgressDialog(context);
            username = usr;
            password = pass;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Registration in progress, please wait");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            response(response, context);
        }

        @Override
        protected Void doInBackground(Void... params) {
            response = register(context);
            return null;
        }

    }

    @Override
    public void response(Long logoutResponse, Context context) {
        int responseCode = Integer.valueOf(logoutResponse.intValue());
        switch (responseCode) {
            case -2:
                Toast.makeText(context, "No connection", Toast.LENGTH_LONG).show();
                break;
            case 0:
                LoginHandler loginHandler = new LoginHandler();
                loginHandler.executeLogin(username, password, context);
                ((Activity)context).finish();
                break;
            case 5:
                Toast.makeText(context, "Database error", Toast.LENGTH_LONG).show();
                break;
            case 6:
                Toast.makeText(context, "Username already exists", Toast.LENGTH_LONG).show();
                break;
            case 401:
                Toast.makeText(context, "Unauthorized", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(context, "An error occurred: Could not retrieve data, error code: " + responseCode, Toast.LENGTH_LONG).show();
                break;
        }
    }

    public long register(Context context) {
        if (isConnected(context)) {
            try {
                String url =  context.getString(R.string.url)  + "/v1/newUser?user=" + username + "&pass=" + password;

                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                con.setRequestMethod("PUT");
                con.setRequestProperty("User-Agent", USER_AGENT);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                registerResponse = response.toString();

                JSONParser parser = new JSONParser();
                JSONObject jsonObj = (JSONObject) parser.parse(registerResponse);
                if (jsonObj.containsKey("statusCode")) {
                    statusCode = (long) jsonObj.get("statusCode");
                } else {
                    //No or invalid response received
                    statusCode = -1;
                }
            } catch (Exception e) {
                statusCode = -1;
                System.out.println("Error: " + e);
            }
        }
        else {
            //No connection
            statusCode = -2;
        }
        return statusCode;
    }
}
