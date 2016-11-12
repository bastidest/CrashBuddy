package hackatum.de.checkcrash.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.Locale;

import hackatum.de.checkcrash.R;
import hackatum.de.checkcrash.models.AccidentProcedure;
import hackatum.de.checkcrash.models.Page;

import static android.content.Context.MODE_PRIVATE;

public class ButtonFragment extends Fragment {
    private static final String ARG_PAGE_ID = "pageid";
    private String pageId;

    private PageFragmentListener mListener;

    public ButtonFragment() {
        // Required empty public constructor
    }

    public static ButtonFragment newInstance(String pageId) {
        ButtonFragment fragment = new ButtonFragment();
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
        View view = inflater.inflate(R.layout.fragment_button, container, false);

        TextView questionTextView = (TextView) view.findViewById(R.id.text_question);
        WebView webView = (WebView) view.findViewById(R.id.web_view);

        Page page = AccidentProcedure.accidentProcedure.pages.get(pageId);

        String question = page.question;
        String description = page.description;
        question = replaceHandlebar(question);
        description = replaceHandlebar(description);

        page.speak(getContext(), Locale.ENGLISH, question + "." + description);

        questionTextView.setText(question);

        webView.loadData(page.description, "text/html; charset=utf-8", "utf-8");
        webView.loadUrl("file:///android_asset/pages/" + pageId + "/index.html");

        mListener.onPageLoad(page);

        return view;
    }

    private String replaceHandlebar(String question) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("settings", MODE_PRIVATE);
        String firstaid = sharedPreferences.getString("firstaid", "");
        String triangle = sharedPreferences.getString("warningtriangle", "");
        question = question.replace("{{positon.triangle}}", triangle);
        return question.replace("{{positon.firstaid}}", firstaid);
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
}
