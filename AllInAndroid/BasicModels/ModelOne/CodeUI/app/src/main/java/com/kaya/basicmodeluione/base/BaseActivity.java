package com.kaya.basicmodeluione.base;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kaya.basicmodeluione.ModelUIApp;
import com.kaya.basicmodeluione.R;
import com.kaya.basicmodeluione.utils.DisplayUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
* 在这里做全生命周期的一个管理和控制，完成一些公有的操作和控制*/
public abstract class BaseActivity extends AppCompatActivity {
    public Toolbar mCommonToolbar;
    private Unbinder binder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        DisplayUtil.setCustomDensity(this, ModelUIApp.getsInstance());
        binder = ButterKnife.bind(this);
        mCommonToolbar = findViewById(R.id.common_toolbar);
        if (mCommonToolbar != null) {
            initToolBar();
            setSupportActionBar(mCommonToolbar);
        }
        initDatas();
        configViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binder.unbind();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public abstract int getLayoutId();
    public abstract void initToolBar();


    public abstract void initDatas();

    /**
     * 对各种控件进行设置、适配、填充数据
     */
    public abstract void configViews();
}
