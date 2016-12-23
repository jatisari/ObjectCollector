package net.agusharyanto.objectcollector;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public File path = Environment.getExternalStorageDirectory();
    private String shortfilename = "";
    private String filename = "";
    private String photoPath = "", action="";
    private ImageView imageViewFoto;
    private HashMap<String, Photo> hashphotopath = new HashMap<String, Photo>();
    private Context context = MainActivity.this;

    private final String TAG = getClass().getSimpleName();

    private LocationManager locationManager;
    private Location loc;
    private String latitude="0";
    private String longitude="0";

    private EditText editTextName, editTextTotalRoom, editTextAddress;
    Dialog loginDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar snackbar = Snackbar
                        .make(view, "Message is deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar snackbar1 = Snackbar.make(view, "Message is restored!", Snackbar.LENGTH_SHORT);
                                snackbar1.show();
                            }
                        });

                snackbar.show();*/

                //Intent intent = new Intent(MainActivity.this, ListHotelActivity.class);
                //startActivity(intent);

                createDialog();

            }
        });

        imageViewFoto = (ImageView) findViewById(R.id.imageViewFoto);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextTotalRoom = (EditText) findViewById(R.id.editTextTotalRoom);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        getcurrentLocation();
    }

    private void createDialog(){
        loginDialog = new Dialog(MainActivity.this);
        loginDialog.setTitle("Login");
        loginDialog.setContentView(R.layout.login_dialog);
        loginDialog.setCancelable(true);
        final EditText edtusername = (EditText) loginDialog.findViewById(R.id.editTextUserLogin);
        final EditText edtpassword = (EditText) loginDialog.findViewById(R.id.editTextPasswordLogin);
        //float x = rating;
        // ratingBar.setRating(ratingbar.getRating());

        //TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
        //text.setText(resto_name);

        Button updateButton = (Button) loginDialog.findViewById(R.id.buttonLogin);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    String	url=AppConfig.URL_SERVER+"/login.php";

                    String username = URLEncoder.encode(edtusername.getText().toString(), "utf-8");
                    String password = URLEncoder.encode(edtpassword.getText().toString(), "utf-8");

                    url += "?password=" + password + "&username=" + username;

                    Log.d("url**","url:"+url);
                   // new RequestTask(MainActivity.this).execute(url);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //  loginDialog.dismiss();

            }
        });
        Button cancelButton = (Button) loginDialog.findViewById(R.id.buttonCancelLogin);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                loginDialog.dismiss();

            }
        });
        //now that the dialog is set up, it's time to show it
        loginDialog.show();
    }


    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
        // this.TAX=value;
    }

    private void SavePreferences(String key, int value) {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
        // this.TAX=value;
    }

    private void LoadPreferences() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

      //  this.loginstatus = sharedPreferences.getInt("LOGINSTATUS", 0);
        //this.BACKGROUND_IMG = sharedPreferences.getString("LIST_BACKGROUND_IMG",

        //this.FONT_SIZE = sharedPreferences.getInt("FONT_SIZE", 16);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_camera) {
           takePicture();
        }
        else if(id == R.id.action_save){
            sendImage2();
        }

        return super.onOptionsItemSelected(item);

    }


    public void takePicture() {
        //Intent intent = null;



        // File path = Environment.getExternalStorageDirectory();
        File sddir = new File(path + "/objcol");

        if (!sddir.mkdirs()) {
            sddir.mkdirs();

        }
//BraInmaTics20i6
        shortfilename = "img_bp_"
                + System.currentTimeMillis() + ".jpg";
        filename = path.getAbsolutePath() + "/objcol/" + shortfilename;

        Uri uri = Uri.fromFile(new File(filename));
        Intent photoIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        photoIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(photoIntent, 1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {

            Log.d("tag", "masukyo 1");
            photoPath = filename;

            if (photoPath.equals("") || photoPath.equals(null))
                return;
            Photo foto = new Photo(hashphotopath.size() + "", shortfilename,
                    photoPath);
            hashphotopath.put("" + hashphotopath.size(), foto);

            imageViewFoto.setVisibility(View.VISIBLE);

            // Bitmap bitmap = ShrinkBitmap(photoPath, 1024, 68);
            BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inPreferredConfig = Bitmap.Config.RGB_565;
            //	options.inSampleSize=4;

            Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);

            File sfile = new File(photoPath);
            long sof = sfile.length() / 1024;
            try {
                FileOutputStream out = new FileOutputStream(photoPath);
                if (sof > 2000) {
                    Log.d("tag","masukyo 2000");
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 10, out);
                } else if (sof > 1000) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
                } else if (sof > 500) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 30, out);
                } else {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d("tag","masukyo 10");
            int height = bitmap.getHeight(), width = bitmap.getWidth();

            if (height > 1280 && width > 960){
                options.inSampleSize=4;
                Bitmap imgbitmap = BitmapFactory.decodeFile(photoPath, options);
                try {
                    FileOutputStream out = new FileOutputStream(photoPath);
                    if (sof > 2000) {
                        Log.d("tag","masukyo 20000");
                        imgbitmap.compress(Bitmap.CompressFormat.JPEG, 10, out);
                    } else if (sof > 1000) {
                        imgbitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
                    } else if (sof > 500) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, out);
                    } else {
                        imgbitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                imageViewFoto.setImageBitmap(imgbitmap);

                System.out.println("Need to resize");

            }else {
                //  imageView.setImageBitmap(bitmap);
                System.out.println("WORKS");
                imageViewFoto.setImageBitmap(bitmap);
            }



        }}

    public void sendImage2() {
        try {
            if (isNetworkAvailable()) {
                Log.d("*******", "fotopath:" + photoPath);
                if (!photoPath.equals("")) {

                    UploadImageAsyncInvokeURLTask task;

                    task = new UploadImageAsyncInvokeURLTask(context,
                            new UploadImageAsyncInvokeURLTask.OnPostExecuteListener() {

                                @Override
                                public void onPostExecute(String result) {
                                    // TODO Auto-generated method stub
                                    Log.d("tag","masuk:"+result);
                                    if (result.contains("uploaded")){
                                       saveDataServer();
                                    }else{
                                        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    //	task.mNoteItWebUrl = "/api/saveorder.php";
                    //task.applicationContext =getActivity();
                    task.hashphotopath= this.hashphotopath;
                    task.execute();

                } else {
                   saveDataServer();

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;

    }

    private void saveDataServer(){

        try {



            String name = editTextName.getText().toString();
            String total_room = editTextTotalRoom.getText().toString();
            String address = editTextAddress.getText().toString();

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
            nameValuePairs.add(new BasicNameValuePair("name", name));
            nameValuePairs.add(new BasicNameValuePair("total_room", total_room));
            nameValuePairs.add(new BasicNameValuePair("address", address));
            nameValuePairs.add(new BasicNameValuePair("foto", shortfilename));
            nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
            nameValuePairs.add(new BasicNameValuePair("longitude", longitude));

                nameValuePairs.add(new BasicNameValuePair("id", "0"));

            AsyncInvokeURLTask task = new AsyncInvokeURLTask(nameValuePairs,
                    new AsyncInvokeURLTask.OnPostExecuteListener() {

                        @Override
                        public void onPostExecute(String result) {
                            // TODO Auto-generated method stub
                            Log.d("TAG", "savedata:" + result);
                            if (result.equals("timeout") || result.trim().equalsIgnoreCase("Tidak dapat Terkoneksi ke Data Base")) {

                            } else {
                                processResponse(result);
                            }
                        }
                    });
            task.showdialog = true;
            task.message = "Proses Submit Data Harap Tunggu..";
            task.applicationContext = context;
            task.mNoteItWebUrl = "/savedata.php";
            task.execute();


        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void processResponse(String response){

        try {
            JSONObject jsonObj = new JSONObject(response);
            String errormsg = jsonObj.getString("errormsg");
            Toast.makeText(getBaseContext(),"Save Data "+errormsg,Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            Log.d("ViewActivity", "errorJSON");
        }

    }

    private void getcurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        String provider = locationManager
                .getBestProvider(new Criteria(), false);
        //provider= locationManager.NETWORK_PROVIDER;
        Location location = locationManager.getLastKnownLocation(provider);

        if (location == null) {
            locationManager.requestLocationUpdates(provider, 0, 0, listener);
        } else {

            loc = location;
            latitude = location.getLatitude()+"";
            longitude = location.getLongitude()+"";
            Log.e(TAG, "locations : " + location.getLatitude()+" "+location.getLongitude());

        }

    }

    private LocationListener listener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "location update : " + location);
            loc = location;
            latitude = location.getLatitude()+"";
            longitude = location.getLongitude()+"";
            Log.e(TAG, "locations update : " + location.getLatitude()+" "+location.getLongitude());
            locationManager.removeUpdates(listener);
        }
    };

}
