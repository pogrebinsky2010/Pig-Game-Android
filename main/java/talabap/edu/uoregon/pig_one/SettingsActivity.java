package talabap.edu.uoregon.pig_one;

/**
 * Created by talaba on 7/2/16.
 */
import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity{

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref);


    }

}