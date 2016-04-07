package com.comp3617.finalproject.meggsage;

import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.RingtonePreference;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_settings);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

    }

    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.app_prefs);

            RingtonePreference ringPref = (RingtonePreference) findPreference("tr_ringtone");
            String ringtonePath = getPreferenceScreen().getSharedPreferences().getString(ringPref.getKey(), "");
            Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), Uri.parse(ringtonePath));
            ringPref.setSummary(ringtone.getTitle(getActivity()));

            RingtonePreference ringPref2 = (RingtonePreference) findPreference("nr_ringtone");
            String ringtonePath2 = getPreferenceScreen().getSharedPreferences().getString(ringPref2.getKey(), "");
            Ringtone ringtone2 = RingtoneManager.getRingtone(getActivity(), Uri.parse(ringtonePath2));
            ringPref2.setSummary(ringtone2.getTitle(getActivity()));


            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Preference pref = findPreference(key);

            if (pref instanceof RingtonePreference) {
                RingtonePreference ringPref = (RingtonePreference) pref;
                String ringtonePath = sharedPreferences.getString(ringPref.getKey(), "");
                Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), Uri.parse(ringtonePath));
                ringPref.setSummary(ringtone.getTitle(getActivity()));
            }
        }


    }

}
