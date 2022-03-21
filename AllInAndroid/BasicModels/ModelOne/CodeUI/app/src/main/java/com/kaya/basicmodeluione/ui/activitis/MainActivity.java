package com.kaya.basicmodeluione.ui.activitis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.kaya.basicmodeluione.R;
import com.kaya.basicmodeluione.base.BaseActivity;
import com.kaya.basicmodeluione.ui.fragments.FragmentB;
import com.kaya.basicmodeluione.ui.fragments.FragmentC;
import com.kaya.basicmodeluione.ui.fragments.FragmentA;
import com.kaya.basicmodeluione.view.RVPIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.indicator)
    RVPIndicator mIndicator;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    private List<Fragment> mTabContents;
    private List<String> mDatas;
    private FragmentPagerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                FloatActivity.actionStart(this,"dd");
                break;
            case R.id.action_login:
                FloatActivity.actionStart(this,"dd");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setLogo(R.mipmap.logo);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle("");

    }

    @Override
    public void initDatas() {
        mDatas = Arrays.asList(getResources().getStringArray(R.array.home_tabs));
        mTabContents = new ArrayList<>();
        mTabContents.add(new FragmentA());
        mTabContents.add(new FragmentB());
        mTabContents.add(new FragmentC());
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTabContents.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mTabContents.get(position);
            }
        };
    }

    @Override
    public void configViews() {
        mIndicator.setTabItemTitles(mDatas);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);

        mIndicator.setViewPager(mViewPager, 0);
    }
}