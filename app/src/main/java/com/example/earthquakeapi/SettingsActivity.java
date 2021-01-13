package com.example.earthquakeapi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity
{
    private static SharedPreferences.OnSharedPreferenceChangeListener preferences;
    private static String order_by = "order_by";
    private static String minimum_magnitude = "min_magnitude";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

//    public static class EarthquakePreferenceFragment extends PreferenceFragment
//            implements Preference
//
//            .OnPreferenceChangeListener
//    {
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            addPreferencesFromResource(R.xml.settings_main);
//
//            Preference minMagnitude = findPreference(getString(R.string.settings_min_magnitude_key));
//            bindPreferenceSummaryToValue(minMagnitude);
//
//            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
//            bindPreferenceSummaryToValue(orderBy);
//        }
//
//        @Override
//        public boolean onPreferenceChange(Preference preference, Object newValue)
//        {
//            String stringValue = newValue.toString();
//            if (preference instanceof ListPreference) {
//                ListPreference listPreference = (ListPreference) preference;
//                int prefIndex = listPreference.findIndexOfValue(stringValue);
//                if (prefIndex >= 0) {
//                    CharSequence[] labels = listPreference.getEntries();
//                    preference.setSummary(labels[prefIndex]);
//                }
//            }
//            else {
//                preference.setSummary(stringValue);
//            }
//            return true;
//        }
//        private void bindPreferenceSummaryToValue(Preference preference)
//        {
//            preference.setOnPreferenceChangeListener(this);
//            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
//            String preferenceString = preferences.getString(preference.getKey(), "");
//            onPreferenceChange(preference, preferenceString);
//        }
//    }
     public static class EarthquakePreferenceFragment extends PreferenceFragmentCompat
    {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
        {
            setPreferencesFromResource(R.xml.settings_main, rootKey);

            preferences = new SharedPreferences.OnSharedPreferenceChangeListener()
            {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
                {
                    if(key.equals(order_by))
                    {
                        androidx.preference.Preference order_by_preference = findPreference(order_by);
                        order_by_preference.setSummary(sharedPreferences.getString(order_by, ""));
                    }
                    if(key.equals(minimum_magnitude))
                    {
                        androidx.preference.Preference min_magnitude_preference = findPreference(minimum_magnitude);
                        min_magnitude_preference.setSummary(sharedPreferences.getString(minimum_magnitude, ""));
                    }
                }
            };
        }

        @Override
        public void onResume()
        {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferences);

            androidx.preference.Preference order_by_preference = findPreference(order_by);
            order_by_preference.setSummary(getPreferenceScreen().getSharedPreferences().getString(order_by, ""));

            androidx.preference.Preference min_magnitude_preference = findPreference(minimum_magnitude);
            min_magnitude_preference.setSummary(getPreferenceScreen().getSharedPreferences().getString(minimum_magnitude, ""));

        }

        @Override
        public void onPause()
        {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferences);
        }
    }
}
