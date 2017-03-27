package com.bartovapps.androidtest;

/**
 * Created by motibartov on 23/03/2017.
 */

public interface MainActivityContract {

    interface View extends BaseView{
        void openSettings();
    }

    interface Presenter extends BasePresenter{
        void drawerSettingsItemClicked(int itemId);
    }

}
