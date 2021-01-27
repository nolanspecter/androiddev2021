package vn.edu.usth.weather;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.tabs.TabLayout;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Log.i("Weather","onCreate");
//        WeatherFragment firstFragment = new WeatherFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.weather_frag, firstFragment).commit();
//        ForecastFragment secondFragment = new ForecastFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.forecast_frag,secondFragment).commit();

        PagerAdapter adapter = new HomePagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(pager);

        MediaPlayer player = MediaPlayer.create(WeatherActivity.this, R.raw.song);
        player.start();
    }

    @Override
    protected void onStart(){
        super.onStart();
        copyFileToExternalStorage(R.raw.song,"sample.mp3");
        Log.i("Weather", "onStart");
    }

    private void copyFileToExternalStorage(int resid, String resname){
        try{
            File file = new File(getExternalFilesDir(null),resname);
            InputStream s = getApplicationContext().getResources().openRawResource(resid);
            OutputStream o = new FileOutputStream(file);
            byte[] buff = new byte[1024*2];
            int read = 0;
            try{
                while ((read = s.read(buff)) > 0){
                    o.write(buff,0,read);
                }
            }
            finally {
                Toast toast = Toast.makeText(getApplicationContext(), file.getAbsolutePath(), Toast.LENGTH_LONG);
                toast.show();
                s.close();
                o.close();
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            Toast toast = Toast.makeText(getApplicationContext(), "File not found", Toast.LENGTH_LONG);
            toast.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.i("Weather", "onResume");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i("Weather", "onPause");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i("Weather", "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i("Weather", "onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.refresh:
                refresh();
                getTempdata();
                break;
            case R.id.settings:
                Intent intent = new Intent(this, PrefActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
    public void getTempdata() {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=hanoi&appid=e25f069c83e74bcd83098b28172ab186";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {
                try {
                    JSONObject json = new JSONObject(res);
                    JSONObject main = json.getJSONObject("main");
                    ((TextView) findViewById(R.id.temp)).setText(main.getString("temp"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }
    public void refresh(){
        final Handler handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                String content = msg.getData().getString("Server Respond");
                Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
            }
        };
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }

                // Assume that we got our data from server
                Bundle bundle = new Bundle();
                bundle.putString("Server Respond", "Refreshing");

                Message msg = new Message();
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        });
        t.start();
    }

//    private class refresh_new extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... voids){
//            try {
//                URL url = new URL("https://usth.edu.vn/uploads/logo_moi-eng.png");
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//                connection.setDoInput(true);
//                connection.connect();
//
//                int response = connection.getResponseCode();
//                Log.i("USTHWeather", "The connection is: " + response);
//                InputStream in = connection.getInputStream();
//
//                Bitmap btmp = BitmapFactory.decodeStream(in);
//                ImageView logo = (ImageView) findViewById(R.id.logo);
//                logo.setImageBitmap(btmp);
//
//                connection.disconnect();
//            }
//            catch (IOException e){
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        protected void onProgressUpdate(Void... voids){}
//
//        @Override
//        protected void onPostExecute(Void voids){
//            Toast.makeText(getApplicationContext(), "Refreshing Again...", Toast.LENGTH_SHORT).show();
//            return;
//        }
//    }
}
