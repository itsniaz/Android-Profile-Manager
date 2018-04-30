package app.niaz.com.profilemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    Intent pServiceIntent;
    public  static TextView profileMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pServiceIntent = new Intent(this,ProfileService.class);


        Button startBtn = (Button)findViewById(R.id.startBtn);
        Button stopBtn = (Button)findViewById(R.id.stopBtn);
        profileMode = (TextView)findViewById(R.id.profileMode);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(pServiceIntent);
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(pServiceIntent);
            }
        });
    }

}
