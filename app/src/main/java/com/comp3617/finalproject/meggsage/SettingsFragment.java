package com.comp3617.finalproject.meggsage;


import android.os.Bundle;
import android.preference.PreferenceFragment;

public static class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.app_prefs);
    }

}
