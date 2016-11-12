package hackatum.de.checkcrash.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hackatum.de.checkcrash.R;
import hackatum.de.checkcrash.models.AccidentProcedure;
import hackatum.de.checkcrash.models.Page;

public class ButtonFragment extends Fragment {
    private static final String ARG_PAGE_ID = "pageid";
    private String pageId;

    private PageFragmentListener mListener;
    private TextView questionTextView;

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
        View root = inflater.inflate(R.layout.fragment_button, container, false);

        questionTextView = (TextView) root.findViewById(R.id.text_question);

        Page page = AccidentProcedure.accidentProcedure.pages.get(pageId);

        String question = page.question;
        questionTextView.setText(question);

        mListener.onButtonsLoad(page.answers);

        return root;
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
