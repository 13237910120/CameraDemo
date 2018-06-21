package com.boby.camerademo;

import android.hardware.Camera;

/**
 * Created by boby on 2018/6/8 0008.
 */
public class CameraManager {
    /** 安全的获取相机实例 */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // 尝试获得一个实例
        }
        catch (Exception e){
            // 相机不可用 (正在被使用或者不存在)
        }
        return c; // returns null if camera is unavailable
    }

    /** 安全的获取相机实例
     * @param camera 哪个相机(有多个相机)
     * */
    public static Camera getCameraInstance(int camera){
        Camera c = null;
        try {
            c = Camera.open(camera); // 尝试获得一个实例
        }
        catch (Exception e){
            // 相机不可用 (正在被使用或者不存在)
        }
        return c; // returns null if camera is unavailable
    }

}
