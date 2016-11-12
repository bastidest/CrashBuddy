package hackatum.de.checkcrash.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import hackatum.de.checkcrash.R;
import hackatum.de.checkcrash.models.AccidentProcedure;
import hackatum.de.checkcrash.models.Page;


public class CameraFragment extends Fragment {

    private static final String ARG_PAGE_ID = "pageid";
    Button cameraButton;
    GridLayout layout;
    TextView desc;
    private String pageId;

    private PageFragmentListener mListener;



    public CameraFragment() {
        // Required empty public constructor
    }


    public static CameraFragment newInstance(String pageId) {
        CameraFragment fragment = new CameraFragment();
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

    /**
     * Saves a bitmap to SDCARD with the current timestamp
     *
     * @param bmp
     */
    private void saveBmp(Bitmap bmp) {
        String sdcard = Environment.getExternalStorageDirectory().toString();
        File saveDir = new File(sdcard + "/accidents");
        saveDir.mkdirs();

        SimpleDateFormat s = new SimpleDateFormat("dd.MM.yyyy-hh:mm:ss");
        String format = s.format(new Date());
        File file = new File(saveDir, "image " + format + ".jpg");
        try {
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Receive photo from the camera app
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 69 && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(500, 550);

            params.setMargins(0, 0, 0, 100);

            ImageView iv = new ImageView(getActivity());
            iv.setLayoutParams(params);
            iv.setImageBitmap(imageBitmap);
            layout.addView(iv);

            saveBmp(imageBitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        cameraButton = (Button) view.findViewById(R.id.button5);
        layout = (GridLayout) view.findViewById(R.id.image_grid);
        desc = (TextView) view.findViewById(R.id.textView10);

        if (pageId != null) {
            desc.setText(AccidentProcedure.accidentProcedure.pages.get(pageId).question);
        }

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(CameraFragment.this.getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 69);
                }
            }
        });

        Page page = AccidentProcedure.accidentProcedure.pages.get(pageId);
        page.speak(getContext(), Locale.ENGLISH);
        desc.setText(page.question);


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
