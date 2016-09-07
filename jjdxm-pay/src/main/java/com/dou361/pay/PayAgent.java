package com.dou361.pay;

import android.app.Activity;
import android.os.Looper;

import com.dou361.pay.alipay.AlipayHelper;
import com.dou361.pay.wxpay.WechatPayHelper;

/**
 * 支付代理类
 */
public class PayAgent {

    public static PayType currentPayType;

    private boolean isInit;

    private static volatile PayAgent instance;


    private static final String TAG = PayAgent.class.getName();

    /***
     * 支付方式
     */
    public enum PayType {
        /**
         * 支付宝
         */
        ALIPAY,
        /**
         * 微信
         */
        WECHATPAY,
        /**
         * 支付宝授权
         */
        ALIAUTHV2
    }

    private static AlipayHelper mAlipayHelper;
    private static WechatPayHelper mWechatpayHelper;


    private PayAgent() {

    }


    public static PayAgent getInstance() {

        if (null == instance) {
            synchronized (PayAgent.class) {
                if (null == instance) {
                    instance = new PayAgent();
                }
            }
        }
        return instance;
    }

    public AlipayHelper getAlipayHelper() {
        if (null == mAlipayHelper) {
            mAlipayHelper = new AlipayHelper();
        }
        return mAlipayHelper;
    }

    public WechatPayHelper getWechatpayHelper() {
        if (null == mWechatpayHelper) {
            mWechatpayHelper = new WechatPayHelper();
        }
        return mWechatpayHelper;
    }

    /***
     * set debug modle
     */
    public void setDebug(boolean debug) {
        L.isDebug = debug;
    }

    /**
     * 初始化 支付组件
     */
    public synchronized boolean init(Activity activity) {
        if (isInit) {
            return true;
        }
        boolean success = true;
        success &= getWechatpayHelper().registerWechatApi(activity);
        isInit = true;
        return success;
    }

    /**
     * 初始化所有
     */
    public boolean initPay(String appId, String partnerId, String privateKey, String appwxId, String mchId,
                           String appKey) {
        return ConstantKeys.initKeys(appId, partnerId, privateKey, appwxId, mchId, appKey);
    }

    /**
     * 初始化支付宝 所需的appid ,appkey
     */
    public boolean initAliPayKeys(String appId, String partnerId, String privateKey) {
        return ConstantKeys.initAliPayKeys(appId, partnerId, privateKey);
    }

    /**
     * 初始化微信支付 所需的appid ,appkey .
     */
    public boolean initWxPayKeys(String appId, String mchId, String appKey) {
        return ConstantKeys.initWxPayKeys(appId, mchId, appKey);
    }

    /**
     * 支付宝 支付  <b>[ 同步调用 <i>即在主（ui）线程调用</i>]</b>
     */
    public void payOfAliPay(Activity activity, PayInfo payInfo, OnPayListener listener) {
        onPay(PayType.ALIPAY, activity, payInfo, listener);
    }

    /**
     * 微信支付 <b>[ 同步调用<i> 即在主（ui）线程调用</i>]</b>
     */
    public void payOfWechatPay(Activity activity, PayInfo payInfo, OnPayListener listener) {
        onPay(PayType.WECHATPAY, activity, payInfo, listener);
    }

    /**
     * 调起 支付 <b>[ 同步调用<i>即在主（ui）线程调用</i>]</b>
     *
     * @param payType
     * @param activity 调起支付 所在的 activity
     * @param payInfo  支付信息 [订单号，支付金额，商品名称，支付服务器回调地址..]
     *                 <p><i>PayInfo -> price 微信:交易金额默认为人民币交易，接口中参数支付金额单位为【分】，参数值不能带小数。</i></p>
     * @param listener 支付回调
     */
    public void onPay(PayType payType, Activity activity, PayInfo payInfo, OnPayListener listener) {
        currentPayType = payType;
        if (!isInit) {
            init(activity);
        }
        if (null == payInfo) {
            throw new IllegalArgumentException(" payinfo  is null!");
        }
        if (null == activity) {
            throw new IllegalArgumentException(" Activity  is null!");
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalArgumentException(Thread.currentThread().getName() + "'. " +
                    "onPay methods must be called on the UI thread. ");
        }
        switch (payType) {
            case ALIPAY:
                getAlipayHelper().signAndPayV2(activity, payInfo, listener);
                break;
            case WECHATPAY:
                getWechatpayHelper().signPay(activity, payInfo, listener);
                break;
            default:
                throw new IllegalArgumentException(" payType is ALIPAY or WXPAY ");
        }

    }


    /**
     * 调起 支付 <b>[ 同步调用<i>即在主（ui）线程调用</i>]</b>
     *
     * @param payType
     * @param activity 调起支付 所在的 activity
     * @param payInfo  支付信息 [订单号，支付金额，商品名称，支付服务器回调地址..]
     *                 <p><i>PayInfo -> price 微信:交易金额默认为人民币交易，接口中参数支付金额单位为【分】，参数值不能带小数。</i></p>
     * @param listener 支付回调
     */
    public void onPay(PayType payType, Activity activity, String payInfo, OnPayListener listener) {
        currentPayType = payType;
        if (!isInit) {
            init(activity);
        }
        if (null == payInfo) {
            throw new IllegalArgumentException(" payinfo  is null!");
        }
        if (null == activity) {
            throw new IllegalArgumentException(" Activity  is null!");
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalArgumentException(Thread.currentThread().getName() + "'. " +
                    "onPay methods must be called on the UI thread. ");
        }
        switch (payType) {
            case ALIPAY:
                getAlipayHelper().payV2(activity, payInfo, listener);
                break;
            case WECHATPAY:
                getWechatpayHelper().pay(activity, payInfo, listener);
                break;
            default:
                throw new IllegalArgumentException(" payType is ALIPAY or WXPAY ");
        }

    }

    public void onAuth(PayType payType, Activity activity, OnAuthListener listener) {
        currentPayType = payType;
        currentPayType = payType;

        if (!isInit) {
            init(activity);
        }
        if (null == activity) {
            throw new IllegalArgumentException(" Activity  is null!");
        }

        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalArgumentException(Thread.currentThread().getName() + "'. " +
                    "onPay methods must be called on the UI thread. ");
        }
        switch (payType) {
            case ALIAUTHV2:
                getAlipayHelper().authV2(activity, listener);
                break;

            default:
                throw new IllegalArgumentException(" payType is ALIAUTHV2 ");
        }
    }
}
