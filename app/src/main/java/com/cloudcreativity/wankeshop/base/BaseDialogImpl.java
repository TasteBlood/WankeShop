package com.cloudcreativity.wankeshop.base;

import io.reactivex.disposables.Disposable;

/**
 * 这是基本的加载对话框和生命周期管理办法，属于Activity的附加功能
 */
public interface BaseDialogImpl {
    boolean addRxDestroy(Disposable disposable);
    void remove(Disposable disposable);
    /**
     * 显示加载对话框
     */
    void showProgress(String msg);
    /**
     * 隐藏加载对话框
     */
    void dismissProgress();
    /**
     * 显示权限出错的对话框，即就是登录超时
     */
    void showUserAuthOutDialog();
    /**
     * 显示获取照片和拍照的对话框
     */
    void openPictureDialog();
}
