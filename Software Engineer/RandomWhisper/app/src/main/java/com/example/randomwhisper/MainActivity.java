package com.example.randomwhisper;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.view.PreviewView;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private VideoView videoView;
    int[] videos = {R.raw.video1, R.raw.video2, R.raw.video3, R.raw.video4,R.raw.video5};
    private int currentVideoIndex = -1;

    private PreviewView previewView;
    private Mat lastFrame;
    private ProcessCameraProvider cameraProvider;
    private long lastMotionTime = 0;

    private static final int REQUEST_CAMERA_PERMISSION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        previewView = findViewById(R.id.previewView);
        checkStuff();

    }

    private void checkStuff() {
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "OpenCV no se pudo cargar");
        } else {
            Log.d(TAG, "OpenCV se cargó correctamente");
        }

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(
                    this, new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    private boolean allPermissionsGranted() {
        return ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this,
                        "Los permisos no fueron concedidos.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();  // Actualiza esta línea
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Error al obtener el proveedor de la cámara", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    private void bindPreview(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT).build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), imageProxy -> {
            Log.d(TAG, "Inicio del análisis de la imagen");
            if (imageProxy != null) {
                Mat currentFrame = convertImageToMat(imageProxy.getImage());
                Log.d(TAG, "currentFrame es " + (currentFrame == null ? "null" : "no null"));
                if (lastFrame != null) {
                    Log.d(TAG, "Llamando a detectMotion");
                    detectMotion(lastFrame, currentFrame);
                }
                lastFrame = currentFrame.clone();
            }
            imageProxy.close();  // Añade esta línea para cerrar el ImageProxy
        });

        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageAnalysis);
    }

    private Mat convertImageToMat(Image image) {
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];

        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        Mat yuv = new Mat(image.getHeight() + image.getHeight() / 2, image.getWidth(), CvType.CV_8UC1);
        yuv.put(0, 0, nv21);

        Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        Imgproc.cvtColor(yuv, mat, Imgproc.COLOR_YUV2BGR_NV21, 3);
        yuv.release();

        return mat;
    }

    private void detectMotion(Mat lastFrame, Mat currentFrame) {
        Mat grayLastFrame = new Mat();
        Mat grayCurrentFrame = new Mat();
        Imgproc.cvtColor(lastFrame, grayLastFrame, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(currentFrame, grayCurrentFrame, Imgproc.COLOR_BGR2GRAY);

        Imgproc.GaussianBlur(grayLastFrame, grayLastFrame, new Size(31, 31), 0);
        Imgproc.GaussianBlur(grayCurrentFrame, grayCurrentFrame, new Size(31, 31), 0);

        Mat frameDelta = new Mat();
        Core.absdiff(grayLastFrame, grayCurrentFrame, frameDelta);

        Mat thresholdFrame = new Mat();
        Imgproc.threshold(frameDelta, thresholdFrame, 25, 255, Imgproc.THRESH_BINARY);

        double movement = Core.sumElems(thresholdFrame).val[0];
        double movementThreshold = 1e7;
        if (movement > movementThreshold && System.currentTimeMillis() - lastMotionTime > 2000) {
            lastMotionTime = System.currentTimeMillis();
            videoView = findViewById(R.id.videoView);
            if (currentVideoIndex == -1) {
                // Si es el primer movimiento detectado, elige un video aleatorio
                Random random = new Random();
                currentVideoIndex = random.nextInt(videos.length);
            } else {
                // Si ya se había reproducido un video antes, cambia al siguiente video (cíclicamente)
                currentVideoIndex = (currentVideoIndex + 1) % videos.length;
            }
            // Detén la reproducción actual, si la hay
            if (videoView.isPlaying()) {
                videoView.stopPlayback();
            }
            // Configura el nuevo video a reproducir
            videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + videos[currentVideoIndex]));
            videoView.start();
            videoView.setOnCompletionListener(mp -> {
                videoView.pause();
                videoView.seekTo(videoView.getDuration());
            });
        }

        grayLastFrame.release();
        grayCurrentFrame.release();
        frameDelta.release();
        thresholdFrame.release();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(
                    this, new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }
}
