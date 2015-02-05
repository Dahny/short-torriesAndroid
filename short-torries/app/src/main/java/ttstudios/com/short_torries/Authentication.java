package ttstudios.com.short_torries;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;

/**
 * Created by Gerard-PC on 5-2-2015.
 */
public class Authentication {

    static String loginResponse;
    static long statusCode;
    private final static String USER_AGENT = "Mozilla/5.0";
    boolean loggedIn;

    // Shared pref mode
    private static final int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Authentication";

    private static final String TOKEN = "token";

    public static long login(String username, String password, Context context) {
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
                        Editor editor = pref.edit();
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
        System.out.println(statusCode);
        return statusCode;
    }

    /**
     * Check if authentication passes
     * @return Returns true if the user is authenticated
     */
    public static boolean isAuthenticated(Context context) {
        try {
            //Read token
            SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            Editor editor = pref.edit();

            String token = pref.getString(TOKEN, null);
            if(token == null) {
                return false;
            }

            String url = "http://192.168.1.101:5200/v1/authenticate?auth=" + token;

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
            Object jsonObj  = parser.parse(loginResponse);
            JSONArray array = new JSONArray();
            array.add(jsonObj);
            System.out.println(array.get(0));
        } catch(Exception e) {
            loginResponse = "error " + e.toString();
            return false;
        }
        return false;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    return isConnected;
    }



}
