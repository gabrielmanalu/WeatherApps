package com.example.weatherapps.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.weatherapps.Fragment.CityFragment;
import com.example.weatherapps.Fragment.ForecastFragment;
import com.example.weatherapps.Fragment.TodayWeatherFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0) {
            return TodayWeatherFragment.getInstance();
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
