package vn.edu.usth.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
                return true;
            case R.id.settings:
                Intent intent = new Intent(this, PrefActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
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
}
