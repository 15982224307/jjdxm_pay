package com.dou361.jjdxm_pay.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.dou361.pay.ConstantKeys;
import com.dou361.pay.L;
import com.dou361.pay.wxpay.WechatPayHelper;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, ConstantKeys.WxPay.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        WechatPayHelper.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        L.d(TAG, " === wxPay onReq " + req.toString() + " === ");
        WechatPayHelper.handleonReq(req);
    }

    @Override
    public void onResp(BaseResp resp) {
        L.d(TAG, " ==== wxPay onResp ===" + resp.errStr + ";code=" + String.valueOf(resp.errCode));
        WechatPayHelper.handleOnResp(resp);
        this.finish();
    }
}