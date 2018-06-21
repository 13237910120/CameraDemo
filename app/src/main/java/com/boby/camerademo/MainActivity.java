package com.boby.camerademo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView tv_deviceInfo;
    private View openCamera2;

    public static int PERMISSION_REQ = 0x123456;
    private String[] mPermission = new String[] {
            Manifest.permission.CAMERA,//相机
            Manifest.permission.WRITE_EXTERNAL_STORAGE,//读写
            Manifest.permission.RECORD_AUDIO//录像
    };
    private List<String> mRequestPermission = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_deviceInfo=findViewById(R.id.tv_deviceInfo);
        openCamera2=findViewById(R.id.openCamera2);
        if(checkAndRequestPermissions()){
            init();
        }


    }

    void init(){
        if(checkCameraHardware(this)){
           int cameraNum= Camera.getNumberOfCameras();
           if(cameraNum>1){
               openCamera2.setVisibility(View.VISIBLE);
           }
            tv_deviceInfo.setText("有摄像头数量: "+cameraNum);
        }else {
            tv_deviceInfo.setText("设备没有摄像头");
        }
    }

    /** 判断设备是否有相机 */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 版本兼容
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            return;
        }
        if (requestCode == PERMISSION_REQ) {
            for (int i = 0; i < grantResults.length; i++) {
                for (String one : mPermission) {
                    if (permissions[i].equals(one) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        mRequestPermission.remove(one);
                    }
                }
            }
            if(mRequestPermission.isEmpty()){
                //已经获取所有的权限
                init();
            }
        }
    }

    public void openCamera1(View view){
        Intent intent1=new Intent(this,CameraActivity.class);
        intent1.putExtra(CameraActivity.CAMERA_INDEX,0);
        startActivity(intent1);
    }
    public void openCamera2(View view){
        Intent intent1=new Intent(this,CameraActivity.class);
        intent1.putExtra(CameraActivity.CAMERA_INDEX,1);
        startActivity(intent1);
    }

    /** 校对请求权限 **/
    boolean checkAndRequestPermissions(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            for (String one : mPermission) {
                if (PackageManager.PERMISSION_GRANTED != this.checkPermission(one, Process.myPid(), Process.myUid())) {
                    mRequestPermission.add(one);
                }
            }
            if (!mRequestPermission.isEmpty()) {
                this.requestPermissions(mRequestPermission.toArray(new String[mRequestPermission.size()]), PERMISSION_REQ);
                return false;
            }
        }
        return true;
    }


}
