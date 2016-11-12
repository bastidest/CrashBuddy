package hackatum.de.checkcrash;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {


    Button grantPermissions;
    CheckBox tts;
    EditText firstAid;
    EditText warningTriangle;


    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("settings", MODE_PRIVATE);


        grantPermissions = (Button) findViewById(R.id.button2);
        tts = (CheckBox) findViewById(R.id.checkBox);
        firstAid = (EditText) findViewById(R.id.editText2);
        warningTriangle = (EditText) findViewById(R.id.editText3);


        tts.setChecked(prefs.getBoolean("tts", true));
        firstAid.setText(prefs.getString("firstaid", ""));
        warningTriangle.setText(prefs.getString("warningtriangle", ""));


        grantPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SystemOverlay.requestSystemAlertPermission(SettingsActivity.this);
            }
        });

        tts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("tts", b);
                editor.commit();
            }
        });


        firstAid.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("firstaid", firstAid.getText().toString());
                editor.commit();
                return false;
            }
        });
        warningTriangle.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("warningtriangle", warningTriangle.getText().toString());
                editor.commit();
                return false;
            }
        });

    }


}
