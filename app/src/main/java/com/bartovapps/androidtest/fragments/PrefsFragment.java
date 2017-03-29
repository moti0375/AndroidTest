package com.bartovapps.androidtest.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.bartovapps.androidtest.R;

/**
 * Created by motibartov on 21/03/2017.
 */

public class PrefsFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    }
}
