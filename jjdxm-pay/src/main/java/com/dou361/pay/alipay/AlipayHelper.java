package com.dou361.pay.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.dou361.pay.L;
import com.dou361.pay.OnAuthListener;
import com.dou361.pay.OnPayListener;
import com.dou361.pay.PayInfo;

import java.util.Map;

public class AlipayHelper {

    private static final String TAG = AlipayHelper.class.getName();

    public interface OnPayResultListener {
        public void onPayResult(String code, String msg);
    }

    protected static final int RQF_PAY = 0;
    protected static final int RQF_AUTH_FLAG = 1;
    private Activity activity;
    private String payInfo;

    private OnPayListener onPayResultListener;
    private OnAuthListener onOnAuthListener;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case RQF_PAY:
                    if (onPayResultListener != null) {

                        @SuppressWarnings("unchecked")
                        PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                        /**
                         对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                         */
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                            onPayResultListener.onPaySuccess();
                        } else if (TextUtils.isEmpty(resultInfo)) {
                            onPayResultListener.onPayFail(resultStatus,
                                    "网络异常，请刷新我的订单再试");
                        } else {
                            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                            // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            //支付结果确认中
                            if (TextUtils.equals(resultStatus, "8000")) {
                                onPayResultListener.onPayFail(resultStatus, resultInfo);
                                //支付失败
                            } else {
                                onPayResultListener.onPayFail(resultStatus, resultInfo);
                            }
                        }

                    }
                    break;
                case RQF_AUTH_FLAG:
                    if (onOnAuthListener != null) {
                        @SuppressWarnings("unchecked")
                        AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                        String resultInfo = authResult.getResult();
                        String resultStatus = authResult.getResultStatus();

                        // 判断resultStatus 为“9000”且result_code
                        // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                        if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                            // 获取alipay_open_id，调支付时作为参数extern_token 的value
                            // 传入，则支付账户为该授权账户
                            onOnAuthListener.onAuthSuccess();
                        } else if (TextUtils.isEmpty(resultInfo)) {
                            onOnAuthListener.onAuthFail(resultStatus,
                                    "网络异常，请刷新我的订单再试");
                        } else {
                            // 其他状态值则为授权失败
                            onOnAuthListener.onAuthFail(resultStatus, resultInfo);
                        }
                    }
                    break;
                default:
                    break;
            }
        }

    };

    public void signAndPayV2(final Activity activity, PayInfo info,
                              OnPayListener l) {

        this.activity = activity;
        this.onPayResultListener = l;
        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        OrderInfoUtil2_0 generator = new OrderInfoUtil2_0(info);
        payInfo = generator.generatePayInfo();
        new Thread(new Runnable() {

            @Override
            public void run() {

                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (null != onPayResultListener) {
                            onPayResultListener.onStartPay();
                        }
                    }
                });

                PayTask aliPay = new PayTask(activity);
                // 设置为沙箱模式，不设置默认为线上环境?
                // aliPay.setSandBox(true);

                Map<String, String> result = aliPay.payV2(payInfo, true);
                L.i(AlipayHelper.class.getName(), "pay result :" + result.toString());
                Message msg = new Message();
                msg.what = RQF_PAY;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        }).start();
    }

    public void payV2(final Activity activity, final String info,
                      OnPayListener l) {

        this.activity = activity;
        this.onPayResultListener = l;

        new Thread(new Runnable() {

            @Override
            public void run() {

                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (null != onPayResultListener) {
                            onPayResultListener.onStartPay();
                        }
                    }
                });

                PayTask aliPay = new PayTask(activity);
                // 设置为沙箱模式，不设置默认为线上环境?
                // aliPay.setSandBox(true);

                Map<String, String> result = aliPay.payV2(info, true);
                L.i(AlipayHelper.class.getName(), "pay result :" + result.toString());
                Message msg = new Message();
                msg.what = RQF_PAY;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        }).start();
    }

    public void authV2(final Activity activity,
                       OnAuthListener l) {

        this.activity = activity;
        this.onOnAuthListener = l;

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        OrderInfoUtil2_0 generator = new OrderInfoUtil2_0(null);
        final String authInfo = generator.generateAuthInfo("");


        new Thread(new Runnable() {

            @Override
            public void run() {

                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (null != onOnAuthListener) {
                            onOnAuthListener.onStartAuth();
                        }
                    }
                });

                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(activity);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);
                L.i(AlipayHelper.class.getName(), "pay result :" + result.toString());
                Message msg = new Message();
                msg.what = RQF_AUTH_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        }).start();
    }
}
