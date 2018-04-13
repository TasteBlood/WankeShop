package com.cloudcreativity.wankeshop.userCenter;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityPerfectUserInfoBinding;
import com.cloudcreativity.wankeshop.userCenter.address.TempAddress;
import com.cloudcreativity.wankeshop.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Map;

/**
 * 完善用户信息
 */
public class PerfectUserInfoActivity extends BaseActivity {

    private PerfectUserInfoModal userInfoModal;
    private ActivityPerfectUserInfoBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_perfect_user_info);
        binding.setUserModal(userInfoModal = new PerfectUserInfoModal(this,binding));
    }

    //这边是图片处理成功
    @Override
    protected void onPhotoSuccess(String filePath) {
        userInfoModal.uploadImage(filePath);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        //在这里判断当前的地址信息是否存在
        StringBuffer buffer = new StringBuffer();
        if(TempAddress.provinceEntity!=null)
            buffer.append(TempAddress.provinceEntity.getName());
        if(TempAddress.cityEntity!=null)
            buffer.append(TempAddress.cityEntity.getName());
        if(TempAddress.areaEntity!=null)
            buffer.append(TempAddress.areaEntity.getName());
        if(TempAddress.streetEntity!=null)
            buffer.append(TempAddress.streetEntity.getName());

        if(buffer.length()>0)
            userInfoModal.setAddress(buffer.toString());

        userInfoModal.user = SPUtils.get().getUser();
        binding.invalidateAll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TempAddress.clear();

        EventBus.getDefault().unregister(this);
    }

    //接收微信登录返回的数据
    @Subscribe
    public void onEvent(Map<String,Object> wxLoginInfo){
        if(wxLoginInfo==null||wxLoginInfo.isEmpty())
            return;
        if(wxLoginInfo.get("type").toString().equals("wx_login")){
            //modal.onWeChatLoginCallback(wxLoginInfo.get("mobile").toString(),wxLoginInfo.get("password").toString());
        }else if(wxLoginInfo.get("type").toString().equals("wx_auth")){
            userInfoModal.onWeChatCallback(wxLoginInfo.get("avatar").toString(),
                    wxLoginInfo.get("userName").toString(),
                    wxLoginInfo.get("openId").toString());
        }

    }
}
