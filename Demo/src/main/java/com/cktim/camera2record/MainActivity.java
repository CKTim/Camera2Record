package com.cktim.camera2record;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.cktim.camera2library.Camera2Config;
import com.cktim.camera2library.camera.Camera2RecordActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnOpenCamera2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnOpenCamera2=findViewById(R.id.btn_openCamera2);

        //配置Camera2相关参数，
        Camera2Config.RECORD_MAX_TIME = 10;
        Camera2Config.RECORD_MIN_TIME=2;
        Camera2Config.RECORD_PROGRESS_VIEW_COLOR=R.color.colorAccent;
        Camera2Config.PREVIEW_MAX_HEIGHT=1300;
        Camera2Config.PATH_SAVE_VIDEO= Environment.getExternalStorageDirectory().getAbsolutePath() + "/CameraV2222/";
        Camera2Config.PATH_SAVE_PIC= Environment.getExternalStorageDirectory().getAbsolutePath() + "/CameraV2222/CameraV22222222/";
        Camera2Config.ENABLE_CAPTURE=true;
        Camera2Config.ENABLE_CAPTURE=true;

        Camera2Config.ACTIVITY_AFTER_CAPTURE = Camera2RecordFinishActivity.class;

        btnOpenCamera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Camera2RecordActivity.start(MainActivity.this);
            }
        });
    }
}
