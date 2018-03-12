
package de.geithonline.imageresizer;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import de.geithonline.imageresizer.settings.Settings;

public class AboutFragment extends PreferenceFragment {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_about);
        enableProFeatures();
    }

    private void enableProFeatures() {
        final Preference proBox = findPreference("premium");

        if (Settings.isPremium()) {
            proBox.setTitle("This is the Premium Version");
            proBox.setIcon(R.drawable.icon_premium);
        } else {
            proBox.setTitle("This is the Free Version");
            proBox.setIcon(R.drawable.icon);
        }
    }

}
