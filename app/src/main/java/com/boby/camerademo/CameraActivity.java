package com.boby.camerademo;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

/**
 * 自定义相机
 */
public class CameraActivity extends AppCompatActivity {
    public static final String CAMERA_INDEX="CameraIndex";
    private String TAG = this.getClass().getSimpleName();
    private Camera mCamera;
    private CameraPreview mPreview;
    private Camera.PictureCallback mPicture;
    private MediaRecorder mMediaRecorder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.activity_camera);
        init();
    }

    void init() {
        // 获取相机实例
        int index=getIntent().getIntExtra(CAMERA_INDEX,0);
        mCamera = CameraManager.getCameraInstance(index);
        CameraUtill.setCameraDisplayOrientation(this,index,mCamera);

        // 创建预览显示实例
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        //拍照回调
        mPicture = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(final byte[] data, Camera camera) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            File pictureFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyCamera");
                            if (!pictureFile.exists()) {
                                pictureFile.mkdir();
                            }
                            pictureFile = new File(pictureFile, System.currentTimeMillis() + ".jpg");
                            FileOutputStream fos = new FileOutputStream(pictureFile);
                            fos.write(data);
                            fos.close();
                        } catch (FileNotFoundException e) {
                            Log.d(TAG, "File not found: " + e.getMessage());
                        } catch (IOException e) {
                            Log.d(TAG, "Error accessing file: " + e.getMessage());
                        }
                        mPreview.post(new Runnable() {
                            @Override
                            public void run() {
                                mCamera.startPreview();
                            }
                        });
                    }
                }).start();

            }
        };
    }


    public void takePicture(View view) {
        if (mCamera != null) {
            mCamera.takePicture(null, null, mPicture);
          //  Preview will be stopped after the image is taken
            //拍照后会停止在拍照的页面，需要重新调用startPreview方法刷新页面，才可再次拍照

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }
}
