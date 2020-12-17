package vn.edu.usth.weather;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class HomePagerAdapter extends FragmentPagerAdapter {
    private final int PAGE_COUNT = 3;
    private String titles[] = new String[] {"Hanoi, Vietnam", "Paris, France", "Toulouse, France"};
    public HomePagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public int getCount(){
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int page){
        switch (page){
            case 0: return WeatherAndForecastFragment.newInstance();
            case 1: return WeatherAndForecastFragment.newInstance();
            case 2: return WeatherAndForecastFragment.newInstance();
            default: return WeatherAndForecastFragment.newInstance();
        }
    }

    @Override
    public CharSequence getPageTitle(int page){
        return titles[page];
    }
}

