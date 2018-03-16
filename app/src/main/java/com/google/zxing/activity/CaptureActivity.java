package com.google.zxing.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.utils.QRCodeDecoder;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.camera.CameraManager;
import com.google.zxing.decoding.CaptureActivityHandler;
import com.google.zxing.decoding.InactivityTimer;
import com.google.zxing.view.ViewfinderView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Vector;


/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class CaptureActivity extends Activity implements Callback {

    private static final int REQUEST_CODE_SCAN_GALLERY = 100;

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private ImageView back;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private ProgressDialog mProgress;
    private String photo_path;
    private Bitmap scanBitmap;
    //	private Button cancelScanButton;
    public static final int RESULT_CODE_QR_SCAN = 0xA1;
    public static final String INTENT_EXTRA_KEY_QR_SCAN = "qr_scan_result";

    private boolean isOpen = false;
    private MyResultHandler resultHandler = new MyResultHandler(this);
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        CameraManager.init(getApplication());
        viewfinderView = findViewById(R.id.viewfinder_content);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        ((CheckBox)findViewById(R.id.btn_open)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    CameraManager.get().openFlash();
                }else{
                    CameraManager.get().closeFlash();
                }
            }
        });
        findViewById(R.id.iv_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开手机中的相册
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, REQUEST_CODE_SCAN_GALLERY);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.scanner_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.scan_local:
//                //打开手机中的相册
//                Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); //"android.intent.action.GET_CONTENT"
//                innerIntent.setType("image/*");
//                Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
//                this.startActivityForResult(wrapperIntent, REQUEST_CODE_SCAN_GALLERY);
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SCAN_GALLERY:
                    //获取选中图片的路径
                    //好像是android多媒体数据库的封装接口，具体的看Android文档
                    if("file".equals(data.getData().getScheme())&&data.getType().contains("image")){
                        //处理特殊情况
                       photo_path = data.getData().getPath();
                    }else{
                        //获取图片的路径：
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = managedQuery(data.getData(), proj, null, null, null);
                        //按我个人理解 这个是获得用户选择的图片的索引值
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        //将光标移至开头 ，这个很重要，不小心很容易引起越界
                        cursor.moveToFirst();
                        //最后根据索引值获取图片路径
                        photo_path = cursor.getString(column_index);
                    }
                    mProgress = new ProgressDialog(CaptureActivity.this);
                    mProgress.setMessage("正在扫描...");
                    mProgress.setCancelable(false);
                    mProgress.show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Result result = scanningImage(photo_path);
                            mProgress.dismiss();
                            if (result != null) {
                                Message m = resultHandler.obtainMessage();
                                m.what = result_ok;
                                m.obj = result.getText();
                                resultHandler.sendMessage(m);
                            } else {
                                Message m = resultHandler.obtainMessage();
                                m.what = result_error;
                                resultHandler.sendMessage(m);
                            }
                        }
                    }).start();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 扫描二维码图片的方法
     * @param path 文件路径
     * @return 扫描结果
     */
    public Result scanningImage(String path) {
        if(TextUtils.isEmpty(path)){
            return null;
        }
        return QRCodeDecoder.syncDecodeQRCode(path);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = findViewById(R.id.scanner_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        //FIXME
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(INTENT_EXTRA_KEY_QR_SCAN, resultString);
            System.out.println("sssssssssssssssss scan 0 = " + resultString);
            // 不能使用Intent传递大于40kb的bitmap，可以使用一个单例对象存储这个bitmap
//            bundle.putParcelable("bitmap", barcode);
//            Logger.d("saomiao",resultString);
            resultIntent.putExtras(bundle);
            this.setResult(RESULT_CODE_QR_SCAN, resultIntent);
        }
        CaptureActivity.this.finish();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    private final static int result_ok = 100;
    private final static int result_error = 101;
    private static class MyResultHandler extends Handler{
        private final WeakReference<Activity> activityWeakReference;
        private MyResultHandler(Activity activity){
            this.activityWeakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Activity activity = activityWeakReference.get();
            if(activity!=null&&!activity.isFinishing()){
                switch (msg.what){
                    case result_ok:
                        Intent resultIntent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString(INTENT_EXTRA_KEY_QR_SCAN ,msg.obj.toString());
                        resultIntent.putExtras(bundle);
                        activity.setResult(RESULT_CODE_QR_SCAN, resultIntent);
                        activity.finish();
                        break;
                    case result_error:
                        ToastUtils.showShortToast(activity,"扫描失败");
                        break;
                }
            }
        }
    }

}