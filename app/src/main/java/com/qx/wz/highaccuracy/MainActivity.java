package com.qx.wz.highaccuracy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * 申请权限页，如果你已经申请，请到MainActivity查看千寻SDK集成
 */
public class MainActivity extends AppCompatActivity {

    private final int REQUESTCODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // android 6.0及以上手机申请权限，如你已了解权限申请，直接跳到MainActivity看千寻SDK集成
        requestPermissions();
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUESTCODE) {
            boolean hasPermiss = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    hasPermiss = false;
                }
            }

            if (hasPermiss) {
                // 申请到这三个必要权限才能启动千寻SDK，否则直接启动千寻SDK，千寻SDK启动不成功
                Intent intent = new Intent(this, QxActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "没有权限，请点击允许", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
