package finiexshiper.quoccuong.app.finiexshiper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import helper.ShipperApplication;
import helper.TrackerService;
import http.GetUserInfoTask;
import model.UserInfo;

public class MainActivity extends AppCompatActivity {
    private String NAME = "ABC";
    private String EMAIL = "abc.123@make_sence.com";
    private Bitmap AVATAR;
    private UserInfo userInfo = new UserInfo();
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!((ShipperApplication) getApplicationContext()).isLogined()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            stopService(new Intent(this, TrackerService.class));
            return;
        }
        startService(new Intent(this, TrackerService.class));
        getDataIntent();
        setContentView(R.layout.activity_main);
        getUserInfo();
        textView = (TextView) findViewById(R.id.tvtext);
        textView.setText(userInfo.getFullName());
    }

    private void getDataIntent() {
        try {
            Intent intent = getIntent();
            NAME = intent.getStringExtra("username");
            EMAIL = intent.getStringExtra("email");
            AVATAR = (Bitmap) intent.getParcelableExtra("avatar");
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void getUserInfo() {
        (new GetUserInfoTask(this)).execute(((ShipperApplication) getApplicationContext()).getAccessToken());
    }
}
