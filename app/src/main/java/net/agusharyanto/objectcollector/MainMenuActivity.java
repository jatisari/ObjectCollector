package net.agusharyanto.objectcollector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MainMenuActivity extends AppCompatActivity {
    private ListView listView1;
    private ArrayList<HashMap<String, String>> menulist = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        initData();
    }

    public void initData(){
        menulist.add(genHashMap("Add Object", "Add Object"));
        menulist.add(genHashMap("List Object", "List Object"));
        menulist.add(genHashMap("Map Object", "Map Object"));
       // menulist.add(genHashMap("","http://91.121.25.89:8030"));
        //menulist.add(genHashMap("Top 104","http://65.254.33.10:6600"));
        setListData();
    }

    public HashMap<String,String> genHashMap(String name, String url){
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("name", name);
        map.put("url", url);
        return map;
    }

    public void setListData(){
        listView1 = (ListView) findViewById(R.id.listViewMenu);

        ListAdapter adapter = new SimpleAdapter(this, menulist, R.layout.row_menu,
                new String[] { "name" }, new int[] {
                R.id.textViewMenu });
        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //showToast("kesini", 0);
                HashMap<String, String> menu = (HashMap<String, String>) listView1.getItemAtPosition(position);
                //	Toast.makeText(getBaseContext(), "ID '" + o.get("id") + "' was clicked.", Toast.LENGTH_SHORT).show();
                //tvRadioUrl.setText(radio.get("name") + " - " + radio.get("url"));
                //startPlaying(radio.get("url"));
                callIntent(menu.get("name"));
            }
        });

    }

    private void callIntent(String argmenu){
        Intent intent =null;
        if (argmenu.equals("Add Object")){
            intent = new Intent(MainMenuActivity.this, MainActivity.class);

        }else if (argmenu.equals("List Object")){
            intent = new Intent(MainMenuActivity.this, ListHotelActivity.class);

        }else if (argmenu.equals("Map Object")){
            intent = new Intent(MainMenuActivity.this, MapsActivity.class);

        }
        startActivity(intent);


    }
}
