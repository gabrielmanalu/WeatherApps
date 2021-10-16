package com.example.weatherapps.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.weatherapps.Fragment.CityFragment;
import com.example.weatherapps.Fragment.ForecastFragment;
import com.example.weatherapps.Fragment.WeatherToday;

public class ViewPagerAdapter extends FragmentStateAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0) {
            return WeatherToday.getInstance();
        } else if(position == 1) {
            return ForecastFragment.getInstance();
        } else {
            return CityFragment.getInstance();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
