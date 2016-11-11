package hackatum.de.checkcrash;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.InputStream;
import java.util.Locale;

import hackatum.de.checkcrash.models.AccidentProcedure;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            Resources res = getResources();
            InputStream jsonInputStream = res.openRawResource(R.raw.germany);

            byte[] b = new byte[jsonInputStream.available()];
            jsonInputStream.read(b);

            AccidentProcedure.load(new String(b));

            AccidentProcedure.accidentProcedure.pages.get("injured").speak(this, Locale.ENGLISH);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void launchSettings(View v) {
        // TODO: 11.11.2016 implement
    }

    public void launchEmergency(View v) {
        Intent intent = new Intent(this, EmergencyActivity.class);
        startActivity(intent);
    }
}
