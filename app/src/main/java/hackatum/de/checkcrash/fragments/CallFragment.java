package hackatum.de.checkcrash.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

import hackatum.de.checkcrash.Geolocation;
import hackatum.de.checkcrash.R;
import hackatum.de.checkcrash.SystemOverlay;
import hackatum.de.checkcrash.models.AccidentProcedure;
import hackatum.de.checkcrash.models.Page;

public class CallFragment extends Fragment {

    private static final String ARG_PAGE_ID = "pageid";
    private String pageId;

    private PageFragmentListener mListener;

    public CallFragment() {
        // Required empty public constructor
    }

    public static CallFragment newInstance(String pageId) {
        CallFragment fragment = new CallFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PAGE_ID, pageId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageId = getArguments().getString(ARG_PAGE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call, container, false);

        Page page = AccidentProcedure.accidentProcedure.pages.get(pageId);

        ImageButton callButton = (ImageButton) view.findViewById(R.id.button_call);
        WebView webView = (WebView) view.findViewById(R.id.web_view);
        TextView questionText = (TextView) view.findViewById(R.id.text_question);

        final String uri;
        if (page.shortDesc.equals("Police")) {
            uri = "tel:" + AccidentProcedure.accidentProcedure.policeNumber;
            callButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_police_cap_black));
        } else {
            uri = "tel:" + AccidentProcedure.accidentProcedure.emergencyNumber;
            callButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_ambulance_black));
        }

        questionText.setText(page.question);
        page.speak(getContext(), Locale.ENGLISH);

        webView.loadData(page.description, "text/html; charset=utf-8", "utf-8");
        webView.loadUrl("file:///android_asset/pages/" + pageId + "/index.html");

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivityForResult(intent, 70);
                createOverlay();
            }
        });
        mListener.onPageLoad(page);

        return view;
    }

    public void createOverlay() {
        //SystemOverlay.requestSystemAlertPermission(getActivity()); // TODO: 12.11.2016 call in settings
        String[] locationName = Geolocation.getLocationName(getContext(), Geolocation.actualLocation);
        SystemOverlay.createPhoneOverlay(getContext(), locationName[0], locationName[1], locationName[2]);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PageFragmentListener) {
            mListener = (PageFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PageFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 70) {
            SystemOverlay.destroyPhoneOverlay();
        }
    }
}
