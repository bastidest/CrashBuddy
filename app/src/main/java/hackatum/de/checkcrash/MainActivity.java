package hackatum.de.checkcrash;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.InputStream;

import hackatum.de.checkcrash.models.AccidentProcedure;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create an instance of GoogleAPIClient.
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        try {
            Resources res = getResources();
            InputStream jsonInputStream = res.openRawResource(R.raw.germany);

            byte[] b = new byte[jsonInputStream.available()];
            jsonInputStream.read(b);

            AccidentProcedure.load(new String(b));


        } catch (Exception e) {
            e.printStackTrace();
        }


        ((Button) findViewById(R.id.button_settings)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
    }

    protected void onStart() {
        googleApiClient.connect();

        super.onStart();
    }

    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }



    public void launchEmergency(View v) {
        Intent intent = new Intent(this, EmergencyActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Geolocation.requestGeolocation(this, googleApiClient);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
