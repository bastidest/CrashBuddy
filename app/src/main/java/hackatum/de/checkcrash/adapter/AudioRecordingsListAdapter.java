package hackatum.de.checkcrash.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import hackatum.de.checkcrash.R;
import hackatum.de.checkcrash.models.AudioRecording;

public class AudioRecordingsListAdapter extends ArrayAdapter<AudioRecording> {

    List<AudioRecording> listModel;
    Context context;

    public AudioRecordingsListAdapter(Context context, int resource, int textViewResourceId, List<AudioRecording> objects) {
        super(context, resource, textViewResourceId, objects);
        listModel = objects;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.record_list_layout, parent, false);

        TextView timestamp = (TextView) view.findViewById(R.id.textView11);
        Button play = (Button) view.findViewById(R.id.button7);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(listModel.get(position).audioPath);
                listModel.get(position).play();
            }
        });

        timestamp.setText(listModel.get(position).timestamp);

        return view;
    }
}
