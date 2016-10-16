package com.fan.mvp.model;

import android.content.Context;

import com.fan.mvp.presenter.MainPresenter;

/**
 * 主页获取天气信息接口
 */
public interface MainModle {
        void getWeather(Context context, String cityNum, MainPresenter listener);
}