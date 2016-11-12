package hackatum.de.checkcrash.models;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;

import hackatum.de.checkcrash.SpeechSynthesis;

import static android.content.Context.MODE_PRIVATE;

public class Page {

    public String type;
    public String question;
    public String shortDesc;
    public String description;

    public Answer[] answers;

    public void speak(Context c, Locale language) {
        // TODO: 12.11.2016 dont check shared preferences on every speak call
        SharedPreferences sharedPreferences = c.getSharedPreferences("settings", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("tts", true)) {
            SpeechSynthesis tts = SpeechSynthesis.getInstance(c, language);
            tts.say(question);
        }

    }


}
