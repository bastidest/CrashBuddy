package hackatum.de.checkcrash;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by matthias on 11/11/16.
 */

public class SpeechSynthesis implements TextToSpeech.OnInitListener {

    private static SpeechSynthesis speechSynthesis;

    private boolean ready = false;
    private boolean hasError = false;
    private String say = "";
    private TextToSpeech tts;
    private Locale lang;

    /**
     * Private contructor - can't be called from outside
     *
     * @param c
     * @param lang
     */
    private SpeechSynthesis(Context c, Locale lang) {
        tts = new TextToSpeech(c, this);
        this.lang = lang;
    }

    /**
     * Get instance of the singleton class
     *
     * @param c
     * @param lang
     * @return
     */
    public static SpeechSynthesis getInstance(Context c, Locale lang) {
        if (speechSynthesis == null) {
            speechSynthesis = new SpeechSynthesis(c, lang);
        }

        return speechSynthesis;
    }

    /**
     * The TTS library has loaded
     * If the "say" Command has been called before, now the text will be spoken
     *
     * @param i
     */
    @Override
    public void onInit(int i) {

        //Dont use library if TTS could not be initialized correctly
        if (i != TextToSpeech.SUCCESS) {
            hasError = true;
        }

        int setLanguageResult = tts.setLanguage(this.lang);

        //If setting the language failed, don't use TTS
        if (setLanguageResult == TextToSpeech.LANG_MISSING_DATA ||
                setLanguageResult == TextToSpeech.LANG_NOT_SUPPORTED) {
            hasError = true;
        }

        //Set speech speed
        tts.setSpeechRate(0.7f);

        //Speak text if the say command has been called before
        if (!say.equals("") && !hasError) {
            speak(say);
            say = "";
        }

        //Set state to ready - now the say command works
        ready = true;
    }

    /**
     * Speaks the given text immediately or when the Library has finished loading
     *
     * @param text
     */
    public void say(String text) {

        //Only use TTS when the libaray loaded correctly
        if (!hasError) {
            say = text;
            if (ready) {
                speak(say);
            }
        } else {
            System.out.println("Error - Speech synthesis disabled");
        }
    }

    /**
     * Speaks the text
     *
     * @param speak
     */
    private void speak(String speak) {
        //Use deprecated method for older devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(speak, TextToSpeech.QUEUE_FLUSH, null, "tts");
        } else {
            tts.speak(speak, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


}
