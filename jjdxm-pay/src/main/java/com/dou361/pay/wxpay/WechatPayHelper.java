package com.dou361.pay.wxpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.dou361.pay.ConstantKeys;
import com.dou361.pay.L;
import com.dou361.pay.OnPayListener;
import com.dou361.pay.PayInfo;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

public class WechatPayHelper {

    private static final String TAG = WechatPayHelper.class.getName();

    private static OnPayListener mListener;

    private IWXAPI msgApi;

    public void signPay(final Activity activity, PayInfo info, OnPayListener listener) {

        mListener = listener;
        if (null == msgApi) {
            registerWechatApi(activity);
        }
        PayUrlGenerator payUrlGenerator = new PayUrlGenerator(info);
        PayReq req = payUrlGenerator.genSignPayReq();
        if (null != mListener) {
            mListener.onStartPay();
        }

        msgApi.sendReq(req);
    }

    public void pay(final Activity activity, String data, OnPayListener listener) {
        mListener = listener;
        if (null == msgApi) {
            registerWechatApi(activity);
        }
        PayReq req = new PayReq();
        try {
            JSONObject json = new JSONObject(data);
            if (null != json && !json.has("retcode")) {
                req.appId = json.getString("appid");
                req.partnerId = json.getString("partnerid");
                req.prepayId = json.getString("prepayid");
                req.nonceStr = json.getString("noncestr");
                req.timeStamp = json.getString("timestamp");
                req.packageValue = json.getString("package");
                req.sign = json.getString("sign");
                req.extData = "app data";
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != mListener) {
            mListener.onStartPay();
        }
        msgApi.sendReq(req);
    }

    /**
     * 注册 微信sdk 到app
     */
    public boolean registerWechatApi(final Activity activity) {
        if (null == msgApi) {
            msgApi = WXAPIFactory.createWXAPI(activity, null);
        }
        return msgApi.registerApp(ConstantKeys.WxPay.APP_ID);
    }

    /**
     * 接收 支付回调
     */
    public static void handleOnResp(BaseResp resp) {
        L.d(TAG, " ====  handleOnResp ,resp:" + resp.toString() + " === ");
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX && resp.errCode == 0) {
            if (null != mListener) {
                mListener.onPaySuccess();
            }
        } else {
            if (null != mListener) {
                mListener.onPayFail(String.valueOf(resp.errCode), resp.errStr);
            }
        }

    }

    public static void handleonReq(BaseReq req) {
        L.d(TAG, " ====== handleonReq =====");
    }

    public static void handleIntent(Intent intent, Context context) {

    }

}
