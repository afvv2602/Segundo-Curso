package com.example.randomwhisper;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private PreviewView previewView;
    private Mat lastFrame; // Almacena el último fotograma para comparar

    private long lastMotionTime = 0;

    private static final int REQUEST_CAMERA_PERMISSION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        previewView = findViewById(R.id.previewView);

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

    // Inicia la cámara
    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Error al obtener el proveedor de la cámara", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    // Vincula la vista previa (preview) con el proveedor de la cámara

    @OptIn(markerClass = ExperimentalGetImage.class)
    private void bindPreview(ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT).build(); // Utiliza la cámara frontal

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), imageProxy -> {
            if (imageProxy!=null){
                Mat currentFrame = convertImageToMat(imageProxy.getImage()); // Convierte la imagen a Mat de OpenCV
                if (lastFrame != null) {
                    detectMotion(lastFrame, currentFrame); // Detecta el movimiento comparando los fotogramas
                }
                lastFrame = currentFrame; // Guarda el fotograma actual para la siguiente comparación
            }
        });

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageAnalysis);
    }

    // Convierte la imagen en objeto Mat de OpenCV
    private Mat convertImageToMat(Image image) {
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];

        // Las componentes U y V están intercambiadas
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

    // Compara el fotograma actual con el último para detectar movimiento
    private void detectMotion(Mat lastFrame, Mat currentFrame) {
        // Convierte los fotogramas a escala de grises
        Mat grayLastFrame = new Mat();
        Mat grayCurrentFrame = new Mat();
        Imgproc.cvtColor(lastFrame, grayLastFrame, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(currentFrame, grayCurrentFrame, Imgproc.COLOR_BGR2GRAY);

        // Difumina las imágenes para reducir el ruido
        Imgproc.GaussianBlur(grayLastFrame, grayLastFrame, new Size(15, 15), 0);  // <--- Cambia el tamaño del kernel
        Imgproc.GaussianBlur(grayCurrentFrame, grayCurrentFrame, new Size(15, 15), 0);  // <--- Cambia el tamaño del kernel

        // Calcula la diferencia absoluta entre el fotograma actual y el último
        Mat frameDelta = new Mat();
        Core.absdiff(grayLastFrame, grayCurrentFrame, frameDelta);

        // Aplica umbral a la imagen de la diferencia (el segundo parámetro puede ajustarse según necesidad)
        Mat thresholdFrame = new Mat();
        Imgproc.threshold(frameDelta, thresholdFrame, 15, 150, Imgproc.THRESH_BINARY);  // <--- Reduce el umbral

        // Verifica si hay movimiento
        double movement = Core.sumElems(thresholdFrame).val[0];
        if(movement > 0 && System.currentTimeMillis() - lastMotionTime > 50) {  // <--- Añade la verificación del tiempo
            Log.i(TAG, "@@@@@@Se detectó movimiento");
            lastMotionTime = System.currentTimeMillis();  // <--- Actualiza la última vez que se detectó movimiento

            // Muestra un Toast
            runOnUiThread(() -> Toast.makeText(MainActivity.this, "Se detectó movimiento", Toast.LENGTH_SHORT).show());
        }

        // Libera los recursos
        grayLastFrame.release();
        grayCurrentFrame.release();
        frameDelta.release();
        thresholdFrame.release();
    }
}
