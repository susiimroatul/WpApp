package com.app.wordpress;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.app.wordpress.data.AppConfig;
import com.app.wordpress.data.Constant;
import com.app.wordpress.data.SharedPref;
import com.app.wordpress.fragment.FragmentCategory;
import com.app.wordpress.fragment.FragmentHome;
import com.app.wordpress.fragment.FragmentLater;
import com.app.wordpress.utils.Tools;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class ActivityMain extends AppCompatActivity {

    //for ads
    private InterstitialAd mInterstitialAd;
    String username;
    SharedPreferences sharedpreferences;
    public static final String TAG_USERNAME = "username";
    private Toolbar toolbar;
    private ActionBar actionBar;
    private NavigationView navigationView;
    private FloatingActionButton fab;
    private Fragment fragment = null;
    private TextView user_name, user_email;
    private SharedPref sharedPref;
    private boolean pendingIntroAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = new SharedPref(this);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);

        initToolbar();
        initDrawerMenu();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ActivitySearch.class);
                startActivity(i);
            }
        });
        if (savedInstanceState == null) {
            pendingIntroAnimation = true;
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void initDrawerMenu() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                showInterstitial();
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                displayFragment(item.getItemId(), item.getTitle().toString());
                drawer.closeDrawers();
                return true;
            }
        });

        user_name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_name);
        user_email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_email);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);
        } else {
            doExitApp();
        }
    }

    @Override
    protected void onResume() {
        username = getIntent().getStringExtra(TAG_USERNAME);
        user_name.setText("Hai, "+username);
        user_email.setText("Selamat Berbelanja :)");
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        if (pendingIntroAnimation) {
            pendingIntroAnimation = false;
            startIntroAnimation();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), ActivitySettings.class);
            startActivity(i);
        } else if (id == R.id.action_rate) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(Login.session_status, false);
            editor.putString(TAG_USERNAME, null);
            editor.commit();

            Intent intent = new Intent(ActivityMain.this, Login.class);
            finish();
            startActivity(intent);
        } else if (id == R.id.action_about) {
            Tools.aboutAction(ActivityMain.this);
        }
        else if (id == R.id.sms)
        {
            startActivity(new Intent(this, sms.class));
        }
        else if (id == R.id.maps)
        {
            startActivity(new Intent(this, MapsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void displayFragment(int id, String title) {

        switch (id) {
            case R.id.nav_home:
                if (!actionBar.getTitle().toString().equals(title)) {
                    actionBar.setTitle(title);
                    fragment = new FragmentHome();
                }
                break;
            case R.id.nav_category:
                if (!actionBar.getTitle().toString().equals(title)) {
                    actionBar.setTitle(title);
                    fragment = new FragmentCategory();
                }
                break;
            case R.id.nav_later:
                if (!actionBar.getTitle().toString().equals(title)) {
                    actionBar.setTitle(title);
                    fragment = new FragmentLater();
                }
                break;
         //   case R.id.nav_setting:
          //      Intent i = new Intent(getApplicationContext(), ActivitySettings.class);
           //     startActivity(i);
            //// case R.id.nav_rate:

            //    break;

          //  case R.id.nav_about:
              //  Tools.aboutAction(ActivityMain.this);
             //   break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_content, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }



    /**
     * show ads
     */
    public void showInterstitial() {
        // Show the ad if it's ready
        if (AppConfig.ENABLE_ADSENSE && mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private void startIntroAnimation() {
        fab.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));

        int actionbarSize = Tools.dip2px(this, 56);
        toolbar.setTranslationY(-actionbarSize);
        toolbar.animate().translationY(0).setDuration(Constant.ANIM_DURATION_TOOLBAR)
                .setStartDelay(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startContentAnimation();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        startContentAnimation();
                    }
                });
    }

    private void startContentAnimation() {
        fab.animate().translationY(0).setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(300)
                .setDuration(Constant.ANIM_DURATION_FAB).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // first fragment to display
                displayFragment(R.id.nav_home, getString(R.string.title_nav_home));
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // first fragment to display
                displayFragment(R.id.nav_home, getString(R.string.title_nav_home));
            }
        }).start();
    }

    public static boolean active = false;
    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }
    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

}
