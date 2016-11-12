package hackatum.de.checkcrash.models;

import android.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;

public class AudioRecording {

    public static ArrayList<AudioRecording> recordings = new ArrayList<>();
    public String timestamp;
    public String audioPath;

    public void play() {
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource("file://" + audioPath);
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
