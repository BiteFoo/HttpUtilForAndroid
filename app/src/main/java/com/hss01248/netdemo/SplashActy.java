package com.hss01248.netdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.hss01248.netdemo.akulaku.AkulakuActy;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by huangshuisheng on 2017/9/29.
 */

public class SplashActy extends Activity {

    @Bind(R.id.button_qxinli)
    Button buttonQxinli;
    @Bind(R.id.button4_akulaku)
    Button button4Akulaku;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_splash);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.button_qxinli, R.id.button4_akulaku})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_qxinli:
                startActivity(new Intent(this,MainActivityNew.class));
                break;
            case R.id.button4_akulaku:
                startActivity(new Intent(this,AkulakuActy.class));
                break;
        }
    }
}
