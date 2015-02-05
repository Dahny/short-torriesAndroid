package ttstudios.com.short_torries;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Gerard-PC on 4-2-2015.
 */
public class MenuActivity extends Activity {

    ListView menu;
    ArrayList<String> menuItems = new ArrayList<>();
    long logoutResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        initialize();
    }

    public void initialize() {
        menu = (ListView) findViewById(R.id.menuList);
        populateMenu();
    }

    public void populateMenu() {
        menuItems.add("New room");
        menuItems.add("Find room");
        menuItems.add("Show rooms");
        menuItems.add("My stories");
        menuItems.add("Top stories");
        menuItems.add("Log out");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, menuItems);
        menu.setAdapter(arrayAdapter);
        //TO-DO: Go to activity on click
        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                switch( position ) {
                    case 4:
                        //Intent newActivity = new Intent(getApplicationContext(), LoginActivity.class);
                        //startActivity(newActivity);
                        break;
                    case 5:
                        logout();
                        break;
                }
            }
        });
    }

    private void logout() {
        Logout logout = new Logout(this);
        logout.execute();
    }

    public void logoutResponse(Long logoutResponse) {
        int responseCode = Integer.valueOf(logoutResponse.intValue());
        switch (responseCode) {
            case -2:
                Toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_LONG).show();
                break;
            case 0:
                Intent menuActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(menuActivity);
                finish();
                break;
            case 401:
                Toast.makeText(getApplicationContext(), "Unauthorized", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(getApplicationContext(), "An error occurred: Could not retrieve data", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private class Logout extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        public Logout(MenuActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Logging out, please wait");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            logoutResponse(logoutResponse);
        }

        @Override
        protected Void doInBackground(Void... params) {
            logoutResponse = Authentication.logout(getApplicationContext());
            return null;
        }

    }

}
