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
public class LogoutHandler extends Authentication {


    static String logoutResponse;
    static long statusCode;
    static long response;

    public void executeLogout(Context context) {
        Logout logout = new Logout(context);
        logout.execute();
    }

    public class Logout extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;
        private Context context;

        public Logout(Context context) {
            this.context = context;
            dialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Logging out, please wait");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            response(response, context);
        }

        @Override
        protected Void doInBackground(Void... params) {
            response = logout(context);
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
                Intent menuActivity = new Intent(context, LoginActivity.class);
                context.startActivity(menuActivity);
                ((Activity)context).finish();
                break;
            case 401:
                Toast.makeText(context, "Unauthorized", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(context, "An error occurred: Could not retrieve data, error code: " + responseCode, Toast.LENGTH_LONG).show();
                break;
        }
    }

    public long logout(Context context) {
        if (isConnected(context)) {
            try {
                SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
                String token = pref.getString(TOKEN, null);
                if(token == null) {
                    return 0;
                }
                String url = "http://192.168.1.101:5200/v1/logout";

                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Authorization", "Bearer " + token);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                logoutResponse = response.toString();

                JSONParser parser = new JSONParser();
                JSONObject jsonObj = (JSONObject) parser.parse(logoutResponse);
                if (jsonObj.containsKey("statusCode")) {
                    statusCode = (long) jsonObj.get("statusCode");
                    if (statusCode == 0) {
                        SharedPreferences.Editor editor = pref.edit();
                        editor.remove(TOKEN);
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
