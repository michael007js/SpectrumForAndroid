package com.sss.spectrum;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioCapabilities;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.DefaultAudioSink;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.sss.VisualizerHelper;
import com.sss.processor.FFTAudioProcessor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ExoPlayer player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        okey();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x001A) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                okey();
            } else {
                Toast.makeText(this, "请授予读取权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void okey() {
        findViewById(R.id.switch_horizontal_energy).setOnClickListener(this);
        findViewById(R.id.switch_ai_voice).setOnClickListener(this);
        findViewById(R.id.switch_siri).setOnClickListener(this);
        findViewById(R.id.switch_slip_ball).setOnClickListener(this);
        findViewById(R.id.switch_hexagram_view).setOnClickListener(this);
        findViewById(R.id.switch_columnar).setOnClickListener(this);
        findViewById(R.id.switch_kugou_column).setOnClickListener(this);
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


        play();
    }

    void play() {
        if (player == null) {
            FFTAudioProcessor fftAudioProcessor = new FFTAudioProcessor();
            fftAudioProcessor.setFftListener(VisualizerHelper.getInstance().getFftListener());
            AudioProcessor[] audioProcessors = new AudioProcessor[]{fftAudioProcessor};
            player = new SimpleExoPlayer.Builder(MainActivity.this, new RenderersFactory() {
                @Override
                public Renderer[] createRenderers(Handler eventHandler, VideoRendererEventListener videoRendererEventListener, AudioRendererEventListener audioRendererEventListener, TextOutput textRendererOutput, MetadataOutput metadataRendererOutput) {
                    MediaCodecAudioRenderer audioRenderer = new MediaCodecAudioRenderer(
                            MainActivity.this,
                            MediaCodecSelector.DEFAULT,
                            eventHandler,
                            audioRendererEventListener,
                            new DefaultAudioSink(AudioCapabilities.getCapabilities(MainActivity.this), audioProcessors)
                    );
                    return new Renderer[]{audioRenderer};
                }
            })
                    .setTrackSelector(new DefaultTrackSelector(MainActivity.this))
                    .build();
            player.addListener(new Player.Listener() {
                @Override
                public void onPlaybackStateChanged(int playbackState) {
                    if (playbackState == Player.STATE_ENDED) {
                        play();
                    }
                }
            });
        }
        Uri uri = RawResourceDataSource.buildRawResourceUri(R.raw.demo);
        MediaItem mediaItem = MediaItem.fromUri(uri);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.switch_kugou_column:
                startActivity(new Intent(this, KugouColumnActivity.class));
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
