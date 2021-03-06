package nl.adeda.reisplanner;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment = null;
    Fragment currentFragment;
    String name;
    SharedPreferences savedState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadContent(savedInstanceState);

        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void loadContent(Bundle savedInstanceState) {
        // Restore state when app is destroyed
        savedState = getSharedPreferences("savedState", MODE_PRIVATE);
        if (savedState != null) {
            String className = savedState.getString("fragmentState", name);
            fragment = selectTask(className);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();
        }

        if (savedInstanceState != null) {
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
          //  return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mijn_reizen) {
            // Open 'Mijn reizen'
            fragment = new MijnReizen();
        } else if (id == R.id.nav_reisplanner) {
            // Open 'Reisplanner'
            if (fragment != new Reisplanner()) {
                fragment = new Reisplanner();
            }
        }

        // Set fragment to content_main
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (fragment != null) {
            getSupportFragmentManager().putFragment(outState, "fragment", fragment);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_main);
        if (currentFragment != null) {
            name = currentFragment.getClass().getName();
            savedState = getSharedPreferences("savedState", MODE_PRIVATE);
            SharedPreferences.Editor editor = savedState.edit();
            editor.putString("fragmentState", name);
            editor.commit();
        }
        super.onStop();

    }

    public Fragment selectTask(String className) {
        switch (className) {
            case "nl.adeda.reisplanner.Reisplanner":
                return new Reisplanner();
            case "nl.adeda.reisplanner.Reisadvies":
                /*return new Reisadvies();*/ return new Reisplanner();
            case "nl.adeda.reisplanner.MijnReizen":
                return new MijnReizen();
        }
        return null;
    }
}
