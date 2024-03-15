package com.example.hevccompressionlibrary;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import com.arthenica.mobileffmpeg.FFmpeg;

public class CompressionManager {

    public static void compressVideo(String inputPath, String outputPath, CompressionListener listener) {
        String[] cmd = {
                "-i", inputPath,
                "-c:v", "lib265",
                "-b:v", "500k",
                "-preset", "ultrafast",
                "-strict", "experimental",
                outputPath
        };

        FFmpeg.executeAsync(cmd, (executionId, returnCode) -> {
            if (returnCode == RETURN_CODE_SUCCESS) {
                listener.onCompressionSuccess(outputPath);
            } else {
                listener.onCompressionFailed("Compression failed with error code: " + returnCode);
            }
        });
    }

    public interface CompressionListener {
        void onCompressionSuccess(String outputPath);

        void onCompressionFailed(String errorMessage);
    }
}



