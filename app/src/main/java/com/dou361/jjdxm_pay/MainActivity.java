package com.dou361.jjdxm_pay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dou361.jjdxm_pay.module.ApiServiceUtils;
import com.dou361.pay.L;
import com.dou361.pay.OnAuthListener;
import com.dou361.pay.OnPayListener;
import com.dou361.pay.PayAgent;
import com.dou361.pay.PayInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button aliPayBtn, authV2Btn, wxPayBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化支付组件
        PayAgent payAgent = PayAgent.getInstance();
        payAgent.setDebug(true);
        payAgent.init(this);
        payAgent.initPay("2015121801006488", "2088311071300602", "MIICXQIBAAKBgQC1OoDgtrma4HfnmKm2ChPzAizThnGnUfNEnKkZWdQF8pyrZB8Khge2YZhH0GwwaoNp0OhR6QCZPr0cG2y1wZayVJE+FAEB2v3nQ2vJ8BSqxPyQyuE6pKGAPJh/0w5Z07XcTsJqKa5xs5gfHw+3Xx0gtjtg34PwrACxcdFHn2bkgQIDAQABAoGATVkJ1l7Ger6hDlyO2l4Uw5vDDAiOi24jmL4QQfyfzGYOzeuuf+xScFnZB5WCB2v+aHQ8I3GByuYHCm7+B9j2+q7ydGfX7qWQYsu17lOSJ1qldtcLkkdDmvf0HoHzNBzpurPuRZdBY/BtEYGpuGMisDJtmTbqjv7OZwQsAJK60v0CQQDtrPTJhN3L+qRAUniubeW6OM9CYAgxlWDLT3+vljpBjWXLC0U1vfiSCLnqWe1L18gNvS9JpVyRsSt/TmivtpIjAkEAwzNxDhzvCzR/f78/uaJZjLhpa0SyeUpR6Zww6STfgz2+4pkQo5dWN3PUQO+A51bHSmGflShbN7j8uvAklog/CwJBAL03l74jkCyHg2JOBhPgHCdQePi/2WYYJXJW/TF96S0s8+BdPaFWd2FTnyeKpldeF7+QYOhBxNuccCOu+bsCH38CQFXj+7oPByvyBKwMVhjzk920g0Zc6v8tsY9OV8Muo17XO3fvi/+/poMt51ZPTHP+niBfhl2WbVS+hA4pfp/yAXMCQQDjDH4avrljPvn/aVfccVi65GNsMNIxY1BVX0e6uH6KPpi9WH9xgZVUA6JVgS3WSLjz3ogrZLpvmV22onOJN0Vv", "wxd930ea5d5a258f4f", "1305176001", "fd48464480a242ab98586b7e6738277d");
        initViews();

    }

    private void initViews() {
        aliPayBtn = (Button) findViewById(R.id.payV2);
        authV2Btn = (Button) findViewById(R.id.authV2);
        wxPayBtn = (Button) findViewById(R.id.weichatpay);
        aliPayBtn.setOnClickListener(this);
        authV2Btn.setOnClickListener(this);
        wxPayBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.payV2:
                PayInfo payInfo = new PayInfo();
                payInfo.setSubject("测试商品");
                payInfo.setPrice("20");
                payInfo.setNotifyUrl("www.cs.not");
                payInfo.setBody("商品描述；");
                payInfo.setOrderNo("201507211420020069452");
                aliPay(payInfo);
                break;
            case R.id.authV2:
                testAuth(PayAgent.PayType.ALIAUTHV2);
                break;
            case R.id.weichatpay:
                winchatPay();
                break;
            default:
                break;
        }

    }

    private void winchatPay() {
        ApiServiceUtils.getWinChatBean(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                L.e("pay", body);
                if (body != null) {
                    wxPay(body);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void wxPay(String sign) {
        PayAgent.getInstance().onPay(PayAgent.PayType.WECHATPAY, this, sign,
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
     * 调起 支付平台
     */
    private void aliPay(PayInfo payInfo) {
        PayAgent.getInstance().onPay(PayAgent.PayType.ALIPAY, this, payInfo,
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
    private void testAuth(PayAgent.PayType payType) {

        PayAgent.getInstance().onAuth(payType, this,
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
