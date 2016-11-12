package hackatum.de.checkcrash.models;

import android.content.Context;

import java.util.Locale;

import hackatum.de.checkcrash.SpeechSynthesis;

/**
 * Created by matthias on 11/11/16.
 */
public class Page {

    public String type;
    public String question;
    public String shortDesc;
    public String description;

    public Answer[] answers;

    public void speak(Context c, Locale language) {
        SpeechSynthesis tts = SpeechSynthesis.getInstance(c, language);
        tts.say(question);
    }


}
