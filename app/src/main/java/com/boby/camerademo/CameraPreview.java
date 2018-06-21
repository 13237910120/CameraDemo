package com.boby.camerademo;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/** 显示相机图像的类 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private String TAG=this.getClass().getSimpleName();;
    private SurfaceHolder mHolder;
    private Camera mCamera;


    public CameraPreview(Context context) {
        super(context, null);
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
    }

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // Surface 已经创建了, camera开始获取图像
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // 如果界面改变或手机旋转，注意和这个方法
        if (mHolder.getSurface() == null){
          // preview surface does not exist
          return;
        }
        // 确保在调整或重新格式化预览之前停止预览。
        try {
            mCamera.stopPreview();
        } catch (Exception e){
          // ignore: tried to stop a non-existent preview
        }

        // 如果您想为您的相机预览设置一个特定的大小，请在这里设置它。
        // 设置预览大小时，必须使用getsupportedpreviewsize()中的值。不要在setPreviewSize()方法中设置任意值。


        // 重新开始绘制图像
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}