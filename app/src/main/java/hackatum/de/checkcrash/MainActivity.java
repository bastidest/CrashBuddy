package hackatum.de.checkcrash;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.gson.Gson;

import java.io.InputStream;

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

            Gson gson = new Gson();
            AccidentProcedure response = gson.fromJson(new String(b), AccidentProcedure.class);

            System.out.println(response.pages.get("injured").answers[0].text);

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
