package com.example.preferenciastarea;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class Preferencias extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferencias, rootKey);
    }
}