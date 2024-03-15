package com.example.hevccompressionlibrary;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.arthenica.mobileffmpeg.FFmpeg;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FFmpeg.execute(String.valueOf(this));
    }
}

public class MainActivity extends AppCompatActivity {

    private static final int PICK_VIDEO_REQUEST = 1;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnChooseVideo = findViewById(R.id.btnChooseVideo);
        Button btnCompress = findViewById(R.id.btnCompress);
        videoView = findViewById(R.id.videoView);

        btnChooseVideo.setOnClickListener(view -> chooseVideo());
        btnCompress.setOnClickListener(view -> compressVideo());
    }

    private void chooseVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri videoUri = data.getData();
            videoView.setVideoURI(videoUri);
        }
    }

    private void compressVideo() {
        Uri videoUri = videoView.getVideoURI();
        String inputPath = VideoUtils.getRealPathFromURI(this, videoUri);
        String outputPath = getExternalCacheDir().getPath() + "/compressed_video.mp4";

        CompressionManager.compressVideo(inputPath, outputPath, new CompressionManager.CompressionListener() {
            private Object Toast;

            @Override
            public void onCompressionSuccess(String outputPath) {
                videoView.setVideoPath(outputPath);
                videoView.start();
            }

            @Override
            public void onCompressionFailed(String errorMessage) {
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}


