package com.cloudcreativity.wankeshop.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.databinding.LayoutProgressDialogBinding;
import com.cloudcreativity.wankeshop.utils.LogUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 这是Activity的基类，将retrofit的生命周期绑定在活动的生命周期上面
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseDialogImpl{
    private CompositeDisposable disposableDestroy;
    private Dialog progressDialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(disposableDestroy!=null){
            throw new IllegalStateException("onCreate called multiple times");
        }
        disposableDestroy = new CompositeDisposable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消显示的Toast
        ToastUtils.cancelToast();

        //销毁对话框，防止内存泄漏
        if(progressDialog!=null){
            dismissProgress();
        }

        if (disposableDestroy == null) {
            throw new IllegalStateException(
                    "onDestroy called multiple times or onCreate not called");
        }
        disposableDestroy.dispose();
        disposableDestroy = null;
    }

    @Override
    public boolean addRxDestroy(Disposable disposable) {
        if(disposable==null){
            throw new IllegalStateException("AddUtilDestroy should be called between onCreate and onDestroy");
        }
        disposableDestroy.add(disposable);
        return true;
    }

    @Override
    public void remove(Disposable disposable) {
        if(disposableDestroy!=null){
            disposableDestroy.remove(disposable);
        }
    }

    /**
     *
     * @param msg 显示加载框
     */
    @Override
    public void showProgress(String msg) {
        dialogMessage.set(msg);
        if(progressDialog!=null&&!progressDialog.isShowing()){
            progressDialog.show();
            return;
        }
        progressDialog = new Dialog(this, R.style.myProgressDialogStyle);
        LayoutProgressDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.layout_progress_dialog,null,false);
        binding.setDialog(this);
        progressDialog.setContentView(binding.getRoot());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dismissProgress();
            }
        });
        progressDialog.show();
    }

    /**
     * 关闭加载框
     */
    @Override
    public void dismissProgress() {
        if(progressDialog!=null&&progressDialog.isShowing())
            progressDialog.dismiss();
        progressDialog = null;
    }

    /**
     * 显示用户在其他地方登录
     */
    @Override
    public void showUserAuthOutDialog() {
        //在这里显示用户权限出现的问题

    }

    /**
     * 打开照片选择对话框
     */
    @Override
    public void openPictureDialog() {

    }

    /**
     *
     * @param message 消息
     *                显示错误的网络请求消息
     */
    @Override
    public void showRequestErrorMessage(String message) {
        ToastUtils.showShortToast(this,message);
    }
}
