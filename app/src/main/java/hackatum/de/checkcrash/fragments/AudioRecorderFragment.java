package hackatum.de.checkcrash.fragments;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import hackatum.de.checkcrash.R;
import hackatum.de.checkcrash.adapter.AudioRecordingsListAdapter;
import hackatum.de.checkcrash.models.AccidentProcedure;
import hackatum.de.checkcrash.models.AudioRecording;
import hackatum.de.checkcrash.models.Page;


public class AudioRecorderFragment extends Fragment {

    private static final String ARG_PAGE_ID = "pageid";
    Button record;
    ListView list;
    MediaRecorder mediaRecorder;
    AudioRecording actualRecording;
    private PageFragmentListener mListener;
    private String pageId;

    public AudioRecorderFragment() {
        // Required empty public constructor
    }

    public static AudioRecorderFragment newInstance(String pageId) {
        AudioRecorderFragment fragment = new AudioRecorderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PAGE_ID, pageId);
        fragment.setArguments(args);
        return fragment;
    }

    private AudioRecording prepareMediaRecorder() {
        AudioRecording recording = new AudioRecording();
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        SimpleDateFormat s = new SimpleDateFormat("dd.MM.yyyy-hh:mm:ss");
        String date = s.format(new Date());
        String sdcard = Environment.getExternalStorageDirectory().toString();
        File saveDir = new File(sdcard + "/accidents");
        saveDir.mkdirs();
        String save = saveDir.toString() + "/" + date + ".3gp";
        recording.audioPath = save;
        recording.timestamp = date;
        mediaRecorder.setOutputFile(save);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recording;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audio_recorder, container, false);

        list = (ListView) view.findViewById(R.id.listView);
        final AudioRecordingsListAdapter adapter = new AudioRecordingsListAdapter(getContext(), 0, 0, AudioRecording.recordings);
        list.setAdapter(adapter);

        actualRecording = prepareMediaRecorder();

        record = (Button) view.findViewById(R.id.button6);
        record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    mediaRecorder.start();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mediaRecorder.stop();
                    AudioRecording.recordings.add(actualRecording);
                    adapter.notifyDataSetChanged();
                    actualRecording = prepareMediaRecorder();
                }
                return false;
            }
        });

        Page page = AccidentProcedure.accidentProcedure.pages.get(pageId);
        page.speak(getContext(), Locale.ENGLISH);

        TextView desc = (TextView) view.findViewById(R.id.textView9);
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
