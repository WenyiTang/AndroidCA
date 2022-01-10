package iss.workshop.ca;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.os.Bundle;

public class UserSettings extends Application {

    public static final String PREFERENCES = "preferences";

    public static final String SOUND_PREF = "soundPref";
    public static final String SOUND_ON = "soundOn";
    public static final String SOUND_OFF = "soundOff";

    private String soundPref;

    public String getSoundPref() {
        return soundPref;
    }

    public void setSoundPref(String soundPref) {
        this.soundPref = soundPref;
    }
}