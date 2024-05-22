package com.sss.spectrum;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.sss.VisualizerHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Visualizer visualizer;
    private MediaPlayer player;
    private static final int PERM_REQ_CODE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERM_REQ_CODE);
        } else {
            // 已经有权限，继续执行初始化
            okey();
        }

    }

    // 请求权限结果的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERM_REQ_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被授予，继续执行初始化
                okey();
            } else {
                // 权限被拒绝
                Toast.makeText(this, "录音权限被拒绝", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void okey() {
        findViewById(R.id.switch_button).setOnClickListener(this);
        findViewById(R.id.switch_horizontal_energy).setOnClickListener(this);
        findViewById(R.id.switch_ai_voice).setOnClickListener(this);
        findViewById(R.id.switch_siri).setOnClickListener(this);
        findViewById(R.id.switch_slip_ball).setOnClickListener(this);
        findViewById(R.id.switch_hexagram_view).setOnClickListener(this);
        findViewById(R.id.switch_columnar).setOnClickListener(this);
        findViewById(R.id.switch_bessel).setOnClickListener(this);
        findViewById(R.id.switch_waveform).setOnClickListener(this);
        findViewById(R.id.switch_circle_round).setOnClickListener(this);
        findViewById(R.id.switch_speedometer).setOnClickListener(this);
        findViewById(R.id.switch_rotating_circle).setOnClickListener(this);
        findViewById(R.id.switch_grid_point).setOnClickListener(this);
        findViewById(R.id.switch_flower).setOnClickListener(this);
        findViewById(R.id.switch_ring).setOnClickListener(this);
        findViewById(R.id.switch_diffusion).setOnClickListener(this);
        findViewById(R.id.switch_wave).setOnClickListener(this);

        player = MediaPlayer.create(MainActivity.this, R.raw.demo_1);
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        player.setLooping(true);
        player.start();

        try {
            int mediaPlayerId = player.getAudioSessionId();
            if (visualizer == null) {
                visualizer = new Visualizer(mediaPlayerId);
            } else {
                visualizer.release();
            }
            //可视化数据的大小： getCaptureSizeRange()[0]为最小值，getCaptureSizeRange()[1]为最大值
            int captureSize = Visualizer.getCaptureSizeRange()[1];
            int captureRate = Visualizer.getMaxCaptureRate() * 3 / 4;

            visualizer.setCaptureSize(captureSize);
            visualizer.setDataCaptureListener(VisualizerHelper.getInstance().getDataCaptureListener(), captureRate, true, true);
            visualizer.setScalingMode(Visualizer.SCALING_MODE_NORMALIZED);
            visualizer.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_button:
                AppConstant.imageValue = ((Switch) findViewById(R.id.switch_button)).isChecked();
                break;
            case R.id.switch_horizontal_energy:
                startActivity(new Intent(this, HorizontalEnergyActivity.class));
                break;
            case R.id.switch_ai_voice:
                startActivity(new Intent(this, AiVoiceActivity.class));
                break;
            case R.id.switch_siri:
                startActivity(new Intent(this, SiriActivity.class));
                break;
            case R.id.switch_slip_ball:
                startActivity(new Intent(this, SlipBallActivity.class));
                break;
            case R.id.switch_hexagram_view:
                startActivity(new Intent(this, HexagramViewActivity.class));
                break;
            case R.id.switch_columnar:
                startActivity(new Intent(this, ColumnarActivity.class));
                break;
            case R.id.switch_bessel:
                startActivity(new Intent(this, BesselActivity.class));
                break;
            case R.id.switch_waveform:
                startActivity(new Intent(this, WaveActivity.class));
                break;
            case R.id.switch_circle_round:
                startActivity(new Intent(this, CircleRoundActivity.class));
                break;
            case R.id.switch_speedometer:
                startActivity(new Intent(this, SpeedometerActivity.class));
                break;
            case R.id.switch_rotating_circle:
                startActivity(new Intent(this, RotatingCircleActivity.class));
                break;
            case R.id.switch_grid_point:
                startActivity(new Intent(this, GridPointActivity.class));
                break;
            case R.id.switch_flower:
                startActivity(new Intent(this, ColorBubbleActivity.class));
                break;
            case R.id.switch_ring:
                startActivity(new Intent(this, AttachmentRingActivity.class));
                break;
            case R.id.switch_diffusion:
                startActivity(new Intent(this, DiffusionRingActivity.class));
                break;
            case R.id.switch_wave:
                startActivity(new Intent(this, WaveRingActivity.class));
                break;
            default:
        }
    }
}
