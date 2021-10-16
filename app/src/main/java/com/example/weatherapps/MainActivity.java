package com.example.weatherapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.weatherapps.Adapter.ViewPagerAdapter;
import com.example.weatherapps.Common.Common;
import com.example.weatherapps.Fragment.WeatherToday;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    ViewPager2 viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;


    private Toolbar toolbar;
    private LinearLayout mLinearLayout;

    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mLinearLayout = findViewById(R.id.linear_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Snackbar.make(mLinearLayout, "Permission Denied", Snackbar.LENGTH_LONG).show();
                    }
                }).check();


    }


    private void setView() {

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
                        locationNow(Common.current_location.getLatitude(), Common.current_location.getLongitude());
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    private void locationNow(Double a, Double b) {
        if(a == null && b == null){
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putDouble("a", a);
        bundle.putDouble("b", b);
        WeatherToday fragment = new WeatherToday();
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.linear_layout, fragment).commit();

    }

}