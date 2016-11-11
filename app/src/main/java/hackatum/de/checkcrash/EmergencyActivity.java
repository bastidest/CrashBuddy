package hackatum.de.checkcrash;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import hackatum.de.checkcrash.fragments.ButtonFragment;

public class EmergencyActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private int fragmentContainer = R.id.fragment_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(fragmentContainer, new ButtonFragment());
        fragmentTransaction.commit();
    }
}
