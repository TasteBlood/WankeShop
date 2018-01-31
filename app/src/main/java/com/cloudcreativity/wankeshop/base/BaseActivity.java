package com.cloudcreativity.wankeshop.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cloudcreativity.wankeshop.utils.LogUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 这是Activity的基类，将retrofit的生命周期绑定在活动的生命周期上面
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseDialogImpl{
    private CompositeDisposable disposableDestroy;

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

    }

    /**
     * 关闭加载框
     */
    @Override
    public void dismissProgress() {

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
}
