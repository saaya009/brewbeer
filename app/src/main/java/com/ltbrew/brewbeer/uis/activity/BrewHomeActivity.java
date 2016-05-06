package com.ltbrew.brewbeer.uis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ltbrew.brewbeer.BrewApp;
import com.ltbrew.brewbeer.R;
import com.ltbrew.brewbeer.interfaceviews.BrewHomeView;
import com.ltbrew.brewbeer.presenter.BrewHomePresenter;
import com.ltbrew.brewbeer.presenter.model.Device;
import com.ltbrew.brewbeer.presenter.util.DeviceUtil;
import com.ltbrew.brewbeer.uis.adapter.SectionsPagerAdapter;
import com.ltbrew.brewbeer.uis.fragment.BrewSessionFragment;
import com.ltbrew.brewbeer.uis.fragment.RecipeFragment;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BrewHomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, BrewHomeView {

    @BindView(R.id.homeCenterTitle)
    TextView homeCenterTitle;
    @BindView(R.id.homeCreateBrewSession)
    TextView homeCreateBrewSession;
    @BindView(R.id.brewRb)
    RadioButton brewRb;
    @BindView(R.id.recipeRb)
    RadioButton recipeRb;

    private BrewSessionFragment brewSessionFragment = new BrewSessionFragment();
    private RecipeFragment recipeFragment = new RecipeFragment();
    private Fragment[] fragments = new Fragment[]{brewSessionFragment, recipeFragment};
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private BrewHomePresenter brewHomePresenter;
    private List<Device> devices = Collections.EMPTY_LIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brew_home);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        homeCenterTitle.setTypeface(BrewApp.getInstance().textFont);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.setFragments(fragments);
        mViewPager = (ViewPager) findViewById(R.id.brewHomeContainer);
        if (mViewPager != null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }
        mViewPager.setOffscreenPageLimit(2);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.setDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
        brewHomePresenter = new BrewHomePresenter(this);
        brewHomePresenter.getDevs();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_add_dev) {
            startAddDevActivity();
        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_exit) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    void startAddDevActivity(){
        Intent intent = new Intent();
        intent.setClass(this, AddDevActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.homeCreateBrewSession)
    public void createBrewSession(){
        if(devices.size() == 0 || DeviceUtil.getCurrentDevId() == null){
            showMsgWindow("提醒", "您还未添加任何设备， 请先添加", null);
            return;
        }
        startAddRecipeActivity();
    }

    private void startAddRecipeActivity() {
        Intent intent = new Intent();
        intent.setClass(this, AddRecipeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onGetDevsSuccess(List<Device> devices) {
        this.devices = devices;
        recipeFragment.getAllRecipes();
    }

    @Override
    public void onGetDevsFailed(String message) {

    }
}
