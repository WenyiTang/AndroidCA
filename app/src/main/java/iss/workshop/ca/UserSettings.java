package iss.workshop.ca;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.os.Bundle;

public class UserSettings extends Application{

    public static final String PREFERENCES = "preferences";

    public static final String SOUND_PREF = "soundPref";
    public static final float SOUND_ON = 1f;
    public static final float SOUND_OFF = 0f;

    private Float soundPref;

    public Float getSoundPref() {
        return soundPref;
    }

    public void setSoundPref(Float soundPref) {
        this.soundPref = soundPref;
    }
}