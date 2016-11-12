package hackatum.de.checkcrash;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.io.IOException;
import java.io.InputStream;

import hackatum.de.checkcrash.design.BreadcrumbView;
import hackatum.de.checkcrash.fragments.ButtonFragment;
import hackatum.de.checkcrash.fragments.PageFragmentListener;
import hackatum.de.checkcrash.models.AccidentProcedure;
import hackatum.de.checkcrash.models.Answer;
import hackatum.de.checkcrash.models.Page;

public class EmergencyActivity extends AppCompatActivity implements PageFragmentListener {

    private final int fragmentContainer = R.id.fragment_container;
    private HorizontalScrollView scrollView;
    private ViewGroup breadcrumbs;
    private FragmentManager fragmentManager;
    private AccidentProcedure accidentProcedure;
    private ViewGroup buttons;
    private boolean firstPage = true;

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
                BreadcrumbView bv = new BreadcrumbView(this, firstPage);
                firstPage = false;
                Page page = AccidentProcedure.accidentProcedure.pages.get(pageId);
                bv.setText(page.shortDesc);
                breadcrumbs.addView(bv);
                break;
            case -1:
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        }
        fragmentTransaction.replace(fragmentContainer, ButtonFragment.newInstance(pageId));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onPageLoad(Answer[] answers) {
        buttons.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        for (final Answer answer : answers) {
            Button button = new Button(this);
            button.setText(answer.text);
            button.setLayoutParams(params);
            buttons.addView(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadFragment(answer.next, 1);
                }
            });
        }
        scrollView.fullScroll(ScrollView.FOCUS_RIGHT);
    }

    @Override
    public void onBackPressed() {
        fragmentManager.popBackStack();
    }
}
