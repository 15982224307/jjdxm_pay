package com.dou361.pay;

import android.app.Activity;
import android.os.Looper;

import com.dou361.pay.alipay.AlipayHelper;
import com.dou361.pay.wxpay.WechatPayHelper;

/**
 * 支付代理类
 */
public class PayAgent {

    private static volatile PayAgent instance;

    private static final String TAG = PayAgent.class.getName();

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
    public PayAgent setDebug(boolean debug) {
        L.isDebug = debug;
        return instance;
    }

    /**
     * 初始化 支付组件
     */
    public synchronized PayAgent init(Activity activity) {
        getWechatpayHelper().registerWechatApi(activity);
        return instance;
    }

    /***
     * app内部下单初始化支付参数
     *
     * @param appId      必填：支付宝app_id
     * @param partnerId  可填：支付宝商户id，订单加签使用，如果使用服务器签好名的字符串可不传该参数
     * @param privateKey 可填：支付宝私有key，订单加签使用，如果使用服务器签好名的字符串可不传该参数
     * @param appwxId    必填：微信app_id
     * @param mchId      可填：微信商户id，订单加签使用，如果使用服务器签好名的字符串可不传该参数
     * @param appKey     可填：微信key，订单加签使用，如果使用服务器签好名的字符串可不传该参数
     * @return
     */
    public PayAgent initPay(String appId, String partnerId, String privateKey, String appwxId, String mchId,
                            String appKey) {
        ConstantKeys.initKeys(appId, partnerId, privateKey, appwxId, mchId, appKey);
        return instance;
    }

    /***
     * app支付已加签订单初始化支付参数
     *
     * @param appId   必填：支付宝app_id
     * @param appwxId 必填：微信app_id
     * @return
     */
    public PayAgent initOrderPay(String appId, String appwxId) {
        initPay(appId, null, null, appwxId, null, null);
        return instance;
    }

    /**
     * 初始化支付宝 所需的appid ,appkey
     */
    public PayAgent initAliPayKeys(String appId, String partnerId, String privateKey) {
        ConstantKeys.initAliPayKeys(appId, partnerId, privateKey);
        return instance;
    }

    /**
     * 初始化微信支付 所需的appid ,appkey .
     */
    public PayAgent initWxPayKeys(String appId, String mchId, String appKey) {
        ConstantKeys.initWxPayKeys(appId, mchId, appKey);
        return instance;
    }

    /**
     * 支付宝 支付  <b>[ 同步调用 <i>即在主（ui）线程调用</i>]</b>
     */
    public PayAgent payOfAliPay(Activity activity, PayInfo payInfo, OnPayListener listener) {
        onPay(PayType.ALIPAY, activity, payInfo, listener);
        return instance;
    }

    /**
     * 微信支付 <b>[ 同步调用<i> 即在主（ui）线程调用</i>]</b>
     */
    public PayAgent payOfWechatPay(Activity activity, PayInfo payInfo, OnPayListener listener) {
        onPay(PayType.WECHATPAY, activity, payInfo, listener);
        return instance;
    }

    /***
     * 调起 支付 app内处理加签操作，出于安全不建议使用，可以用作demo调试
     *
     * @param payType  支付类型
     * @param activity 所在的activity
     * @param payInfo  支付对象信息[订单号，支付金额，商品名称，支付服务器回调地址..]微信:交易金额默认为人民币交易，接口中参数支付金额单位为【分】，参数值不能带小数。
     * @param listener 支付回调
     * @return
     */
    public PayAgent onPay(PayType payType, Activity activity, PayInfo payInfo, OnPayListener listener) {
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
        return instance;
    }

    /***
     * 调起 支付 服务器加载后，回传加签数据
     *
     * @param payType  支付类型
     * @param activity 所在的activity
     * @param payInfo  服务器加签后的数据 例如：{"appid":"wxb4ba3c02aa476ea1","partnerid":"1305176001","package":"Sign=WXPay","noncestr":"6e778396de67b4a57310651d3302067c","timestamp":1473258151,"prepayid":"wx20160907222231ce5961f6a10963609051","sign":"E0C61AD6F45E26D20B8B4F2B69B4170B"}
     * @param listener 支付回调
     * @return
     */
    public PayAgent onPay(PayType payType, Activity activity, String payInfo, OnPayListener listener) {
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
        return instance;
    }

    /***
     * 阿里授权支付
     *
     * @param activity
     * @param listener
     * @return
     */
    public PayAgent onAliAuth(Activity activity, OnAuthListener listener) {
        if (null == activity) {
            throw new IllegalArgumentException(" Activity  is null!");
        }

        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalArgumentException(Thread.currentThread().getName() + "'. " +
                    "onPay methods must be called on the UI thread. ");
        }
        getAlipayHelper().authV2(activity, listener);
        return instance;
    }
}
