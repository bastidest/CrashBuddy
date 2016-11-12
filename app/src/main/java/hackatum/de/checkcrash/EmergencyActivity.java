package hackatum.de.checkcrash;

import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import hackatum.de.checkcrash.fragments.ButtonFragment;
import hackatum.de.checkcrash.models.AccidentProcedure;

public class EmergencyActivity extends AppCompatActivity implements ButtonFragment.OnFragmentInteractionListener {

    private final int fragmentContainer = R.id.fragment_container;
    private FragmentManager fragmentManager;
    private AccidentProcedure accidentProcedure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        fragmentManager = getSupportFragmentManager();

        try {
            Resources res = getResources();
            InputStream jsonInputStream = res.openRawResource(R.raw.germany);

            byte[] b = new byte[jsonInputStream.available()];
            jsonInputStream.read(b);

            AccidentProcedure.load(new String(b));
            Log.d("EA", AccidentProcedure.accidentProcedure.rootPage);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //loadFragment(accidentProcedure.rootPage, 0);
    }


    /**
     * @param pageId    the page id to load
     * @param direction -1 --> backwards animation; 0 --> no animation; 1 --> forward animation
     */
    private void loadFragment(String pageId, int direction) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (direction) {
            case 1:
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                break;
        }
        fragmentTransaction.replace(fragmentContainer, ButtonFragment.newInstance(pageId));
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
