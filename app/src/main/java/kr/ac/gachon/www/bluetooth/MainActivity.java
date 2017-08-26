package kr.ac.gachon.www.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {
    // Debugging
    private static final String TAG = "Main";

    // 필요한 권한 여기서 설정 -> 여기 있는 것들 메니페스트 파일에도 등록해줘야 앱이 필요한 권한이 뭔지 OS가 인식함
    private static final String[] PERMISSIONS = new String[]{Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN};
    private static final int REQUEST_CODE = 0;

    private PermissionsChecker checker;

    public final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    // Intent request code
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // Layout
    private Button btn_Connect;
    private TextView txt_Result;

    private BluetoothService btService = null;


    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");

        setContentView(R.layout.activity_main);
        checker = new PermissionsChecker(this);


    }
    @Override
    protected void onResume() {
        super.onResume();

        if (checker.lacksPermissions(PERMISSIONS)) {
            // 권한 못 받았으면 권한 요청하는 액티비티 띄워줌
            PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
        }else{
            // 있으면 실행
            setBluetooth();
        }
    }

    // PermissionActivity 완료 후 결과 확인
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
        Log.d(TAG, "onActivityResult " + resultCode);

        switch (requestCode) {

            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {

                } else {

                    Log.d(TAG, "Bluetooth is not enabled");
                }
                break;
        }
    }

    // 실제 실행해야 하는 부분
    public void setBluetooth() {
        /** Main Layout **/
        btn_Connect = (Button) findViewById(R.id.btn_connect);
        txt_Result = (TextView) findViewById(R.id.txt_result);

        btn_Connect.setOnClickListener(this);

        // BluetoothService 클래스 생성
        if(btService == null) {
            btService = new BluetoothService(this, mHandler);
        }
    }

    @Override
    public void onClick(View v) {
        if(btService.getDeviceState()) {
            // 블루투스가 지원 가능한 기기일 때
            btService.enableBluetooth();
        } else {
            finish();
        }
    }

}
