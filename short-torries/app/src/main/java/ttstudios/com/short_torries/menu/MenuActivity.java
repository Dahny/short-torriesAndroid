package ttstudios.com.short_torries.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ttstudios.com.short_torries.R;
import ttstudios.com.short_torries.authentication.LogoutHandler;

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
        LogoutHandler logout = new LogoutHandler();
        logout.executeLogout(this);
    }
}
