package app.niaz.com.profilemanager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.M)
public class MainActivity extends Activity {
    Intent pServiceIntent;
    public  static TextView profileMode;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //For silent mode access in  API>23
        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }

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
