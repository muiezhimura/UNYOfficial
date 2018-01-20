package com.infinite.rzzkan.uny;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.app.Fragment;

import com.infinite.rzzkan.uny.Fragment.BeritaFragment;
import com.infinite.rzzkan.uny.Fragment.HomeFragment;
import com.infinite.rzzkan.uny.Fragment.InfoFragment;
import com.infinite.rzzkan.uny.Fragment.PengumumanFragment;
import com.infinite.rzzkan.uny.Fragment.VideoFragment;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    HomeFragment homeFragment = new HomeFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, homeFragment);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_berita:
                    BeritaFragment beritaFragment = new BeritaFragment();
                    FragmentTransaction fragmentBeritaTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentBeritaTransaction.replace(R.id.content, beritaFragment);
                    fragmentBeritaTransaction.commit();
                    return true;
                case R.id.navigation_pengumuman:
                    PengumumanFragment pengumumanFragment = new PengumumanFragment();
                    FragmentTransaction fragmentPengumumanTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentPengumumanTransaction.replace(R.id.content, pengumumanFragment);
                    fragmentPengumumanTransaction.commit();
                    return true;

                case R.id.navigation_video:
                    VideoFragment videoFragment = new VideoFragment();
                    FragmentTransaction fragmentVideoTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentVideoTransaction.replace(R.id.content, videoFragment);
                    fragmentVideoTransaction.commit();
                    return true;

                case R.id.navigation_info:
                    InfoFragment infoFragment = new InfoFragment();
                    FragmentTransaction fragmentInfoTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentInfoTransaction.replace(R.id.content, infoFragment);
                    fragmentInfoTransaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, homeFragment);
        fragmentTransaction.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
