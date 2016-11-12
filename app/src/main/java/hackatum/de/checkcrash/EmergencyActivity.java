package hackatum.de.checkcrash;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import hackatum.de.checkcrash.design.BreadcrumbView;
import hackatum.de.checkcrash.fragments.AudioRecorderFragment;
import hackatum.de.checkcrash.fragments.ButtonFragment;
import hackatum.de.checkcrash.fragments.CallFragment;
import hackatum.de.checkcrash.fragments.CameraFragment;
import hackatum.de.checkcrash.fragments.PageFragmentListener;
import hackatum.de.checkcrash.models.AccidentProcedure;
import hackatum.de.checkcrash.models.Answer;
import hackatum.de.checkcrash.models.Page;

public class EmergencyActivity extends AppCompatActivity implements PageFragmentListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private HorizontalScrollView scrollView;
    private ViewGroup breadcrumbs;
    private FragmentManager fragmentManager;
    private AccidentProcedure accidentProcedure;
    private ViewGroup buttons;
    private ArrayList<Page> pageList = new ArrayList<>();
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        buttons = (ViewGroup) findViewById(R.id.buttons);
        breadcrumbs = (ViewGroup) findViewById(R.id.breadcrumbsll);
        scrollView = (HorizontalScrollView) findViewById(R.id.breadcrumbs);

        fragmentManager = getSupportFragmentManager();

        try {
            Resources res = getResources();
            InputStream jsonInputStream = res.openRawResource(R.raw.germany);

            byte[] b = new byte[jsonInputStream.available()];
            jsonInputStream.read(b);

            AccidentProcedure.load(new String(b));
            accidentProcedure = AccidentProcedure.accidentProcedure;
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadFragment(accidentProcedure.rootPage, 0);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
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
            case -1:
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
                break;
        }
        int fragmentContainer = R.id.fragment_container;

        Page page = AccidentProcedure.accidentProcedure.pages.get(pageId);
        Fragment fragment = null;
        switch (page.type) {
            case "buttons":
                fragment = ButtonFragment.newInstance(pageId);
                break;
            case "image_capture":
                fragment = CameraFragment.newInstance(pageId);
                break;
            case "audio_capture":
                fragment = AudioRecorderFragment.newInstance(pageId);
                break;
            case "call":
                fragment = CallFragment.newInstance(pageId);
                break;
        }
        if (fragment != null) {
            fragmentTransaction.replace(fragmentContainer, fragment);
            if (pageList.size() > 0)
                fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            pageList.add(page);
        }
    }

    /**
     * loads a Page that is already loaded as a Fragment in the Activity and goes back to it
     * and deletes the backStack to the first Page
     *
     * @param p
     */
    private void loadExistingPage(Page p) {
        int index = pageList.indexOf(p);
        for (int i = pageList.size() - 1; i > index; i--) {
            pageList.remove(i);
            fragmentManager.popBackStack();
        }
//        HashMap<String, Page> hashMap = AccidentProcedure.accidentProcedure.pages;
//        for (Map.Entry<String, Page> entry : hashMap.entrySet()) {
//            if(entry.getValue().equals(p)) {
//                loadFragment(entry.getKey(), -1);
//            }
//        }

    }

    /**
     * draws the Breadcrumb bar and sets up OnClickListeners
     */
    private void drawBreadcumbs() {
        boolean first = true;
        breadcrumbs.removeAllViews();
        for (int i = 0; i < pageList.size() - 1; i++) {
            final Page p = pageList.get(i);
            BreadcrumbView bv = new BreadcrumbView(this, first);
            bv.setText(p.shortDesc);

            bv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadExistingPage(p);
                }
            });

            breadcrumbs.addView(bv);
            first = false;
        }
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_RIGHT);
            }
        });

    }

    /**
     * draws all possible answers an sets OnClickListeners for each one
     *
     * @param answers all possible answers
     */
    private void drawButtons(Answer[] answers) {
        buttons.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        for (final Answer answer : answers) {
            Button button = new Button(this);
            button.setText(answer.text);
            button.setLayoutParams(params);
            if (answer.color != null && answer.color.length() > 0) {
                int color = Color.parseColor(answer.color);
                if (color != -1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        button.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{color}));
                    } else {
                        button.setBackgroundColor(color);
                    }
                }
            }

            buttons.addView(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadFragment(answer.next, 1);
                }
            });
        }
    }


    /**
     * Callback from the Fragment after it finished drawing the Views
     *
     * @param page The Page the Fragment is referencing
     */
    @Override
    public void onPageLoad(Page page) {
        drawBreadcumbs();
        drawButtons(page.answers);
    }

    @Override
    public void onBackPressed() {
        fragmentManager.popBackStack();
        if (pageList.size() > 1)
            pageList.remove(pageList.size() - 1);
    }

    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings_menu_item) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
