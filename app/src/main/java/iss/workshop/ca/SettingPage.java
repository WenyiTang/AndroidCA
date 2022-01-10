package iss.workshop.ca;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingPage extends AppCompatActivity {

    private Switch musicSwitch;

    private UserSettings settings;

    private float volume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        settings = (UserSettings) getApplication();

        musicSwitch = findViewById(R.id.musicSwitch);


        //loadSharedPreferences();

    }

    /*private void loadSharedPreferences() {
        SharedPreferences sp = getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE);
        String soundPref = sp.getString(UserSettings.SOUND_PREF, UserSettings.SOUND_ON);
        settings.setSoundPref(soundPref);
    }

    private void initSwitchListener(){
        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    settings.setSoundPref(UserSettings.SOUND_OFF);
                }
                else{
                    settings.setSoundPref(UserSettings.SOUND_ON);
                }

                SharedPreferences.Editor editor = getSharedPreferences(UserSettings.PREFERENCES,MODE_PRIVATE).edit();
                editor.putString(UserSettings.SOUND_PREF,settings.getSoundPref());
                editor.commit();
                updateSound();
            }
        });
    }

    private void updateSound(){
        if (settings.getSoundPref().equals(UserSettings.SOUND_OFF))
        {
            volume=0f;
        }
        if (settings.getSoundPref().equals(UserSettings.SOUND_ON))
        {
            volume=1f;
        }
    }*/


}
