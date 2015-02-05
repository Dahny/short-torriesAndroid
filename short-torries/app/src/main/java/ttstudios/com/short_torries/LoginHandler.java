package ttstudios.com.short_torries;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Gerard on 5-2-2015.
 */
public class LoginHandler extends Authentication{

    static String loginResponse;
    static long statusCode;
    static long response;
    private String username;
    private String password;

    public void executeLogin(String username, String password, Context context) {
        Login login = new Login(context,username,password);
        login.execute();
    }

    public class Login extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;
        public Context context;

        public Login(Context context, String usr, String pass) {
            dialog = new ProgressDialog(context);
            this.context = context;
            username = usr;
            password = pass;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Logging in, please wait");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            response(response, context);
        }

        @Override
        protected Void doInBackground(Void... params) {
            response = login(context);
            return null;
        }
    }

    @Override
    public void response(Long loginResponse, Context context) {
        int responseCode = Integer.valueOf(loginResponse.intValue());
        switch (responseCode) {
            case -2:
                Toast.makeText(context, "No connection", Toast.LENGTH_LONG).show();
                break;
            case 0:
                Intent menuActivity = new Intent(context, MenuActivity.class);
                context.startActivity(menuActivity);
                ((Activity) context).finish();
                break;
            case 4:
                Toast.makeText(context, "Invalid username/password", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(context, "An error occurred: Could not retrieve data, error code: " + responseCode, Toast.LENGTH_LONG).show();
                break;
        }
    }

    public long login(Context context) {
        if (isConnected(context)) {
            try {
                String url = "http://192.168.1.101:5200/v1/login?user=" + username + "&pass=" + password;

                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", USER_AGENT);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                loginResponse = response.toString();

                JSONParser parser = new JSONParser();
                JSONObject jsonObj = (JSONObject) parser.parse(loginResponse);
                if (jsonObj.containsKey("statusCode")) {
                    statusCode = (long) jsonObj.get("statusCode");
                    if (jsonObj.containsKey("token")) {
                        //Save token in SharedPreferences
                        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
                        SharedPreferences.Editor editor = pref.edit();
                        String token = (String) jsonObj.get("token");
                        editor.putString(TOKEN, token);
                        editor.commit();
                    }
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
