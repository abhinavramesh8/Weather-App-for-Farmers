package com.a5corp.weather.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.a5corp.weather.R;

import com.a5corp.weather.fragment.WeatherFragment;
import com.a5corp.weather.fragment.CropsFragment;
import com.a5corp.weather.model.WeatherFort;
import com.a5corp.weather.preferences.Prefs;
import com.a5corp.weather.utils.Constants;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.weather_icons_typeface_library.WeatherIcons;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import shortbread.Shortbread;
import shortbread.Shortcut;

public class WeatherActivity extends AppCompatActivity {
    Prefs preferences;
    WeatherFragment wf;

    @BindView(R.id.toolbar) Toolbar toolbar;
    Drawer drawer;
    NotificationManagerCompat mManager;
    Handler handler;

    @Shortcut(id = "home", icon = R.drawable.shortcut_home , shortLabel = "Weather Info", rank = 1)
    public void addWeather() {

    }


    @Shortcut(id = "crops", icon = R.drawable.crop , shortLabel = "crops",rank = 2)
    public void addCrops() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Activity" , WeatherActivity.class.getSimpleName());
        mManager = NotificationManagerCompat.from(this);
        preferences = new Prefs(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        handler = new Handler();

        wf = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("mode" , intent.getIntExtra(Constants.MODE , 0));
        wf.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, wf)
                .commit();
        initDrawer();
    }

    public void createShortcuts() {
        Shortbread.create(this);
    }

    public void initDrawer() {
        final Context context = this;
        final IProfile profile = new ProfileDrawerItem()
                .withIcon(R.drawable.crop);
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTextColor(ContextCompat.getColor(this , R.color.md_amber_400))
                .addProfiles(
                        profile
                )
                .withSelectionListEnabled(false)
                .withProfileImagesClickable(false)
                .build();
        SecondaryDrawerItem item1 = new SecondaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_home)
                .withIcon(new IconicsDrawable(this)
                        .icon(WeatherIcons.Icon.wic_day_sunny));


        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("Crops")
                .withIcon(new IconicsDrawable(this)
                        .icon(GoogleMaterial.Icon.gmd_crop));



        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withSelectedItem(1)
                .withTranslucentStatusBar(true)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                            if (drawerItem != null) {
                                switch((int) drawerItem.getIdentifier()) {
                                    case 1:
                                        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment);
                                        if (!(f instanceof WeatherFragment)) {
                                            wf = new WeatherFragment();
                                            getSupportFragmentManager().beginTransaction()
                                                    .replace(R.id.fragment, wf)
                                                    .commit();
                                        }
                                        break;

                                    case 2:
                                        f = getSupportFragmentManager().findFragmentById(R.id.fragment);
                                        if(!(f instanceof CropsFragment)){
                                            CropsFragment cropsFragment = new CropsFragment();
                                            getSupportFragmentManager().beginTransaction()
                                                    .replace(R.id.fragment,cropsFragment)
                                                    .commit();
                                        }
                                        break;

                                }
                            }
                        return false;
                    }
                })
                .build();
    }



    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        }
        else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private static final String DESCRIBABLE_KEY = "describable_key";


}
