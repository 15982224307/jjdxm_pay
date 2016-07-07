package com.dou361.jjdxm_pay;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dou361.pay.OnPayListener;
import com.dou361.pay.PayAgent;
import com.dou361.pay.PayInfo;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog progressDialog;
    private Button aliPayBtn, wxPayBtn, upPayBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化支付组件
        PayAgent payAgent = PayAgent.getInstance();
        payAgent.setDebug(true);
        payAgent.initPay(this);

        initViews();

    }

    private void initViews() {
        progressDialog = new ProgressDialog(MainActivity.this);

        aliPayBtn = (Button) findViewById(R.id.alipay);
        wxPayBtn = (Button) findViewById(R.id.weichatpay);
        upPayBtn = (Button) findViewById(R.id.uppay);

        aliPayBtn.setOnClickListener(this);
        wxPayBtn.setOnClickListener(this);
        upPayBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.alipay:

                PayInfo payInfo = new PayInfo();
                payInfo.setSubject("测试商品");
                payInfo.setPrice("20");
                payInfo.setNotifyUrl("www.cs.not");
                payInfo.setBody("商品描述；");
                payInfo.setOrderNo("201507211420020069452");
                testPay(PayAgent.PayType.ALIPAY, payInfo);
                break;

            case R.id.weichatpay:
                PayInfo info = new PayInfo();
                info.setOrderNo("201507211420020069452");
                testPay(PayAgent.PayType.WECHATPAY, info);
                break;

            case R.id.uppay:

                requestTestOrderNo();

                break;

            default:
                break;
        }

    }

    /**
     * 获取 测试 订单号 ，银联支付
     *
     * @return void
     * @autour BaoHong.Li
     * @date 2015-7-21 下午3:09:11
     * @update (date)
     */
    private void requestTestOrderNo() {

        new AsyncTask<String, Integer, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setTitle("获取订单中...");
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {

                String tn = null;
                InputStream is;
                try {

                    String url = "http://202.101.25.178:8080/sim/gettn";

                    URL myURL = new URL(url);
                    URLConnection ucon = myURL.openConnection();
                    ucon.setConnectTimeout(120000);
                    is = ucon.getInputStream();
                    int i = -1;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while ((i = is.read()) != -1) {
                        baos.write(i);
                    }

                    tn = baos.toString();
                    is.close();
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.i(getClass().getName(), "response :" + tn);

                return tn;
            }


            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                progressDialog.dismiss();

                // 获取到订单后 调起 支付
                PayInfo inf = new PayInfo();
                inf.setOrderNo(result);
                testPay(PayAgent.PayType.UPPAY, inf);
            }


        }.execute();


    }

    /**
     * 调起 支付平台
     *
     * @param payType
     * @param payInfo
     * @return void
     * @autour BaoHong.Li
     * @date 2015-7-21 下午2:39:21
     * @update (date)
     */
    private void testPay(PayAgent.PayType payType, PayInfo payInfo) {

        PayAgent.getInstance().onPay(payType, this, payInfo,
                new OnPayListener() {

                    @Override
                    public void onStartPay() {

                        progressDialog.setTitle("加载中。。。");
                        progressDialog.show();
                    }

                    @Override
                    public void onPaySuccess() {
//						public void onPaySuccess(String code, String msg) {

                        Toast.makeText(MainActivity.this, "支付成功！", Toast.LENGTH_LONG).show();

                        if (null != progressDialog) {
                            progressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onPayFail(String code, String msg) {
                        Toast.makeText(MainActivity.this,
                                "code:" + code + "msg:" + msg, Toast.LENGTH_LONG).show();
                        Log.e(getClass().getName(), "code:" + code + "msg:" + msg);

                        if (null != progressDialog) {
                            progressDialog.dismiss();
                        }
                    }
                });
    }
}
