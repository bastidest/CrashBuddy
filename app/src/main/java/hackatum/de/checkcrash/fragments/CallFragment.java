package hackatum.de.checkcrash.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hackatum.de.checkcrash.R;
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
        mListener.onPageLoad(page);

        return view;
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
