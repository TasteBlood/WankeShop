package com.cloudcreativity.wankeshop.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.utils.APIService;
import com.cloudcreativity.wankeshop.utils.AppConfig;
import com.cloudcreativity.wankeshop.utils.LogUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 这是有关微信的回掉页面
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI api;
    private String access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    private String get_user_info = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
    private OkHttpClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, AppConfig.WX_APP_ID, true);
        //注册微信
        api.registerApp(AppConfig.WX_APP_ID);
        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            boolean result = api.handleIntent(getIntent(), this);
            if (!result) {
                LogUtils.e("xuxiwu", "参数不合法，未被SDK处理，退出");
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {
        LogUtils.e("xuxiwu——onReq", new Gson().toJson(baseReq));
    }

    @Override
    public void onResp(BaseResp baseResp) {
        String s = new Gson().toJson(baseResp);
        LogUtils.e("xuxiwu——onResq", s);
        try {
            if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                JSONObject object = new JSONObject(s);
                if (object.has("state") && "wechat_sdk_微信登录".equals(object.getString("state"))) {
                    client = new OkHttpClient().newBuilder()
                            .readTimeout(APIService.timeOut, TimeUnit.SECONDS)
                            .connectTimeout(APIService.timeOut, TimeUnit.SECONDS)
                            .build();
                    //这是微信登录
                    String code = object.getString("code");
                    getAccessToken(code);
                } else {
                    //这是微信支付
                }
            } else {
                LogUtils.e("xuxiwu——onResq", new Gson().toJson(baseResp));
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        api.handleIntent(data, this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    private void getAccessToken(String code) {
        Request request = new Request.Builder()
                .url(String.format(access_token_url, AppConfig.WX_APP_ID, AppConfig.WX_APP_SECRET, code))
                .get()
                .build();
        Call call = client.newCall(request);
        showProgress("请稍后");
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                WXEntryActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        WXEntryActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showShortToast(getApplicationContext(),"获取access_token失败");
                                dismissProgress();
                                finish();
                            }
                        });
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                WXEntryActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ResponseBody body = response.body();
                            String content = new String(body.bytes(),"UTF-8");
                            LogUtils.e("xuxiwu",content);
                            JSONObject object = new JSONObject(content);
                            if(object.has("access_token")){
                                getUserInfo(object.getString("access_token"),object.getString("unionid"));
                            }else{
                                ToastUtils.showShortToast(getApplicationContext(),"获取access_token失败");
                                dismissProgress();
                                finish();
                            }
                            //获取用户信息
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dismissProgress();
                            ToastUtils.showShortToast(getApplicationContext(),"数据解析失败");
                            finish();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            dismissProgress();
                            ToastUtils.showShortToast(getApplicationContext(),"数据解析失败");
                            finish();
                        } catch (IOException e) {
                            e.printStackTrace();
                            dismissProgress();
                            ToastUtils.showShortToast(getApplicationContext(),"数据解析失败");
                            finish();
                        }
                    }
                });
            }
        });
    }

    private void getUserInfo(String access_token,String unionId){
        Request request = new Request.Builder()
                .url(String.format(get_user_info,access_token,unionId))
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                WXEntryActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShortToast(getApplicationContext(),"获取用户信息失败");
                        dismissProgress();
                        finish();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                WXEntryActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            dismissProgress();
                            ResponseBody body = response.body();
                            String content = new String(body.bytes(),"UTF-8");
                            LogUtils.e("xuxiwu",content);
                            //获取用户信息
                            JSONObject object = new JSONObject(content);
                            Map<String,Object> map = new HashMap<>();
                            map.put("type","wx_auth");
                            map.put("userName",object.getString("nickname"));
                            map.put("avatar",object.getString("headimgurl"));
                            map.put("openId",object.getString("openid"));
                            EventBus.getDefault().post(map);
                            finish();
                        } catch (JSONException e) {
                            ToastUtils.showShortToast(getApplicationContext(),"获取用户信息失败");
                            finish();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            ToastUtils.showShortToast(getApplicationContext(),"获取用户信息失败");
                            finish();
                        } catch (IOException e) {
                            e.printStackTrace();
                            ToastUtils.showShortToast(getApplicationContext(),"获取用户信息失败");
                            finish();
                        } finally {
                            dismissProgress();
                            finish();
                        }
                    }
                });
            }
        });
    }

}
