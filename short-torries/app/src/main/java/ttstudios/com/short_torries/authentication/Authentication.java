package ttstudios.com.short_torries.authentication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * Created by Gerard on 5-2-2015.
 */
public abstract class Authentication {

    public final static String USER_AGENT = "Mozilla/5.0";

    // Sharedpref mode
    public static final int PRIVATE_MODE = 0;

    // Sharedpref file name
    public static final String PREF_NAME = "Authentication";
    public static final String TOKEN = "token";

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public abstract void response(Long response, Context context);
}
