package com.fan.mvp.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fan.mvp.R;
import com.fan.mvp.entity.Weather;
import com.fan.mvp.loaddingview.LoadingState;
import com.fan.mvp.loaddingview.LoadingView;
import com.fan.mvp.loaddingview.OnRetryListener;
import com.fan.mvp.presenter.MainPresenterImpl;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView{

    @Bind(R.id.edit_num)
    EditText editNum;
    @Bind(R.id.btn_go)
    Button btnGo;
    @Bind(R.id.loadding)
    LoadingView loadding;
    @Bind(R.id.show_text)
    TextView showText;
    private MainPresenterImpl presenter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (null == presenter) {
            presenter = new MainPresenterImpl(this);
        }
        presenter.initView();
    }
    @Override
    public void initView() {
        loadding.withLoadedEmptyText("≥﹏≤ , 连条毛都没有 !").withEmptyIco(R.mipmap.disk_file_no_data).withBtnEmptyEnnable(false)
                .withErrorIco(R.mipmap.ic_chat_empty).withLoadedErrorText("(῀( ˙᷄ỏ˙᷅ )῀)ᵒᵐᵍᵎᵎᵎ,我家程序猿跑路了 !").withbtnErrorText("去找回她!!!")
                .withLoadedNoNetText("你挡着信号啦o(￣ヘ￣o)☞ᗒᗒ 你走").withNoNetIco(R.mipmap.ic_chat_empty).withbtnNoNetText("网弄好了，重试")
                .withLoadingIco(R.drawable.loading_animation).withLoadingText("加载中...").withOnRetryListener(new OnRetryListener() {
            @Override
            public void onRetry(){
                //传送给P层次
                presenter.sendRequest(MainActivity.this,editNum.getText().toString().trim());
            }
        }).build();
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //传送给P层次
                presenter.sendRequest(MainActivity.this,editNum.getText().toString().trim());
            }
        });
        loadding.setVisibility(View.GONE);
        showText.setVisibility(View.GONE);
    }

    @Override
    public void showSuccess(Weather weather) {
        showText.setText(weather.toString());
        loadding.setVisibility(View.GONE);
        showText.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoNet() {
        loadding.setState(LoadingState.STATE_NO_NET);
        loadding.setVisibility(View.VISIBLE);
        showText.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        loadding.setState(LoadingState.STATE_ERROR);
        loadding.setVisibility(View.VISIBLE);
        showText.setVisibility(View.GONE);
    }

    @Override
    public void showLoadding() {
        loadding.setState(LoadingState.STATE_LOADING);
        loadding.setVisibility(View.VISIBLE);
        showText.setVisibility(View.GONE);
    }
}
