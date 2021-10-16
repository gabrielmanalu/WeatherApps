package com.example.weatherapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.weatherapps.Adapter.ViewPagerAdapter;
import com.example.weatherapps.Common.Common;
import com.example.weatherapps.Fragment.CityFragment;
import com.example.weatherapps.Fragment.ForecastFragment;
import com.example.weatherapps.Fragment.TodayWeatherFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    ViewPager2 viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;


    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.w("LOG", "HAHAHAHA1");
        

        //Request permission
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            setView();
                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                            showMeTheUserCurrentLocation();
                            Log.w("LOG", "HAHAHAHA2");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Snackbar.make(coordinatorLayout, "Permission Denied", Snackbar.LENGTH_LONG).show();
                    }
                }).check();
    }

    private void setView() {
        Log.w("LOG", "HAHAHAHA");
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), new Lifecycle() {
            @Override
            public void addObserver(@NonNull LifecycleObserver observer) {

            }

            @Override
            public void removeObserver(@NonNull LifecycleObserver observer) {

            }

            @NonNull
            @Override
            public State getCurrentState() {
                return null;
            }
        }));
        Log.w("LOG", "HAHAHAHA");
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setText("TODAY");
                } else if (position == 1) {
                    tab.setText("5 DAYS");
                } else {
                    tab.setText("City");
                }
            }
        }).attach();
    }

    private void showMeTheUserCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        return;
        }
        else{
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if(task != null){
                        Common.current_location = task.getResult();
                        Toast.makeText(MainActivity.this, Common.current_location.getLongitude() + "", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

}