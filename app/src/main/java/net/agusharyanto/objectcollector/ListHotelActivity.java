package net.agusharyanto.objectcollector;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListHotelActivity extends AppCompatActivity {
    private ListView listViewHotel;
    private Button buttonTambah;
    private HotelArrayAdapter adapter;
    private ArrayList<Hotel> hotelList = new ArrayList<Hotel>();

    private Hotel hotel;
    private int selectedPosition=0;
    private ProgressDialog pDialog;

    private static  final int REQUEST_CODE_ADD =1;
    private static  final int REQUEST_CODE_EDIT =2;
    private Context context = ListHotelActivity.this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hotel);
        hotel = new Hotel();
        pDialog = new ProgressDialog(context);

        listViewHotel = (ListView) findViewById(R.id.listViewHotel);
        loadDataServerVolley();

        /*buttonTambah = (Button) findViewById(R.id.buttonAdd);


        loadDataServerVolley();
        buttonTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTambahData();
            }
        });*/

    }


    private void loadDataServerVolley(){

        String url = AppConfig.URL_SERVER+"listdata.php";
        pDialog.setMessage("Retieve Data Hotel...");
        showDialog();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        processResponse(response);
                        gambarDatakeListView();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                // params.put("id","1");
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

    private void processResponse(String response){

        try {
            JSONObject jsonObj = new JSONObject(response);
            JSONArray jsonArray = jsonObj.getJSONArray("data");
            Log.d("TAG", "data length: " + jsonArray.length());
            Hotel objecthotel = null;
            hotelList.clear();
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                objecthotel= new Hotel();
                objecthotel.setId(obj.getString("id"));

                objecthotel.setName(obj.getString("name"));
                objecthotel.setAddress(obj.getString("address"));
                objecthotel.setTotal_room(obj.getString("total_room"));
                objecthotel.setFoto(obj.getString("foto"));
                objecthotel.setLatitude(obj.getString("latitude"));
                objecthotel.setLongitude(obj.getString("longitude"));


                this.hotelList.add(objecthotel);
            }

        } catch (JSONException e) {
            Log.d("MainActivity", "errorJSON");
        }

    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }



    private void gambarDatakeListView() {
        adapter = new HotelArrayAdapter(this,
                R.layout.row_hotel, hotelList);
        listViewHotel.setAdapter(adapter);
       /* listViewHotel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hotel = (Hotel) parent.getAdapter().getItem(position);
                selectedPosition = position;
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra("hotel", hotel);
                startActivityForResult(intent, REQUEST_CODE_EDIT);
            }
        });*/
     /*   listViewHotel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hotel = (Hotel) parent.getAdapter().getItem(position);
                selectedPosition = position;
                Intent intent = new Intent(MainActivity.this, AddEditServerActivity.class);
                intent.putExtra("hotel", hotel);
                startActivityForResult(intent, REQUEST_EDIT);
            }
        });*/


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_ADD: {
                if (resultCode == RESULT_OK && null != data) {
                    if (data.getStringExtra("refreshflag").equals("1")) {
                        //hotelList = databaseHelper.getDataHotel(db);
                        //gambarDatakeListView();
                        loadDataServerVolley();
                    }
                }
                break;
            }
            case REQUEST_CODE_EDIT: {
                if (resultCode == RESULT_OK && null != data) {
                    if (data.getStringExtra("refreshflag").equals("1")) {
                        //   hotelList = databaseHelper.getDataHotel(db);
                        //  gambarDatakeListView();
                        loadDataServerVolley();
                    }
                }
                break;
            }

        }
    }


}
