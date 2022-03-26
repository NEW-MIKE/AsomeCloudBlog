package com.kaya.wifitransform;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.kaya.wifitransform.source.utils.NetworkUtils;
import com.kaya.wifitransform.wifiTransfer.Defaults;
import com.kaya.wifitransform.wifiTransfer.ServerRunner;

public class MainActivity extends AppCompatActivity {
    public Toolbar mCommonToolbar;
    protected Context mContext;
    private TextView  mTvWifiName,mTvWifiIp,tvRetry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mCommonToolbar = findViewById(R.id.common_toolbar);

        mTvWifiIp = findViewById(R.id.mTvWifiIp);
        mTvWifiName = findViewById(R.id.mTvWifiName);
        tvRetry = findViewById(R.id.tvRetry);
        mCommonToolbar.setTitle("WiFi传书");
        setSupportActionBar(mCommonToolbar);
        initDatas();
    }

    public void initDatas() {
        String wifiname = NetworkUtils.getConnectWifiSsid(mContext);
        if (!TextUtils.isEmpty(wifiname)) {
            mTvWifiName.setText(wifiname.replace("\"", ""));
        } else {
            mTvWifiName.setText("Unknow");
        }

        String wifiIp = NetworkUtils.getConnectWifiIp(mContext);
        if (!TextUtils.isEmpty(wifiIp)) {
            tvRetry.setVisibility(View.GONE);
            mTvWifiIp.setText("http://" + NetworkUtils.getConnectWifiIp(mContext) + ":" + Defaults.getPort());
            // 启动wifi传书服务器
            ServerRunner.startServer();
        } else {
            mTvWifiIp.setText("请开启Wifi并重试");
            tvRetry.setVisibility(View.VISIBLE);
        }
    }
}