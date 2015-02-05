package ttstudios.com.short_torries;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Gerard-PC on 4-2-2015.
 */
public class MenuActivity extends Activity {

    ListView menu;
    ArrayList<String> menuItems = new ArrayList<>();

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
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, menuItems);
        menu.setAdapter(arrayAdapter);
        /* TO-DO: Go to activity on click
        menu.setOnClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch( position )
                {
                    case 0:  Intent newActivity = new Intent(this, a.class);
                        startActivity(newActivity);
                        break;
                    case 1:  Intent newActivity = new Intent(this, a.class);
                        startActivity(newActivity);
                        break;
                    case 2:  Intent newActivity = new Intent(this, a.class);
                        startActivity(newActivity);
                        break;
                    case 3:  Intent newActivity = new Intent(this, a.class);
                        startActivity(newActivity);
                        break;
                    case 4:  Intent newActivity = new Intent(this, a.class);
                        startActivity(newActivity);
                        break;
                }
            }
        });
        */
    }
}
