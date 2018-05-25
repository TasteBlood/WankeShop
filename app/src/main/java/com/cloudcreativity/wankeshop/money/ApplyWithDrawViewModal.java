package com.cloudcreativity.wankeshop.money;

import android.databinding.ObservableField;
import android.text.TextUtils;
import android.view.View;

import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.ActivityApplyWithdrawBinding;
import com.cloudcreativity.wankeshop.entity.UserEntity;
import com.cloudcreativity.wankeshop.utils.BalancePayDialogUtils;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.SPUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 用户申请提现ViewModal
 */
public class ApplyWithDrawViewModal {
    private BaseDialogImpl baseDialog;
    private ApplyWithDrawActivity context;
    private ActivityApplyWithdrawBinding binding;
    public ObservableField<Float> money = new ObservableField<>();

    ApplyWithDrawViewModal(BaseDialogImpl baseDialog, ApplyWithDrawActivity context, ActivityApplyWithdrawBinding binding) {
        this.baseDialog = baseDialog;
        this.context = context;
        this.binding = binding;

        loadData();
    }

    public void onBack(View view){
        context.finish();
    }

    public void onApplyClick(View view){
        String s = binding.etMoney.getText().toString();
        if(TextUtils.isEmpty(s))
            return;
        try{
            float money = Float.parseFloat(s);
            if(money>this.money.get()){
                return;
            }
            applyWithDraw(money);
        }catch(Exception e){
            ToastUtils.showShortToast(context,"金额必须数字");
        }
    }

    //加载账户余额信息
    private void loadData(){
        HttpUtils.getInstance().getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        //加载账户余额信息
                        UserEntity userEntity = new Gson().fromJson(t, UserEntity.class);
                        //展示数据
                        money.set(Float.parseFloat(userEntity.getBalance()));
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    //申请提现
    private void applyWithDraw(final float money){
       HttpUtils.getInstance().registerGetVerRify(SPUtils.get().getUser().getMobile())
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new DefaultObserver<String>(baseDialog,true) {
                   @Override
                   public void onSuccess(String t) {
                       BalancePayDialogUtils utils = new BalancePayDialogUtils();
                       utils.setOnOkClickListener(new BalancePayDialogUtils.OnOkClickListener() {
                           @Override
                           public void onClick(String sms) {
                               HttpUtils.getInstance().applyWithdrawRequest(money,SPUtils.get().getUser().getMobile(),sms)
                                       .subscribeOn(Schedulers.io())
                                       .observeOn(AndroidSchedulers.mainThread())
                                       .subscribe(new DefaultObserver<String>(baseDialog,true) {
                                           @Override
                                           public void onSuccess(String t) {
                                               ToastUtils.showShortToast(context,"发起提现请求成功");
                                               context.finish();
                                           }

                                           @Override
                                           public void onFail(ExceptionReason msg) {

                                           }
                                       });
                           }
                       });

                       utils.show(context,baseDialog);
                   }

                   @Override
                   public void onFail(ExceptionReason msg) {

                   }
               });
    }
}
