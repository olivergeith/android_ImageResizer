
package de.geithonline.imageresizer;

import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import de.geithonline.imageresizer.settings.Settings;

public class MainPreferencesActivity extends PreferenceActivity {

    private BillingManager billingManager;
    private PermissionRequester permissionRequester;
    public static SharedPreferences prefs;

    @Override
    protected boolean isValidFragment(final String fragmentName) {
        Log.i("GEITH", "isValidFragment Called for " + fragmentName);

        return AboutFragment.class.getName().equals(fragmentName); //
        // || BackgroundPreferencesFragment.class.getName().equals(fragmentName);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final String permissions[], final int[] grantResults) {
        permissionRequester.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Homebutton im Action bar

        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Unsere eigenes PreferenceFile f�r den LWPService
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // prefs = getSharedPreferences(Settings.LWP_PREFERENCE_FILE, MODE_PRIVATE);
        Settings.initPrefs(prefs, getApplicationContext());

        // getPreferenceManager().setSharedPreferencesName("preference_file_name");

        // Setting up Permission requester
        permissionRequester = new PermissionRequester(this);
        permissionRequester.requestPermission();

        // Setting up Billing
        billingManager = new BillingManager(this);
        final boolean isPremium = billingManager.isPremium();

        // A View because there might be another button (billing)
        final LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        ll.setGravity(Gravity.CENTER);

        // Add a button to the header list.
        if (!isPremium) {
            final Button button = billingManager.getButton();
            button.setBackgroundResource(R.color.accent);
            ll.addView(button);
        }
        // set view with buttons to the list footer
        setListFooter(ll);
    }

    /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(final List<Header> target) {
        super.onBuildHeaders(target);
        loadHeadersFromResource(R.xml.preferences_header, target);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        billingManager.onDestroy();
    }

}