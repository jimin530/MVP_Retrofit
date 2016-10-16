package com.fan.mvp.presenter;

import android.content.Context;

import com.fan.mvp.entity.Weather;
import com.fan.mvp.model.MainModelImpl;
import com.fan.mvp.model.MainModle;
import com.fan.mvp.view.MainView;

/**
 * Created by fan on 2016/10/16.
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛Code is far away from bug with the animal protecting
 * 　　　　┃　　　┃    神兽保佑,代码无bug
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * <p/>
 * ━━━━━━感觉萌萌哒━━━━━━
 */
public class MainPresenterImpl implements MainPresenter{
    private MainView mainView;
    private MainModle mainModle;
    public MainPresenterImpl(MainView mainView){
        this.mainView = mainView;
        mainModle = new MainModelImpl();
    }

    @Override
    public void onSuccess(Weather weather) {
        mainView.showSuccess(weather);
    }

    @Override
    public void onError() {
        mainView.showError();
    }

    @Override
    public void showNoNet() {
        mainView.showNoNet();
    }

    @Override
    public void initView() {
        mainView.initView();
    }

    @Override
    public void sendRequest(Context context, String cityNum) {
        mainView.showLoadding();
        mainModle.getWeather(context,cityNum,this);
    }
}
