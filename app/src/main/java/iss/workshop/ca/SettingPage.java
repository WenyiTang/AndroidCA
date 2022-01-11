package iss.workshop.ca;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingPage extends AppCompatActivity{

    private Switch musicSwitch;

    private UserSettings settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        musicSwitch = findViewById(R.id.musicSwitch);
        musicSwitch.setChecked(true);

        settings = (UserSettings) getApplication();
        initSwitchListener();
        loadSharedPreferences();


        getMusicVolume(getApplicationContext());

    }

    private void loadSharedPreferences(){
        SharedPreferences sharedPreferences=getSharedPreferences(UserSettings.PREFERENCES,MODE_PRIVATE);
        float soundPref=sharedPreferences.getFloat(UserSettings.PREFERENCES,UserSettings.SOUND_ON);

        //if (settings.getSoundPref().equals(UserSettings.SOUND_OFF)){
        //    soundPref=sharedPreferences.getFloat(UserSettings.PREFERENCES,UserSettings.SOUND_OFF);
        //}

        settings.setSoundPref(soundPref);
    }



    private void initSwitchListener(){
        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                //SharedPreferences sharedPref = getSharedPreferences("volume", Context.MODE_PRIVATE);
                //SharedPreferences.Editor editor =sharedPref.edit();


                if(isChecked){
                    settings.setSoundPref(UserSettings.SOUND_ON);
                    Toast.makeText(getApplicationContext(), "Switch enabled", Toast.LENGTH_SHORT).show();
                    //editor.putFloat("volume",1f);
                    //editor.commit();
                }
                else{
                    settings.setSoundPref(UserSettings.SOUND_OFF);
                    Toast.makeText(getApplicationContext(), "Switch disabled", Toast.LENGTH_SHORT).show();
                    //editor.clear();
                    //editor.putFloat("volume",0f);
                    //editor.commit();
                }

                SharedPreferences.Editor editor=getSharedPreferences(UserSettings.PREFERENCES,MODE_PRIVATE).edit();
                editor.putFloat(UserSettings.SOUND_PREF,settings.getSoundPref());
                editor.commit();
                getMusicVolume(getApplicationContext());

            }
        });
    }

    public static float getMusicVolume(Context context){
        //SharedPreferences sharedPref = context.getSharedPreferences("volume", Context.MODE_PRIVATE);
        //float volume = sharedPref.getFloat("volume",1f);

        SharedPreferences sharedPref = context.getSharedPreferences(UserSettings.SOUND_PREF, Context.MODE_PRIVATE);
        float volume = sharedPref.getFloat(UserSettings.SOUND_PREF,1f);



        return volume;
    }




}
