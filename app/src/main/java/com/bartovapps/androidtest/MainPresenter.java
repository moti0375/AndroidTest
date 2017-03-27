package com.bartovapps.androidtest;

import android.app.Activity;
import android.content.Context;

/**
 * Created by motibartov on 23/03/2017.
 */

public class MainPresenter implements MainActivityContract.Presenter {

    Context mContext;
    MainActivityContract.View mView;

    public MainPresenter(Context context, MainActivityContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void setApiClient(int apiClient) {

    }

    @Override
    public void drawerSettingsItemClicked(int itemId) {
        switch (itemId) {
            case R.id.settings:
                mView.openSettings();
                break;
        }
    }
}
