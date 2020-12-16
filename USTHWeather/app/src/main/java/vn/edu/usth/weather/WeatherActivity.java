package vn.edu.usth.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

public class WeatherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Log.i("Weather","onCreate");
        WeatherFragment firstFragment = new WeatherFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.weather_frag, firstFragment).commit();
        ForecastFragment secondFragment = new ForecastFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.forecast_frag,secondFragment).commit();
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i("Weather", "onStart");
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

    
}
