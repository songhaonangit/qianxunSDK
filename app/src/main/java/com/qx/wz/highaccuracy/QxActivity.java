package com.qx.wz.highaccuracy;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import com.qx.wz.common.bean.QxLocation;
import com.qx.wz.magic.location.Options;
import com.qx.wz.magic.location.QxLocationListener;
import com.qx.wz.magic.location.QxLocationManager;

/**
 * 千寻SDK调用类
 */

public class QxActivity extends AppCompatActivity implements QxLocationListener {

    private TextView mTextView;
    TextView tv_result;
    TextView status;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.test);
        tv_result = findViewById(R.id.tv_result);
        status = findViewById(R.id.status);
        /**
         * 登录千寻位置网官网的开发者管理控制台创建应用，成功后会自动生成该应用的AppKey和AppSerect；AppKey和AppSecret是应用的凭证信息，请妥善保管。详细信息请见千寻官网帮助文档。SDK使用需要使用AppKey、AppSecret、DeviceId、DeviceType。
         1. DeviceId和DeviceType联合组成设备唯一值，从而能够区分设备。在同一个AppKey下， 需保证这两个值联合唯一，一般情况同Appkey下DeviceType值是相同的；
         2. DeviceId 表示设备号。建议使用终端用户可以看到的值，系统生成的类似UUID值用户可能是看不见的。 建议使用用户可在产品上查到的值。后续比如用户报障，续费使用等问题都可方便的进行解决。 因此推荐使用印刷在终端设备上的SN、IMEI等值；
         3. DeviceType表示设备类型。 语义是用来区分设备关键型号。例如关键的传感器类型，可区分不同的产品值等。 要求填写有意义的值，如果是App集成推荐使用应用名称（字母）。如果是嵌入式终端智能产品推荐使用产品名称。 这个值可用于为不同产品设置不同的配置信息。
         */
        String appKey = "647323";
        String appSecret = "1e99c85889f241028a7a6559efd59e7363e9e9f105b1bc55f817364c9d8e36fc";
        String deviceId = "Qx11";
        String deviceType = "Hh";
        String serialPath = "/dev/ttyMT2";


        // 构造千寻SDK必须输入参数，context、appKey、appSecret、deviceType、deviceId是千寻SDK必须输入
        //使用千寻魔方数据provider使用QxLocationManager.DEVICE_PROVIDER，开发人员无设备仅是模拟测试使用QxLocationManager.MOCK_PROVIDER
        //硬件主板集成千寻魔方方案，serialPath不能为空，必须传入千寻魔方驱动设备节点路径（由硬件方案商提供）；通过USB线外接设备集成千寻魔方方案，serialPath为空或者不要设置即可。
        Options options = new Options.Builder()
                .context(getApplicationContext())
                .appKey(appKey)
                .appSecret(appSecret)
                .deviceId(deviceId)
                .deviceType(deviceType)
                .provider(QxLocationManager.DEVICE_PROVIDER)
                .serialPath(serialPath)
                .build();
        //切记，使用之前确保相关权限已经授权，这部分由集成方负责
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            QxLocationManager.getInstance().init(options);
        }
        // 启动千寻SDK，结果通过onLocationChanged返回
        QxLocationManager.getInstance().requestLocationUpdates(this, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 移除千寻SDK listener，如果当前listener是最后一个，则会自动关闭千寻SDK功能
        QxLocationManager.getInstance().removeUpdates(this);
        // 清除SDK内存缓存和释放资源
        QxLocationManager.getInstance().close();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onLocationChanged(final QxLocation qxLocation) {

        // 千寻SDK返回位置
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextView.setText("lat:" + qxLocation.getLatitude() + " lng:" + qxLocation.getLongitude());
                Log.e("tag", "lat:" + qxLocation.getLatitude() + " lng:" + qxLocation.getLongitude());
            }
        });
    }


    @Override
    public void onStatusChanged(final int i, Bundle bundle) {
        // 千寻SDK返回状态，状态码详细见
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("tag", "status:" + i);
                status.setText("status:" + i);
            }
        });
    }

    @Override
    public void onNmeaReceived(String s) {

       tv_result.setText(s);
        Log.e("tag", "onNmeaReceived:" + s);
    }
}
