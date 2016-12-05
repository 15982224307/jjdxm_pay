package com.dou361.jjdxm_pay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dou361.jjdxm_pay.module.ApiServiceUtils;
import com.dou361.pay.L;
import com.dou361.pay.OnAuthListener;
import com.dou361.pay.OnPayListener;
import com.dou361.pay.PayAgent;
import com.dou361.pay.PayInfo;
import com.dou361.pay.PayType;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // 初始化支付组件
        PayAgent.getInstance()
                .init(this)
                .setDebug(true)
//                .initPay("2015121801006488", "2088311071300602", "MIICXQIBAAKBgQC1OoDgtrma4HfnmKm2ChPzAizThnGnUfNEnKkZWdQF8pyrZB8Khge2YZhH0GwwaoNp0OhR6QCZPr0cG2y1wZayVJE+FAEB2v3nQ2vJ8BSqxPyQyuE6pKGAPJh/0w5Z07XcTsJqKa5xs5gfHw+3Xx0gtjtg34PwrACxcdFHn2bkgQIDAQABAoGATVkJ1l7Ger6hDlyO2l4Uw5vDDAiOi24jmL4QQfyfzGYOzeuuf+xScFnZB5WCB2v+aHQ8I3GByuYHCm7+B9j2+q7ydGfX7qWQYsu17lOSJ1qldtcLkkdDmvf0HoHzNBzpurPuRZdBY/BtEYGpuGMisDJtmTbqjv7OZwQsAJK60v0CQQDtrPTJhN3L+qRAUniubeW6OM9CYAgxlWDLT3+vljpBjWXLC0U1vfiSCLnqWe1L18gNvS9JpVyRsSt/TmivtpIjAkEAwzNxDhzvCzR/f78/uaJZjLhpa0SyeUpR6Zww6STfgz2+4pkQo5dWN3PUQO+A51bHSmGflShbN7j8uvAklog/CwJBAL03l74jkCyHg2JOBhPgHCdQePi/2WYYJXJW/TF96S0s8+BdPaFWd2FTnyeKpldeF7+QYOhBxNuccCOu+bsCH38CQFXj+7oPByvyBKwMVhjzk920g0Zc6v8tsY9OV8Muo17XO3fvi/+/poMt51ZPTHP+niBfhl2WbVS+hA4pfp/yAXMCQQDjDH4avrljPvn/aVfccVi65GNsMNIxY1BVX0e6uH6KPpi9WH9xgZVUA6JVgS3WSLjz3ogrZLpvmV22onOJN0Vv", "wxd930ea5d5a258f4f", "1305176001", "fd48464480a242ab98586b7e6738277d");
                .initOrderPay("2015121801006488", "wxd930ea5d5a258f4f");

    }

    @OnClick({R.id.btn_wechat_pay, R.id.btn_wechat_order_pay, R.id.btn_ali_pay, R.id.btn_ali_order_pay, R.id.btn_ali_auth})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_wechat_pay: {
                PayInfo payInfo = new PayInfo();
                payInfo.setSubject("测试商品");
                payInfo.setPrice("1");
                payInfo.setNotifyUrl("www.dou361.com");
                payInfo.setBody("商品描述；");
                payInfo.setOrderNo("201507211420020069452");
                pay(PayType.WECHATPAY, payInfo);
            }
            break;
            case R.id.btn_wechat_order_pay:
                winchatPay();
                break;
            case R.id.btn_ali_pay: {
                PayInfo payInfo = new PayInfo();
                payInfo.setSubject("测试商品");
                payInfo.setPrice("0.01");
                payInfo.setNotifyUrl("www.dou361.com");
                payInfo.setBody("商品描述；");
                payInfo.setOrderNo("201507211420020069452");
                pay(PayType.ALIPAY, payInfo);
            }
            break;
            case R.id.btn_ali_order_pay:
                /**这里只是模拟一个加签后的订单支付*/
                orderPay(PayType.ALIPAY, "app_id=2015052600090779&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22seller_id%22%3A%22%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%220.02%22%2C%22subject%22%3A%221%22%2C%22body%22%3A%22%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22out_trade_no%22%3A%22314VYGIAGG7ZOYY%22%7D&charset=utf-8&method=alipay.trade.app.pay&sign_type=RSA&timestamp=2016-08-15%2012%3A12%3A15&version=1.0&sign=MsbylYkCzlfYLy9PeRwUUIg9nZPeN9SfXPNavUCroGKR5Kqvx0nEnd3eRmKxJuthNUx4ERCXe552EV9PfwexqW%2B1wbKOdYtDIb4%2B7PL3Pc94RZL0zKaWcaY3tSL89%2FuAVUsQuFqEJdhIukuKygrXucvejOUgTCfoUdwTi7z%2BZzQ%3D");
                break;
            case R.id.btn_ali_auth:
                aliAuth();
                break;
        }
    }

    /**
     * 获取服务器微信订单
     */
    private void winchatPay() {
        ApiServiceUtils.getWinChatBean(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                L.e("pay", body);
                if (body != null) {
                    orderPay(PayType.WECHATPAY, body);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    /**
     * 获取服务器加签好的订单并调起 支付平台
     */
    private void orderPay(PayType payType, String sign) {
        PayAgent.getInstance().onPay(payType, this, sign,
                new OnPayListener() {

                    @Override
                    public void onStartPay() {
                        Log.e("pay", "------onStartPay------");
                    }

                    @Override
                    public void onPaySuccess() {
                        Toast.makeText(MainActivity.this, "支付成功！", Toast.LENGTH_LONG).show();
                        Log.e("pay", "------onPaySuccess------");
                    }

                    @Override
                    public void onPayFail(String code, String msg) {
                        Toast.makeText(MainActivity.this,
                                "code:" + code + "msg:" + msg, Toast.LENGTH_LONG).show();
                        Log.e("pay", "code:" + code + "msg:" + msg);

                    }
                });
    }

    /**
     * 下单并调起 支付平台
     */
    private void pay(PayType payType, PayInfo payInfo) {
        PayAgent.getInstance().onPay(payType, this, payInfo,
                new OnPayListener() {

                    @Override
                    public void onStartPay() {
                        Log.e("pay", "------onStartPay------");
                    }

                    @Override
                    public void onPaySuccess() {
                        Toast.makeText(MainActivity.this, "支付成功！", Toast.LENGTH_LONG).show();
                        Log.e("pay", "------onPaySuccess------");
                    }

                    @Override
                    public void onPayFail(String code, String msg) {
                        Toast.makeText(MainActivity.this,
                                "code:" + code + "msg:" + msg, Toast.LENGTH_LONG).show();
                        Log.e("pay", "code:" + code + "msg:" + msg);

                    }
                });
    }


    /**
     * 调起 支付宝授权平台
     */
    private void aliAuth() {
        PayAgent.getInstance().onAliAuth(this,
                new OnAuthListener() {

                    @Override
                    public void onStartAuth() {
                        Log.e("pay", "------onStartAuth------");
                    }

                    @Override
                    public void onAuthSuccess() {
                        Toast.makeText(MainActivity.this, "授权成功！", Toast.LENGTH_LONG).show();
                        Log.e("pay", "------onAuthSuccess------");

                    }

                    @Override
                    public void onAuthFail(String code, String msg) {
                        Toast.makeText(MainActivity.this,
                                "code:" + code + "msg:" + msg, Toast.LENGTH_LONG).show();
                        Log.e("pay", "code:" + code + "msg:" + msg);

                    }
                });
    }

}
