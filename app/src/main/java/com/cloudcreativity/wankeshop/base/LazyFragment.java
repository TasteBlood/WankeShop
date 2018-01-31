package com.cloudcreativity.wankeshop.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 懒加载的Fragment
 */
public abstract class LazyFragment extends Fragment implements BaseDialogImpl{
    private boolean isViewCreated;
    private boolean isViewVisible;
    private boolean isDataLoaded;
    protected Activity context;
    private CompositeDisposable disposableDestroy;//这是网络请求RxJava需要的东西，跟生命周期绑定的

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        if(disposableDestroy!=null){
            throw new IllegalStateException("onCreate called multiple times");
        }
        disposableDestroy = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposableDestroy == null) {
            throw new IllegalStateException(
                    "onDestroy called multiple times or onCreate not called");
        }
        disposableDestroy.dispose();
        disposableDestroy = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.isViewCreated = true;
        if(isViewVisible&&!isDataLoaded){
            initialLoadData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isViewVisible = isVisibleToUser;
        if(isVisibleToUser&&isViewCreated&&!isDataLoaded)
            initialLoadData();
    }

    /**
     * 初始化记载数据
     */
    public abstract void initialLoadData();

    /**
     * 数据记载成功就停止下次的加载
     */
    protected void initialLoadDataSuccess(){
        this.isDataLoaded = true;
    }


    //这是BaseDialogImpl的方法
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

    @Override
    public void showProgress(String msg) {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void showUserAuthOutDialog() {
        //在这里显示用户权限出现的问题

    }

    @Override
    public void openPictureDialog() {

    }
}
