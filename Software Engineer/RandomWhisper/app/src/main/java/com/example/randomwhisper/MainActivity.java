package com.example.randomwhisper;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    int[] videos = {R.raw.video1, R.raw.video2, R.raw.video3, R.raw.video4};
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
        initActivity();
    }

    // Permisos e iniciar la vista
    private void initActivity() {
        previewView = findViewById(R.id.previewView);
        videoView = findViewById(R.id.videoView);

        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "OpenCV no se pudo cargar");
        } else {
            Log.d(TAG, "OpenCV se cargo correctamente");
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


    // Iniciamos la camara, la cual solo se mostrara la primera vez que iniciemos la app.
    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Error al obtener el proveedor de la camara", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    // Despues de iniciar la camara la bindeamos al previewview del layout
    @OptIn(markerClass = ExperimentalGetImage.class)
    private void bindPreview(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        Preview preview = new Preview.Builder().build();
        // Especificamos la camara frontal
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT).build(); 

        // Solo almacenamos la ultima asi el rendimiento es mejor
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();


        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), imageProxy -> {
            if (imageProxy != null) {
                // Nos aseguramos de que la imagen no sea null y la enviamos a comprobar en detecMotion
                Mat currentFrame = convertImageToMat(imageProxy.getImage());
                if (lastFrame != null) {
                    detectMotion(lastFrame, currentFrame);
                }
                // Clonamos el ultimo frame para poder comparar el siguiente
                // La clonamos para evitar que ambas variables apunten a la misma matriz
                lastFrame = currentFrame.clone();
            }
            imageProxy.close();
        });

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Esta es la camara que use el provider para recoger el movimiento
        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageAnalysis);
    }

    // Usamos un material de OpenCV para el procesamiento, el cual se divide en tres vectores YUV.
    private Mat convertImageToMat(Image image) {
        Image.Plane[] planes = image.getPlanes();

        // Obtenemos los planos de color del objeto Image.
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        // Obtenemos los buffers que contienen los datos de cada plano de color.
        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        // Se crea un array NV21 con el tamaño de la suma de los tres planos
        byte[] nv21 = new byte[ySize + uSize + vSize];

        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        // Se crea una matriz temporal para contener los valores YUV para despues transformarla
        Mat yuv = new Mat(image.getHeight() + image.getHeight() / 2, image.getWidth(), CvType.CV_8UC1);
        yuv.put(0, 0, nv21);

        // Transformamos la matriz YUV en una matriz BRG
        Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        Imgproc.cvtColor(yuv, mat, Imgproc.COLOR_YUV2BGR_NV21, 3);
        yuv.release();

        return mat;
    }

    private void detectMotion(Mat lastFrame, Mat currentFrame) {
        // Dos cuadros para el viejo y el nuevo y poder compararlos
        Mat grayLastFrame = new Mat();
        Mat grayCurrentFrame = new Mat();

        // Guardamos los valores en escala de grises
        Imgproc.cvtColor(lastFrame, grayLastFrame, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(currentFrame, grayCurrentFrame, Imgproc.COLOR_BGR2GRAY);

        // Se le aplica un filtro gaussiano para reducir el ruido de la imagen
        Imgproc.GaussianBlur(grayLastFrame, grayLastFrame, new Size(31, 31), 0);
        Imgproc.GaussianBlur(grayCurrentFrame, grayCurrentFrame, new Size(31, 31), 0);

        Mat frameDelta = new Mat();
        Core.absdiff(grayLastFrame, grayCurrentFrame, frameDelta);

        Mat thresholdFrame = new Mat();
        Imgproc.threshold(frameDelta, thresholdFrame, 25, 255, Imgproc.THRESH_BINARY);

        // Se le aplica otro filtro para convertir la imagen en blanco y negro para diferenciar mejor el movimiento
        double movement = Core.sumElems(thresholdFrame).val[0];
        double movementThreshold = 1e7;

        // Si el ultimo movimiento ha sido despues de 5 desde el anterior se carga otro video
        if (movement > movementThreshold && System.currentTimeMillis() - lastMotionTime > 5000) {
            lastMotionTime = System.currentTimeMillis();
            if (currentVideoIndex == -1) {
                Random random = new Random();
                currentVideoIndex = random.nextInt(videos.length);
            } else {
                currentVideoIndex = (currentVideoIndex + 1) % videos.length;
            }
            if (videoView.isPlaying()) {
                videoView.stopPlayback();
            }
            videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + videos[currentVideoIndex]));
            previewView.setVisibility(View.INVISIBLE);
            videoView.setVisibility(View.VISIBLE);
            videoView.start();

            // Cuando el video llega al final se queda pausado en el ultimo frame, esperando a que se vuelva a detectar movimiento
            videoView.setOnCompletionListener(mp -> {
                videoView.pause();
                videoView.seekTo(videoView.getDuration());
                videoView.stopPlayback();
            });
        }

        // Limpiamos todos los recursos
        grayLastFrame.release();
        grayCurrentFrame.release();
        frameDelta.release();
        thresholdFrame.release();
    }

    // Cuando al app se pausa la devolvemos al mismo punto que estaba en el onCreate
    @Override
    protected void onPause() {
        super.onPause();
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
            videoView.setVisibility(View.INVISIBLE);
            previewView.setVisibility(View.VISIBLE);
        }
    }

    // Cuando se vuelve a abrir la app simplemente se revisan los permisos,
    // ya que en el onPause ya habiamos reiniciado la app
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
